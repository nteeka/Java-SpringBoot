package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Role;
import com.example.demo.services.RoleService;

@Controller
@RequestMapping("/Role")
public class RoleController {
	
	@Autowired
    private RoleService roleService;
	
	@GetMapping("/createRole")
    public String showCreateRoleView() {
        return "/Role/Role-Create";
    }
	
	@PostMapping("/addNewRole")
	public String createRole(@RequestParam("roleName") String name)			
	{      
		Role role = new Role();
		role.setRoleName(name);		
		roleService.saveRole(role);
	    return "redirect:/StudentView/listStudent";
		
	}
}
