package com.example.demo.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.models.SubmitHomework;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReplyCommentRepository;
import com.example.demo.repositories.SubmitHomeworkRepository;

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
	
	
	
	
	
	
	
	
	

	
	

	
//	@GetMapping("/enterClass/{classId}")
//	public String enterClassView(@PathVariable("classId") String id,Model m,HttpServletRequest request) {	
//		
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
//	    
//	    return "/Classes/Class-Content";
//	}
	
	
	
	
	
	
}
