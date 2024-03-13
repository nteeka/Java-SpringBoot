package com.example.demo.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.DetailTeachingRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/Teacher")
public class TeacherController {
	
	@Autowired
    private DetailTeachingRepository detailTeachingRepository;
	
	@Autowired
    private ClassRepository classRepository;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private ClassAccountRepository classAccountRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
	public String createClass(@RequestParam("className") String name,HttpServletRequest request)			
	{      
		Classes classes = new Classes();
		classes.setClassName(name);		
		HttpSession session = request.getSession();
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");	
        if(loggedInUser==null)
        	classes.setAccount(null);
		classes.setAccount(loggedInUser);
		classRepository.save(classes);
	    return "redirect:/StudentView/listStudent";
		
	}
	@GetMapping("/listClass")
  public String listClassView(Model m,HttpServletRequest request) {	
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    // Kiểm tra xem tài khoản đã đăng nhập chưa
	    if (loggedInUser == null) {
	        // Xử lý khi tài khoản chưa đăng nhập
	        return "/Authen/Login";
	    }
	List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());  
	List<Classes> classes = new ArrayList<Classes>();
	for (ClassAccount classes2 : account) {
		classes.add(classes2.getClasses());
	}
	m.addAttribute("classes",classes);
    return "/Classes/Class-List";
  }
	

	@PostMapping("/joinClass")
	public String joinClass(@RequestParam("classId") String id,Model model,HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    // Kiểm tra xem tài khoản đã đăng nhập chưa
	    if (loggedInUser == null) {
	        // Xử lý khi tài khoản chưa đăng nhập
	        return "/Authen/Login";
	    }

	    // Tìm lớp học dựa trên ID
	    Optional<Classes> optionalClass = classRepository.findById(id);
//	    Optional<ClassAccount> optional = classAccountRepository.findByClassId(id);
	    List<ClassAccount> optional = classAccountRepository.findByClassId(id);

	    if (optionalClass.isPresent()) {
	        Classes classes = optionalClass.get();
	        // Kiểm tra xem tài khoản đã tham gia lớp học chưa
	        for (ClassAccount classAccount : optional) {
	            if (classAccount.getAccount().getAccountId() == loggedInUser.getAccountId()) {
	                // Xử lý khi tài khoản đã tham gia lớp học
	            	model.addAttribute("error", "already join class with ID: " + id);
	    	        return "/Classes/Class-List";
	            }
	        }

	        // Tạo một đối tượng ClassAccount mới và lưu vào cơ sở dữ liệu
	        ClassAccount classAccount = new ClassAccount();
	        classAccount.setClasses(classes);
	        classAccount.setAccount(loggedInUser);
	        classAccount.setNum(1); // Set số lượng hoặc các thuộc tính khác nếu cần

	        classAccountRepository.save(classAccount);
	        
	        // Xử lý khi tham gia lớp học thành công
	        model.addAttribute("success", "Join class with ID: " + id + " successfull");
	        return "redirect:/Teacher/listClass";
	    } else {
	    	model.addAttribute("error", "Class not found with ID: " + id);
	        return "/Classes/Class-List";
	    }
	}

}
