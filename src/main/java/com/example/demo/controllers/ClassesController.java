package com.example.demo.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.NotificationRepository;

@Controller
@RequestMapping("/Account")
public class ClassesController {
	
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	NotificationRepository notiRepository;
	
//	@GetMapping("/detailNoti/{noti}")
//    public String detailNotification(@PathVariable long notifyId,Model model) {
//		Optional<Notification> notiDetail = notiRepository.findById(notifyId); 
//		Notification currentNoti;
//        if(notiDetail.isPresent())
//        {
//        	currentNoti = notiDetail.get();
//    		model.addAttribute("noti", currentNoti);
//        }       	
//        //error -> error
//        return "/Notification/Noti-Detail";
//        
//    }
	
	
	
}
