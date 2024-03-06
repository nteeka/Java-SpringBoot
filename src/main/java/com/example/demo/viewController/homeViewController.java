package com.example.demo.viewController;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Role;
import com.example.demo.services.RoleService;


@Controller
@RequestMapping("/Home")
public class homeViewController {
	
	@Autowired
    private RoleService roleService;
	
	@GetMapping("/index")
    public String showHomePage() {
        return "index";       
    }
	
	@GetMapping("/login")
    public String showLoginPage() {
        return "/Authen/Login";       
    }
	
	@GetMapping("/register")
    public String showRegisterPage(Model model) {
		List<Role> roles = roleService.getAllRoles();
		model.addAttribute("roles", roles);
	    model.addAttribute("role", new Role()); // Add this line to create a new Role object
        return "/Authen/Register";       
    }
}
