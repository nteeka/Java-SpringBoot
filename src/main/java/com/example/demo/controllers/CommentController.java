package com.example.demo.controllers;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Account;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReplyCommentRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Comment")
public class CommentController {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	@Autowired
	NotificationRepository notifyRepository;
	
	@Autowired
	ReplyCommentRepository replyRepository;
	
	
	@PostMapping("/createNotifyComment")
    public String createComment(@RequestParam("content") String content,
    							@RequestParam("notify") Notification notify,
    							HttpServletRequest request) {
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
		Comment cmt = new Comment();
		cmt.setAccount(loggedInUser);
		cmt.setContent(content);
		cmt.setDateCreated(LocalDateTime.now());		
		cmt.setNotify(notify);	
		Optional<Notification> noti = notifyRepository.findById(notify.getNotifyId());
		if(noti.isPresent())
		{
			long numCMT= noti.get().getNumComment();
			numCMT = numCMT +1;
			noti.get().setNumComment(numCMT);
			notifyRepository.save(noti.get());
		}
			
		commentRepository.save(cmt);
		return "redirect:/Teacher/enterClass/" + notify.getClasses().getClassId();
    }
	
	
	
	@PostMapping("/updateComment/{commentId}")
    public String updateNotification(@PathVariable("commentId") long id,
    							@RequestParam("content") String content) {	       	                      
		Optional<Comment> currentCmt = commentRepository.findById(id);
		Comment newCmt = currentCmt.get();
		newCmt.setContent(content);	
		newCmt.setLastModified(LocalDateTime.now());
		commentRepository.save(newCmt);
		return "redirect:/StudentView/listStudent";
    }
	
	
	@GetMapping("/deleteComment/{commentId}")
    public String deleteNotification(@PathVariable("commentId") long id) {	       	               
        
		Optional<Comment> currentCmt = commentRepository.findById(id);
		Comment deletedCmt= currentCmt.get();
		deletedCmt.setDeleted(true);	
		commentRepository.save(deletedCmt);
		return "redirect:/StudentView/listStudent";
    }
	
	
	@GetMapping("/likeComment/{commentId}")
    public String likeComment(@PathVariable("commentId") long id, HttpServletRequest request) {	       	                      
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		Optional<Comment> currentCmt = commentRepository.findById(id);
		
		int increaseLike = currentCmt.get().getLikeNumber();
		
		
		//check like
		Optional<CommentLike> isLiked = commentLikeRepository.checkLiked(loggedInUser.getAccountId(), id);
		//unlike
		if(isLiked.isPresent())
		{
			increaseLike = increaseLike - 1;
			currentCmt.get().setLikeNumber(increaseLike);
			commentLikeRepository.deleteById(isLiked.get().getCommentLikeId());
			//return ??;
		}
		//like
		else
		{
			CommentLike cmtLiked = new CommentLike();
			cmtLiked.setAccount(loggedInUser);
			cmtLiked.setComment(currentCmt.get());
			increaseLike = increaseLike+1;
			currentCmt.get().setLikeNumber(increaseLike);			
			commentLikeRepository.save(cmtLiked);
		}
		commentRepository.save(currentCmt.get());
		return "redirect:/Teacher/enterClass/" + currentCmt.get().getNotify().getClasses().getClassId();
    }
	
	
	
	
	
	@PostMapping("/createReplyComment")
    public String createReplyComment(@RequestParam("content") String content,
    							@RequestParam("comment") Comment comment,
    							HttpServletRequest request) {
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
		ReplyComment reply = new ReplyComment();
		reply.setAccount(loggedInUser);
		reply.setContent(content);
		reply.setDateCreated(LocalDateTime.now());		
		reply.setComment(comment);	
		//count numReply each cmt
//		Optional<Notification> noti = notifyRepository.findById(notify.getNotifyId());
//		if(noti.isPresent())
//		{
//			long numCMT= noti.get().getNumComment();
//			numCMT = numCMT +1;
//			noti.get().setNumComment(numCMT);
//			notifyRepository.save(noti.get());
//		}
			
		replyRepository.save(reply);
		return "redirect:/Teacher/enterClass/" + comment.getNotify().getClasses().getClassId();
    }
	
	
	
	
	
	
}
