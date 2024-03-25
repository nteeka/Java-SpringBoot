package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.repositories.ClassAccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReplyCommentRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Notify")
public class NotificationController {
		
	@Autowired
	NotificationRepository notiRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	ReplyCommentRepository replyRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	@Autowired
	ClassRepository classRepository;
	
	@Autowired
	ClassAccountRepository classAccountRepository;
	
	@Autowired
	HomeworkRepository homeworkRepository;
	
	@Autowired
	NotificationRepository notifyRepository;
	
	@GetMapping("/detailNoti/{notifyId}")
    public String showNotiDetail(@PathVariable long notifyId, HttpServletRequest request,Model m) {
        

		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
	    
	    Optional<Notification> notiDetail = notiRepository.findById(notifyId); 
		Notification currentNoti;
        
        
        	currentNoti = notiDetail.get();
    		m.addAttribute("noti", currentNoti);
         
        
        
	  //get class id
	    Optional<Classes> c = classRepository.findById(currentNoti.getClasses().getClassId());
	    m.addAttribute("id", c.get().getClassId());	    
		m.addAttribute("c", c.get());
	    //get class list which is already joined
	    List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());  
	    List<Classes> classes = new ArrayList<Classes>();
	    for (ClassAccount classes2 : account) {
	    	if(classes2.getClasses().getClassId() != currentNoti.getClasses().getClassId())
	    		classes.add(classes2.getClasses());
	    }
	    m.addAttribute("classes",classes);
	    
	    List<Homework> hw = homeworkRepository.findByClassId(currentNoti.getClasses().getClassId());
	    //findByClassId
	    
	    m.addAttribute("hw",hw);
	    
	    //get all account on this class
	    List<ClassAccount> acc = classAccountRepository.findByClassId(currentNoti.getClasses().getClassId());
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
	    
	    List<ReplyComment> listReply = replyRepository.findAll();	    
	    m.addAttribute("listReply",listReply);
	    
	    List<CommentLike> checkLikeComment = commentLikeRepository.findByAccountId(loggedInUser.getAccountId());	    
	    List<Long> check = new ArrayList<Long>();
	    for (CommentLike commentLike : checkLikeComment) {
			for (Comment cmt22 : listComment) {
				if(commentLike.getComment().getCommentId() == cmt22.getCommentId())
					check.add(cmt22.getCommentId());
			}
		}
	    m.addAttribute("checkLikeComment",check);
	    
	    return "/Notification/Noti-Detail";
        
    }
}
