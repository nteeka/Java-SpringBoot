package com.example.demo.controllers;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.FileAttach;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.models.SubmitHomework;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.FileAttachRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReplyCommentRepository;
import com.example.demo.repositories.SubmitHomeworkRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Class")
public class ClassesController {
	
	
	@Autowired
    private ClassRepository classRepository;
	
	
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
	
	@Autowired
	SubmitHomeworkRepository submitHomeworkRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private FileAttachRepository fileAttachRepository;
	
	
	@GetMapping("/listClass")
	public String listClassView(Model m,HttpServletRequest request) {	
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    // Kiểm tra xem tài khoản đã đăng nhập chưa
	    if (loggedInUser == null) {
	        // Xử lý khi tài khoản chưa đăng nhập
	        return "/Authen/Login";
	    }
	    Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());

	    List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());  
	    if(account.isEmpty())
	    {
	    	//lấy ảnh user cho header
	    	m.addAttribute("account",acc.get());
	    	return "/Classes/Class-List_Empty";

	    }
	    List<Classes> classes = new ArrayList<Classes>();
	    for (ClassAccount classes2 : account) {
	    	classes.add(classes2.getClasses());
	    }
	    //sẽ thấy được toàn bộ lớp mà người trong session login đã tạo
//	    List<Classes> c = classRepository.findByAccountId(loggedInUser.getAccountId());
//	    classes.addAll(c);
	    m.addAttribute("classes",classes);
	    //lấy ảnh user cho header
    	m.addAttribute("account",acc.get());
	    return "/Classes/Class-List";
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
		
	    return "redirect:/Class/listClass";
		
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
	        return "redirect:/Class/listClass";
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
		
//		HttpSession session = request.getSession();
//	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
//
//	    if (loggedInUser == null) {
//	        return "/Authen/Login";
//	    }
//		//get class id
//	    Optional<Classes> c = classRepository.findById(id);
//	    m.addAttribute("id", c.get().getClassId());	    
//		m.addAttribute("c", c.get());
//	    //get class list which is already joined
//	    List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());  
//	    List<Classes> classes = new ArrayList<Classes>();
//	    for (ClassAccount classes2 : account) {
//	    	if(classes2.getClasses().getClassId() != id)
//	    		classes.add(classes2.getClasses());
//	    }
//	    m.addAttribute("classes",classes);
//	    
//	    //all homework
//	    List<Homework> hw = homeworkRepository.findByClassId(id);
//	    m.addAttribute("hw",hw);
//	    
//	    //check done or not done
//	    List<Long> listSubmit = submitHomeworkRepository.listHomeworkByAccountId(loggedInUser.getAccountId());	    
//	    m.addAttribute("listSubmit",listSubmit);
//	    
//	    //get all account on this class
//	    List<ClassAccount> acc = classAccountRepository.findByClassId(id);
//	    List<Account> listAccount = new ArrayList<Account>();
//	    for (ClassAccount lstAcc : acc)
//	    {
//	    	//phân biệt người tạo ra lớp với người tham gia
//	    	if(lstAcc.getAccount().getAccountId() != c.get().getAccount().getAccountId())
//	    		listAccount.add(lstAcc.getAccount());
//	    }
//	    m.addAttribute("listAccount",listAccount);
//	    
//	    List<Notification> notifies = notifyRepository.findByClassId(c.get().getClassId());
//	    m.addAttribute("notifies",notifies);
//	    
//	    List<Long> lstNotifyId = notifyRepository.listNotifyId(c.get().getClassId());
//	    m.addAttribute("lstNotifyId",lstNotifyId);
//	    
//	    List<Comment> listComment = commentRepository.findAllNotDeleted();	    
//	    m.addAttribute("listComment",listComment);
//	    
//	    List<CommentLike> checkLikeComment = commentLikeRepository.findByAccountId(loggedInUser.getAccountId());
//	    
//	    List<Long> check = new ArrayList<Long>();
//	    for (CommentLike commentLike : checkLikeComment) {
//			for (Comment cmt22 : listComment) {
//				if(commentLike.getComment().getCommentId() == cmt22.getCommentId())
//					check.add(cmt22.getCommentId());
//			}
//		}
//	    m.addAttribute("checkLikeComment",check);
//	    
//	    List<ReplyComment> listReply = replyRepository.findAll();
//	    
//	    m.addAttribute("listReply",listReply);
//	    
//	    
//	    m.addAttribute("checkLikeComment",check);
//	    
//	    Long countMember = classAccountRepository.countAccountsInClass(id);
//	    m.addAttribute("countMember",countMember);
//	    List<SubmitHomework> countSubmited = submitHomeworkRepository.countSubmited(id);
//	    m.addAttribute("countSubmited",countSubmited);
//	    Map<Long, Integer> countSubmittedByHomeworkId = new HashMap<>();
//	    for (SubmitHomework submission : countSubmited) {
//	        Long homeworkId = submission.getHomework().getHomeworkId();
//	        countSubmittedByHomeworkId.put(homeworkId, countSubmittedByHomeworkId.getOrDefault(homeworkId, 0) + 1);
//	    }
//
//	    m.addAttribute("countSubmittedByHomeworkId", countSubmittedByHomeworkId);
//	   
//	    //update submitdHomework
//	    List<SubmitHomework> listSubmitHomework = submitHomeworkRepository.findAll();	    
//	    m.addAttribute("listSubmitHomework",listSubmitHomework);
//	    for (SubmitHomework submitHomework : listSubmitHomework) {
//	    	submitHomework.getHomework().getDeadline().isAfter(null);
//		}
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
	    
	    //all homework
	    List<Homework> hw = homeworkRepository.findByClassId(id);
	    m.addAttribute("hw",hw);
	    
	    //check done or not done
	    List<Long> listSubmit = submitHomeworkRepository.listHomeworkByAccountId(loggedInUser.getAccountId());	    
	    m.addAttribute("listSubmit",listSubmit);
	    
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
	    
	    List<Long> lstNotifyId = notifyRepository.listNotifyId(c.get().getClassId());
	    m.addAttribute("lstNotifyId",lstNotifyId);
	    
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
	    
	    
	    m.addAttribute("checkLikeComment",check);
	    
	    
	    Long countMember = classAccountRepository.countAccountsInClass(id);
	    m.addAttribute("countMember",countMember);
	    List<SubmitHomework> countSubmited = submitHomeworkRepository.countSubmited(id);
	    m.addAttribute("countSubmited",countSubmited);
	    Map<Long, Integer> countSubmittedByHomeworkId = new HashMap<>();
	    for (SubmitHomework submission : countSubmited) {
	        Long homeworkId = submission.getHomework().getHomeworkId();
	        countSubmittedByHomeworkId.put(homeworkId, countSubmittedByHomeworkId.getOrDefault(homeworkId, 0) + 1);
	    }

	    m.addAttribute("countSubmittedByHomeworkId", countSubmittedByHomeworkId);
	   
	    //update submitdHomework
	    List<SubmitHomework> listSubmitHomework = submitHomeworkRepository.findAll();	    
	    m.addAttribute("listSubmitHomework",listSubmitHomework);
	    
	    Optional<Account> accountImg = accountRepository.findById(loggedInUser.getAccountId());
	    
        m.addAttribute("account",accountImg.get());
        
        List<FileAttach> listFile = fileAttachRepository.findAll();
		m.addAttribute("listFile", listFile);
	    
	    
	    return "/Classes/Class-Content";
	}
	
	
	
}
