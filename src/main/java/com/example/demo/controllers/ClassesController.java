package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.repositories.ClassRepository;

@Controller
@RequestMapping("/Account")
public class ClassesController {
	
	
	@Autowired
	ClassRepository classRepository;
	
	
}
