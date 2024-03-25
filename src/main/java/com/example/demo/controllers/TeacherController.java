package com.example.demo.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Faculty;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.models.Student;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.DetailTeachingRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReplyCommentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/Teacher")
public class TeacherController {
	
	
	
	@Autowired
    private ClassRepository classRepository;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private ClassAccountRepository classAccountRepository;
	
	@Autowired
    private HomeworkRepository homeworkRepository;
	
	@Autowired
    private NotificationRepository notifyRepository;
	
	@Autowired
    private CommentRepository commentRepository;
	
	@Autowired
    private CommentLikeRepository commentLikeRepository;
	
	@Autowired
    private ReplyCommentRepository replyRepository;
	
	@Autowired
	NotificationRepository notiRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
//	@GetMapping("/createHomework/{classId}")
//    public String createHomeworkView(@PathVariable("classId") String id,Model m) {
//		Optional<Classes> c = classRepository.findById(id);
//		m.addAttribute("classes", c);
//        return "/Teacher/Homework-Create";
//    }
	
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
	    String uniqueFileName = homeWork.getHomeworkId() + "_homework_" + fileNameWithoutExtension + "." + fileExtension;

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
	
	@PostMapping("/updateHomework")
    public String updateHomework(@RequestParam("homeworkId") long id,
    							@RequestParam("homeworkName") String name,
    							@RequestParam("description") String description,   
    							@RequestParam("deadline") LocalDate deadline,  
    							@RequestParam("filePath") MultipartFile multipartFile) {	       	               
        
		Optional<Homework> currentHomework = homeworkRepository.findById(id);
		Homework newHw = currentHomework.get();
		newHw.setHomeworkName(name);
		newHw.setLastModified(LocalDate.now());
		newHw.setDescription(description);
		newHw.setDeadline(deadline);
		
		if(multipartFile.isEmpty())
		{
			homeworkRepository.save(newHw);
			return "redirect:/Teacher/enterClass/" + newHw.getClasses().getClassId();
		}
		String originalFilename = multipartFile.getOriginalFilename();
	    int lastDotIndex = originalFilename.lastIndexOf('.');
	    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
	    String fileExtension = originalFilename.substring(lastDotIndex + 1);
	    String uniqueFileName = newHw.getHomeworkId() + "_homework_" + fileNameWithoutExtension + "." + fileExtension;

	    Path path = Paths.get("fileUploads/");
	    try {
	        InputStream inputStream = multipartFile.getInputStream();
	        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
	        newHw.setFilePath(uniqueFileName.toLowerCase());       
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		homeworkRepository.save(newHw);
    	return "redirect:/Teacher/enterClass/" + newHw.getClasses().getClassId();
    }
	
	
	
	//chuyển qua bên NotiController đê !!
	@PostMapping("/createNotification")
    public String createNotification(@RequestParam("classes") Classes classes,
    								@RequestParam("title") String title,
    								@RequestParam("content") String content,
    								@RequestParam("filePath") MultipartFile[] multipartFile,
    								HttpServletRequest request) {
		
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
		
		Notification notify = new Notification();
		notify.setClasses(classes);
		notify.setTitle(title);
		notify.setContent(content);
		notify.setAccount(loggedInUser);
		notify.setDateCreated(LocalDate.now());
		notifyRepository.save(notify);
		
		List<String> fileNames = new ArrayList<>();
		
		
		
		Path path = Paths.get("fileUploads/");
		for (MultipartFile files : multipartFile)
		{
			String originalFilename = files.getOriginalFilename();
		    int lastDotIndex = originalFilename.lastIndexOf('.');
		    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
		    String fileExtension = originalFilename.substring(lastDotIndex + 1);
		    String uniqueFileName = notify.getNotifyId() + "_notify_" + fileNameWithoutExtension + "." + fileExtension;

		    
		    try {	        
		    	InputStream inputStream = files.getInputStream();
		        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
		        fileNames.add(uniqueFileName.toLowerCase());       
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		notify.setFilePath(fileNames);
	    notifyRepository.save(notify);
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
	    //findByClassId
	    
	    m.addAttribute("hw",hw);
	    
	    //get all account on this class
	    List<ClassAccount> acc = classAccountRepository.findByClassId(id);
	    List<Account> listAccount = new ArrayList<Account>();
	    for (ClassAccount lstAcc : acc)
	    {
	    	//phân biệt người tạo ra lớp với người tham gia
	    	if(lstAcc.getAccount().getAccountId() != c.get().getAccount().getAccountId())
	    		listAccount.add(lstAcc.getAccount());
	    }
	    m.addAttribute("listAccount",listAccount);
	    
	    List<Notification> notifies = notifyRepository.findByClassId(c.get().getClassId());
	    m.addAttribute("notifies",notifies);
	    
	    List<Comment> listComment = commentRepository.findAllNotDeleted();	    
	    m.addAttribute("listComment",listComment);
	    
	    List<CommentLike> checkLikeComment = commentLikeRepository.findByAccountId(loggedInUser.getAccountId());
	    
	    List<Long> check = new ArrayList<Long>();
	    for (CommentLike commentLike : checkLikeComment) {
			for (Comment cmt22 : listComment) {
				if(commentLike.getComment().getCommentId() == cmt22.getCommentId())
					check.add(cmt22.getCommentId());
			}
		}
	    m.addAttribute("checkLikeComment",check);
	    
	    List<ReplyComment> listReply = replyRepository.findAll();
	    
	    m.addAttribute("listReply",listReply);
	    
//	    List<Notification> noti = notiRepository.findAll();  //add without deleted and by ClassId later !!
//        m.addAttribute("noti", noti);
	    	    
	    return "/Classes/Class-Content";
	}
	
	
	
	
	@PostMapping("/updateNoti")
    public String updateNotification(@RequestParam("notifyId") long id,
    							@RequestParam("title") String title,
    							@RequestParam("content") String content,   							
    							@RequestParam("filePath") MultipartFile multipartFile) {	       	               
        
		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification newNoti = currentNoti.get();
		newNoti.setContent(content);
		newNoti.setLastModifed(LocalDate.now());
		newNoti.setTitle(title);
		
		if(multipartFile.isEmpty())
		{
			notifyRepository.save(newNoti);
			return "redirect:/Teacher/enterClass/" + newNoti.getClasses().getClassId();
		}
//		String originalFilename = multipartFile.getOriginalFilename();
//	    int lastDotIndex = originalFilename.lastIndexOf('.');
//	    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
//	    String fileExtension = originalFilename.substring(lastDotIndex + 1);
//	    String uniqueFileName = newNoti.getNotifyId() + "_notify_" + fileNameWithoutExtension + "." + fileExtension;
//
//	    Path path = Paths.get("fileUploads/");
//	    try {
//	        InputStream inputStream = multipartFile.getInputStream();
//	        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
//	        newNoti.setFilePath(uniqueFileName.toLowerCase());       
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
		notifyRepository.save(newNoti);
    	return "redirect:/Teacher/enterClass/" + newNoti.getClasses().getClassId();
    }
	
	
	@GetMapping("/deleteNoti/{notifyId}")
    public String deleteNotification(@PathVariable("notifyId") long id) {	       	               
        
		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification deletedNoti = currentNoti.get();
		deletedNoti.setDeleted(true);	
		notifyRepository.save(deletedNoti);
    	return "redirect:/Teacher/enterClass/" + deletedNoti.getClasses().getClassId();
    }
	
	@GetMapping("/deleteHomework/{homeworkId}")
    public String deleteHomework(@PathVariable("homeworkId") long id) {	       	               
        
		Optional<Homework> currentHW = homeworkRepository.findById(id);
		Homework deletedHW = currentHW.get();
		deletedHW.setDeleted(true);	
		homeworkRepository.save(deletedHW);
    	return "redirect:/Teacher/enterClass/" + deletedHW.getClasses().getClassId();
    }
	
}
