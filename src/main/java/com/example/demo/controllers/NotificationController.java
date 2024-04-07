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
	    return "redirect:/Class/enterClass/" + notify.getClasses().getClassId();
    }
	
	
	@PostMapping("/updateNoti")
    public String updateNotification(@RequestParam("notifyId") long id,
    							@RequestParam("title") String title,
    							@RequestParam("content") String content,   							
    							@RequestParam("filePath") MultipartFile[] file) {	       	               
        
		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification newNoti = currentNoti.get();
		newNoti.setContent(content);
		newNoti.setLastModifed(LocalDate.now());
		newNoti.setTitle(title);		
		notifyRepository.save(newNoti);
		if(file.length != 0)
		{
			deleteOldFilesNoti(newNoti);
		 	List<String> fileNames = new ArrayList<>();	
			Path path = Paths.get("fileUploads/");
			for (MultipartFile files : file)
			{
				String originalFilename = files.getOriginalFilename();
				  if (originalFilename == null || originalFilename.isEmpty()) {				        
					  return "redirect:/Class/enterClass/" + newNoti.getClasses().getClassId();
				    }
			    int lastDotIndex = originalFilename.lastIndexOf('.');
			    if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
			    	return "redirect:/Class/enterClass/" + newNoti.getClasses().getClassId();
			    	//return "/Authen/Login"; // Hoặc bất kỳ giá trị mặc định nào phù hợp
			    }
			    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
			    String fileExtension = originalFilename.substring(lastDotIndex + 1);
			    String uniqueFileName = newNoti.getNotifyId() + "_notify_" + fileNameWithoutExtension + "." + fileExtension;

			    
			    try {	        
			    	InputStream inputStream = files.getInputStream();
			        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
			        fileNames.add(uniqueFileName.toLowerCase());       
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			newNoti.setFilePath(fileNames);
			notifyRepository.save(newNoti);
		}

		
    	return "redirect:/Class/enterClass/" + newNoti.getClasses().getClassId();
    }
	private void deleteOldFilesNoti(Notification noti) {
	    Path path = Paths.get("fileUploads/");
	    for (String fileName : noti.getFilePath()) {
	        try {
	            Files.deleteIfExists(path.resolve(fileName));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	
	@GetMapping("/deleteNoti/{notifyId}")
    public String deleteNotification(@PathVariable("notifyId") long id) {	       	               
        
		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification deletedNoti = currentNoti.get();
		deletedNoti.setDeleted(true);	
		deleteOldFilesNoti(deletedNoti);
		notifyRepository.save(deletedNoti);
    	return "redirect:/Class/enterClass/" + deletedNoti.getClasses().getClassId();
    }
	
	
	
	
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
