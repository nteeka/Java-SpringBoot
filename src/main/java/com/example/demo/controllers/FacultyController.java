package com.example.demo.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Faculty;
import com.example.demo.models.Student;
import com.example.demo.repositories.FacultyRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.FacultyService;

@Controller
@RequestMapping("/Faculty")
public class FacultyController {
	
	
	@Autowired
    private FacultyService facultyService;
	
	
	
	@PostMapping("/addNewFaculty")
	public String createFaculty(@RequestParam("facultyName") String name)			
	{      
		Faculty faculty = new Faculty();
		faculty.setFacultyName(name);		
		facultyService.saveFaculty(faculty);
	    return "redirect:/StudentView/listStudent";
		
	}
}
