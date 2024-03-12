package com.example.demo.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Classes;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.DetailTeachingRepository;


@Controller
@RequestMapping("/Teacher")
public class TeacherController {
	
	@Autowired
    private DetailTeachingRepository detailTeachingRepository;
	
	@Autowired
    private ClassRepository classRepository;
	
	@GetMapping("/createHomework")
    public String createHomeworkView(Model m) {
		
//		 List<Classes> classes = detailTeachingRepository.get();		       
//	        String imageUrl = "/image/getImage/" + student.getImage();
//	        List<Classes> classes1 = classRepository.findAll();			        
//	        model.addAttribute("classes", classes1);
		
		//phải làm cái đăng nhập lấy đc id teacher
        return "/Teacher/Homework-Create";
    }
//	@GetMapping("/createClass")
//    public String createClassView(Model m) {
//		
//        return "/Classes/Class-Create";
//    }
	@PostMapping("/addNewClass")
	public String createClass(@RequestParam("className") String name)			
	{      
		Classes classes = new Classes();
		classes.setClassName(name);		
		classRepository.save(classes);
	    return "redirect:/StudentView/listStudent";
		
	}
	@GetMapping("/listClass")
  public String listClassView(Model m) {		
	List<Classes> classes = classRepository.findAllNotDeleted();
	m.addAttribute("classes",classes);
    return "/Classes/Class-List";
  }
	
	@PostMapping("/joinClass")
	public String joinClass(@RequestParam("classId") String id,Model model)			
	{      
	    Optional<Classes> optionalClasses = classRepository.findByYourId(id);
	    if (optionalClasses.isPresent()) {
	    	
	    	model.addAttribute("success", "Join class with ID: " + id + " successfull");
	        return "/Classes/Class-List";
	    } else {
	        model.addAttribute("error", "Class not found with ID: " + id);
	        return "/Classes/Class-List";
	    }


		
	}
}
