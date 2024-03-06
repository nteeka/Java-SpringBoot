package com.example.demo.viewController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Faculty;
import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.FacultyService;
import com.example.demo.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/StudentView")
public class StudentViewController {
	
	
	@Autowired
    private StudentService studentService;
	
	@Autowired
    private FacultyService facultyService;
	
	
    @GetMapping("/listStudent")
    public String showStudentList(Model model) {
        List<Student> students = studentService.getAllStudents();  
        model.addAttribute("students", students);

        return "/Student/Student-List";
        
    }
    
	
	@GetMapping("/createStudent")
    public String showCreateStudentView(Model m) {
		List<Faculty> faculties = facultyService.getAllFaculties();
		m.addAttribute("faculties",faculties);
		m.addAttribute("student", new Student());
        return "/Student/Student-Create";
    }	
	
	@GetMapping("/editStudent/{id}")    
    public String edit(@PathVariable("id") long id, Model m){    
        Optional<Student> student = studentService.getStudentById(id);
        List<Faculty> faculties = facultyService.getAllFaculties();
       
        String imageUrl = "/image/getImage/" + student.get().getImage();
        m.addAttribute("student",student);  
        m.addAttribute("imageUrl", imageUrl);
        m.addAttribute("faculties",faculties);
        return "/Student/Student-Edit";    
    }    
     
	
	
	
	
	

	

	
}
