package com.example.demo.viewController;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Faculty;
import com.example.demo.models.Student;
import com.example.demo.services.FacultyService;
import com.example.demo.services.StudentService;


@Controller
@RequestMapping("/StudentView")
public class StudentViewController {
	
	
	@Autowired
    private StudentService studentService;
	
	@Autowired
    private FacultyService facultyService;
	
	
    @GetMapping("/listStudent")
    public String showStudentList(Model model) {
        List<Student> students = studentService.getAllStudents();  
        model.addAttribute("students", students);

        return "/Student/Student-List";
        
    }
    
	
	@GetMapping("/createStudent")
    public String showCreateStudentView(Model m) {
		List<Faculty> faculties = facultyService.getAllFaculties();
		m.addAttribute("faculties",faculties);
		m.addAttribute("student", new Student());
		
        return "/Student/Student-Create";
    }	
	
	@GetMapping("/editStudent/{id}")    
    public String edit(@PathVariable("id") long id, Model m){    
        Optional<Student> student = studentService.getStudentById(id);
        List<Faculty> faculties = facultyService.getAllFaculties();
       
        String imageUrl = "/image/getImage/" + student.get().getImage();
        m.addAttribute("student",student);  
        m.addAttribute("imageUrl", imageUrl);
        m.addAttribute("faculties",faculties);
        return "/Student/Student-Edit";    
    }    
     
	
	
	
	
	

	

	
}
