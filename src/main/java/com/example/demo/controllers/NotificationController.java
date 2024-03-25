package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Notification;
import com.example.demo.repositories.NotificationRepository;

@Controller
@RequestMapping("/Notify")
public class NotificationController {
		
	@Autowired
	NotificationRepository notiRepository;
	
	@GetMapping("/listNoti")
    public String showStudentList(Model model) {
        List<Notification> noti = notiRepository.findAll();  //add without deleted and by ClassId later !!
        model.addAttribute("noti", noti);

        return "/Notification/Noti-List";
        
    }
}
