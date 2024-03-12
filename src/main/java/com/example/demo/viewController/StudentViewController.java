package com.example.demo.viewController;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Classes;
import com.example.demo.models.Faculty;
import com.example.demo.models.Student;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.services.FacultyService;
import com.example.demo.services.StudentService;


@Controller
@RequestMapping("/StudentView")
public class StudentViewController {
	
	
	@Autowired
    private StudentService studentService;
	
	@Autowired
    private FacultyService facultyService;
	
	
	
	@Autowired
    private ClassRepository classRepository;
	
    @GetMapping("/listStudent")
    public String showStudentList(Model model) {
        List<Student> students = studentService.getAllStudents();  
        model.addAttribute("students", students);

        return "/Student/Student-List";
        
    }
    
	
	@GetMapping("/createStudent")
    public String showCreateStudentView(Model m) {
		List<Faculty> faculties = facultyService.getAllFaculties();
		List<Classes> classes = classRepository.findAll();
        m.addAttribute("classes", classes);
		m.addAttribute("faculties",faculties);
		m.addAttribute("student", new Student());
//		Optional<Classes> studentClass = classRepository.findById((long) 3);		    		       
//		if (studentClass.isPresent()) {
//		    Classes classes1 = studentClass.get();
//		    List<Student> students = classes1.getStudents();
//
//		    for (Student student : students) {
//		        System.out.println("Student ID: " + student.getId());
//		        System.out.println("Student Name: " + student.getName());
//		        System.out.println("Student Age: " + student.getAge());
//		        // Add other student information as needed
//		    }
//		    students.clear();
//
//		    classRepository.save(classes1);
//		} else {
//		    System.out.println("Classes not found with ID 1");
//		}
		
        return "/Student/Student-Create";
    }	
	
	@GetMapping("/editStudent/{id}")    
    public String edit(@PathVariable("id") long id, Model m){    
        Optional<Student> student = studentService.getStudentById(id);
        List<Faculty> faculties = facultyService.getAllFaculties();
       
        String imageUrl = "/image/getImage/" + student.get().getImage();
        List<Classes> classes = classRepository.findAll();
        m.addAttribute("classes", classes);
        m.addAttribute("student",student);  
        m.addAttribute("imageUrl", imageUrl);
        m.addAttribute("faculties",faculties);
        return "/Student/Student-Edit";    
    }    
     
	
	
	
	
	
	

	

	
}
