package com.example.demo.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Faculty;
import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.FacultyService;
import com.example.demo.services.StudentService;

@Controller
@RequestMapping("/Students")
public class StudentController {
	 @Autowired
	 	private StudentService studentService;
	 @Autowired
	    private StudentRepository studentRepository;
	 @Autowired
	    private BCryptPasswordEncoder passwordEncoder;
	 @Autowired
	    private FacultyService facultyService;
	 
	    @GetMapping
	    public List<Student> getAllStudents() {
	        return studentService.getAllStudents();
	    }

	    @GetMapping("/{id}")
	    public Optional<Student> getStudentById(@PathVariable Long id) {
	        return studentService.getStudentById(id);
	    }
	    
	    @PostMapping("/addNewStudent")
		public String createStudent(@RequestParam("name") String name, 
									@RequestParam("age") int age,
									@RequestParam("faculty") Faculty faculty,
									@RequestParam("email") String email,
						
									@RequestParam("image") MultipartFile img,
									Model model)
				
		{      
			Student student = new Student();
			
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
			if (studentService.isEmailTaken(email) || !email.matches(emailRegex) ) {
		        model.addAttribute("emailError", "Email is invalid or Email is already taken. Please choose a different one.");
		        List<Faculty> faculties = facultyService.getAllFaculties();
		        model.addAttribute("faculties", faculties);
		        model.addAttribute("student", new Student());
		        return "/Student/Student-Create";
		    }
			
			 // Loại bỏ khoảng trắng ở đầu và cuối chuỗi
			name = name.trim();
			// Kiểm tra độ dài của tên
		    if (name.length() == 0 || name.length() > 50) {
		        model.addAttribute("nameError", "Name must be non-empty and have a maximum length of 50 characters.");
		        List<Faculty> faculties = facultyService.getAllFaculties();
		        model.addAttribute("faculties", faculties);
		        model.addAttribute("student", new Student());
		        return "/Student/Student-Create";
		    }

		    // Kiểm tra không chấp nhận ký tự đặc biệt và số
		    Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

		    Matcher matcher = pattern.matcher(name);
		    if (!matcher.matches()) {
		        model.addAttribute("nameError", "Name must not contain special characters or numbers.");
		        List<Faculty> faculties = facultyService.getAllFaculties();
		        model.addAttribute("faculties", faculties);
		        model.addAttribute("student", new Student());
		        return "/Student/Student-Create";
		    }
		    
		    
		 
		    if (age <= 0 || age > 100 ) {
		        model.addAttribute("ageError", "Age is invalid.");
		        List<Faculty> faculties = facultyService.getAllFaculties();
		        model.addAttribute("faculties", faculties);
		        model.addAttribute("student", new Student());
		        return "/Student/Student-Create";
		    }

		    
			
		    
			
			
			student.setName(name);
			student.setAge(age);
			student.setFaculty(faculty);
			student.setEmail(email);
//	        String hashedPassword = passwordEncoder.encode(password);
//	        student.setPassword(hashedPassword);
			Path path = Paths.get("uploads/");
			try{
				InputStream inputStream = img.getInputStream();
				Files.copy(inputStream, path.resolve(img.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
				student.setImage(img.getOriginalFilename().toLowerCase());
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		    studentService.saveStudent(student);
		    return "redirect:/StudentView/listStudent";			
		}
	    
	    

	    @GetMapping("/delete/{id}")
	    public String deleteStudent(@PathVariable Long id) {
	    	Optional<Student> optionalStudent = studentRepository.findById(id);
	        if (optionalStudent.isPresent()) {
	            Student student = optionalStudent.get();
	            student.setDeleted(true);
	            studentRepository.save(student); // Save the changes back to the database
	            return "redirect:/StudentView/listStudent";   
	        }
	        return "redirect:/StudentView/listStudent";
	    }
	    
	    @PostMapping("/{id}")
	    public String updateStudent(@PathVariable("id") long id,
	    							@RequestParam("name") String Name,
	    							@RequestParam("age") int Age,
	    							@RequestParam("email") String Email,
	    							@RequestParam("faculty") Faculty faculty,
	    							@RequestParam("image") MultipartFile multipartFile,
	    							Model model) {	       	               
	        
	    	Optional<Student> existingStudent = studentRepository.findById(id);
	        if (existingStudent.isPresent()) {
	            Student student = existingStudent.get();
	           
	            
            
	            Name = Name.trim();
				// Kiểm tra độ dài của tên
			    if (Name.length() == 0 || Name.length() > 50) {
			        model.addAttribute("nameError", "Name must be non-empty and have a maximum length of 50 characters.");
			        Optional<Student> student1 = studentService.getStudentById(id);
			        List<Faculty> faculties = facultyService.getAllFaculties();		       
			        String imageUrl = "/image/getImage/" + student.getImage();
			        model.addAttribute("student",student1);  
			        model.addAttribute("imageUrl", imageUrl);
			        model.addAttribute("faculties",faculties);
			        return "/Student/Student-Edit";
			    }
			    // Kiểm tra không chấp nhận ký tự đặc biệt và số
			    Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
			    Matcher matcher = pattern.matcher(Name);
			    if (!matcher.matches()) {
			        model.addAttribute("nameError", "Name must not contain special characters or numbers.");
			        Optional<Student> student1 = studentService.getStudentById(id);
			        List<Faculty> faculties = facultyService.getAllFaculties();		       
			        String imageUrl = "/image/getImage/" + student.getImage();
			        model.addAttribute("student",student1);  
			        model.addAttribute("imageUrl", imageUrl);
			        model.addAttribute("faculties",faculties);
			        return "/Student/Student-Edit";			        
			    }
			    
	            
			    if (Age <= 0 || Age > 100 ) {
			        model.addAttribute("ageError", "Age is invalid.");
			        Optional<Student> student1 = studentService.getStudentById(id);
			        List<Faculty> faculties = facultyService.getAllFaculties();		       
			        String imageUrl = "/image/getImage/" + student.getImage();
			        model.addAttribute("student",student1);  
			        model.addAttribute("imageUrl", imageUrl);
			        model.addAttribute("faculties",faculties);
			        return "/Student/Student-Edit";
			    }
	            
			    
	            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	            if (!Email.matches(emailRegex) || studentService.isEmailTaken(Email,id)) {
	            	Optional<Student> student1 = studentService.getStudentById(id);
			        model.addAttribute("emailError", "Email is already taken or Email is invalid. Please choose a different one.");
			        List<Faculty> faculties = facultyService.getAllFaculties();		       
			        String imageUrl = "/image/getImage/" + student.getImage();
			        model.addAttribute("student",student1);  
			        model.addAttribute("imageUrl", imageUrl);
			        model.addAttribute("faculties",faculties);
			        return "/Student/Student-Edit";
			    }	
	            student.setName(Name);
	            student.setEmail(Email);
	            student.setAge(Age);
	            student.setFaculty(faculty);
	    		Path path = Paths.get("uploads/");
	    		try{
	    			InputStream inputStream = multipartFile.getInputStream();
	    			Files.copy(inputStream, path.resolve(multipartFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
	    			student.setImage(multipartFile.getOriginalFilename().toLowerCase());
	    		}catch (Exception e) {
	    			e.printStackTrace();
	    		}           
	    		studentRepository.save(student);
	    		return "redirect:/StudentView/listStudent";       
	        } else {
	        	return "redirect:/StudentView/listStudent";
	        }
	    }
	    
}
