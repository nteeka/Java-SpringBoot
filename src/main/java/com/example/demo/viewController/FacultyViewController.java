package com.example.demo.viewController;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Faculty;
import com.example.demo.models.Student;

@Controller
@RequestMapping("/FacultyView")
public class FacultyViewController {

	
//	@GetMapping("/listFalculty")
//    public String showStudentList(Model model) {
//        List<Faculty> faculties = studentService.getAllStudents();  
//        model.addAttribute("students", students);
//        return "Student-List";
//        
//    }
	@GetMapping("/createFaculty")
    public String showCreateFalcultyView() {
        return "/Faculty/Faculty-Create";
    }
	
	
}
