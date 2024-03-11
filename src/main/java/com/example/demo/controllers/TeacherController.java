package com.example.demo.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Classes;
import com.example.demo.models.Faculty;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.DetailTeachingRepository;


@Controller
@RequestMapping("/Teacher")
public class TeacherController {
	
	@Autowired
    private DetailTeachingRepository detailTeachingRepository;
	
	@GetMapping("/createHomework")
    public String createHomeworkView(Model m) {
		
//		 List<Classes> classes = detailTeachingRepository.get();		       
//	        String imageUrl = "/image/getImage/" + student.getImage();
//	        List<Classes> classes1 = classRepository.findAll();			        
//	        model.addAttribute("classes", classes1);
		
		//phải làm cái đăng nhập lấy đc id teacher
        return "/Teacher/Homework";
    }
}
