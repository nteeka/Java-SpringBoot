package com.example.demo.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Homework;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.DetailTeachingRepository;
import com.example.demo.repositories.HomeworkRepository;

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
	
	@Autowired
    private HomeworkRepository homeworkRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/createHomework/{classId}")
    public String createHomeworkView(@PathVariable("classId") String id,Model m) {
		Optional<Classes> c = classRepository.findById(id);
		m.addAttribute("classes", c);
        return "/Teacher/Homework-Create";
    }
	
	//missing validate
	
	@PostMapping("/createHomework")
    public String createHomework(@RequestParam("homeworkName") String homeworkName,
    								@RequestParam("classes") Classes classes,
    								@RequestParam("description") String description,
    								@RequestParam("deadline") LocalDate deadline,
    								@RequestParam("filePath") MultipartFile multipartFile) {
		Homework homeWork = new Homework();
		homeWork.setClasses(classes);
		homeWork.setHomeworkName(homeworkName);
		homeWork.setDescription(description);
		homeWork.setDeadline(deadline);
		homeWork.setDateCreated(LocalDate.now());
		
		// Tạo tên file duy nhất bằng cách kết hợp ID của bài tập và tên file gốc
	    String originalFilename = multipartFile.getOriginalFilename();
	    int lastDotIndex = originalFilename.lastIndexOf('.');
	    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
	    String fileExtension = originalFilename.substring(lastDotIndex + 1);
	    String uniqueFileName = homeWork.getHomeworkId() + "_" + fileNameWithoutExtension + "." + fileExtension;

	    Path path = Paths.get("fileUploads/");
	    try {
	        InputStream inputStream = multipartFile.getInputStream();
	        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
	        homeWork.setFilePath(uniqueFileName.toLowerCase());       
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		homeworkRepository.save(homeWork);
        return "redirect:/StudentView/listStudent";
    }
	@PostMapping("/addNewClass")
	public String createClass(@RequestParam("className") String name,
							@RequestParam("description") String desc,
								HttpServletRequest request)			
	{      
		Classes classes = new Classes();
		classes.setClassName(name);		
		classes.setDescription(desc);
		HttpSession session = request.getSession();
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");	
        if(loggedInUser==null)
        	classes.setAccount(null);
		classes.setAccount(loggedInUser);
		classRepository.save(classes);
		
		
		//người tạo đc add vào lớp
		ClassAccount classAccount = new ClassAccount();
        classAccount.setClasses(classes);
        classAccount.setAccount(loggedInUser);
        classAccount.setNum(1); // Set số lượng hoặc các thuộc tính khác nếu cần
        classAccountRepository.save(classAccount);
		
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
	    //sẽ thấy được toàn bộ lớp mà người trong session login đã tạo
//	    List<Classes> c = classRepository.findByAccountId(loggedInUser.getAccountId());
//	    classes.addAll(c);
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
	
	
	
	@GetMapping("/leaveClass/{classId}")
	public String leaveClass(@PathVariable("classId") String id,Model model,HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
	    ClassAccount deleteClassAccount = classAccountRepository.findByClassIdAndAccountId(id.trim(), loggedInUser.getAccountId());
	    classAccountRepository.delete(deleteClassAccount);
	    model.addAttribute("success", "Leave class with ID: " + id + " successfull");
        return "/Classes/Class-List";
	    	        
	     
	}
	
	
	@GetMapping("/enterClass/{classId}")
	public String enterClassView(@PathVariable("classId") String id,Model m,HttpServletRequest request) {	
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
		//get class id
	    Optional<Classes> c = classRepository.findById(id);
	    m.addAttribute("id", c.get().getClassId());	    
		m.addAttribute("c", c.get());
	    //get class list which is already joined
	    List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());  
	    List<Classes> classes = new ArrayList<Classes>();
	    for (ClassAccount classes2 : account) {
	    	if(classes2.getClasses().getClassId() != id)
	    		classes.add(classes2.getClasses());
	    }
	    m.addAttribute("classes",classes);
	    
	    List<Homework> hw = homeworkRepository.findByClassId(id);
	    m.addAttribute("hw",hw);
	    return "/Classes/Class-Content";
	}

}
