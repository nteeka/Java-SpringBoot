package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.models.Account;
import com.example.demo.models.Classes;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.repositories.CommentLikeRepository;
import com.example.demo.repositories.CommentRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Comment")
public class CommentController {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	CommentLikeRepository commentLikeRepository;
	
	
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
		cmt.setDateCreated(LocalDate.now());
		cmt.setNotify(notify);	
		commentRepository.save(cmt);
        return "redirect:/StudentView/listStudent";
    }
	
	
	
	@PostMapping("/updateComment/{commentId}")
    public String updateNotification(@PathVariable("commentId") long id,
    							@RequestParam("content") String content) {	       	                      
		Optional<Comment> currentCmt = commentRepository.findById(id);
		Comment newCmt = currentCmt.get();
		newCmt.setContent(content);	
		newCmt.setLastModified(LocalDate.now());
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
		CommentLike cmtLiked = new CommentLike();
		cmtLiked.setAccount(loggedInUser);
		cmtLiked.setComment(currentCmt.get());
		int increaseLike = currentCmt.get().getLikeNumber();
		currentCmt.get().setLikeNumber(increaseLike++);
		commentRepository.save(currentCmt.get());
		commentLikeRepository.save(cmtLiked);
		return "redirect:/Teacher/enterClass/" + currentCmt.get().getNotify().getClasses().getClassId();
    }
}
