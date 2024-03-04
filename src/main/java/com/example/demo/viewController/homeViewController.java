package com.example.demo.viewController;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Student;

@Controller
@RequestMapping("/Home")
public class homeViewController {
	
	@GetMapping("/index")
    public String showHomePage() {
        return "index";       
    }
	
	@GetMapping("/login")
    public String showLoginPage() {
        return "Login";       
    }
	
	@GetMapping("/register")
    public String showRegisterPage() {
        return "Register";       
    }
}
