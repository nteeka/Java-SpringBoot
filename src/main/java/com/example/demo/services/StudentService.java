package com.example.demo.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;

@Service
public class StudentService { 
	@Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAllNotDeleted();
    }

    public Optional<Student> getStudentById(long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public String deleteStudent(long id) {
    	Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setDeleted(true);
            studentRepository.save(student); // Save the changes back to the database
            return "redirect:/StudentView/listStudent";   
        }
        return "redirect:/StudentView/listStudent";   
//        studentRepository.deleteById(id);
        
    }
    public Student updateStudent(Long id, Student updatedStudent,MultipartFile multipartFile) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            if(updatedStudent.getName() != null && updatedStudent.getName() != "")
            	student.setName(updatedStudent.getName());
            if(updatedStudent.getAge() != 0)
            	student.setAge(updatedStudent.getAge());
            
            
    			Path path = Paths.get("uploads/");
    			try{
    				InputStream inputStream = multipartFile.getInputStream();
    				Files.copy(inputStream, path.resolve(multipartFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
    				student.setImage(multipartFile.getOriginalFilename().toLowerCase());
    			}catch (Exception e) {
    				// TODO: handle exception
    				e.printStackTrace();
    			}           
            return studentRepository.save(student);       
            // Update other fields as needed
        } else {
            // Handle case where the student with the given id is not found
            return null;
        }
    }
    public boolean isEmailTaken(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }
    public boolean isEmailTaken(String email,long id) {
        return studentRepository.findByEmailAndIdNot(email,id).isPresent();
    }
    public boolean containsUppercaseAndLowercase(String str) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;

        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }

        return hasUppercase && hasLowercase;
    }
    
}
