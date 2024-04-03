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
	
	
	//missing validate
	
	@PostMapping("/createHomework")
    public String createHomework(@RequestParam("homeworkName") String homeworkName,
    								@RequestParam("classes") Classes classes,
    								@RequestParam("description") String description,
    								
    								@RequestParam(value = "deadline", required = false) LocalDate deadline,
    								@RequestParam("filePath") MultipartFile[] multipartFile,
    								Model m,
    								HttpServletRequest request) {
		Homework homeWork = new Homework();
		homeWork.setClasses(classes);
		homeWork.setHomeworkName(homeworkName);
		homeWork.setDescription(description);
		
		if(deadline != null && !deadline.isAfter(LocalDate.now()))
		{
			m.addAttribute("errorDeadline", "Dealine is invalid");
			return enterClassView(classes.getClassId(),m,request);
		}
		homeWork.setDeadline(deadline);
		homeWork.setDateCreated(LocalDate.now());
		homeworkRepository.save(homeWork);
		
		List<String> fileNames = new ArrayList<>();
		
		Path path = Paths.get("fileUploads/");
		for (MultipartFile files : multipartFile)
		{
			String originalFilename = files.getOriginalFilename();
		    int lastDotIndex = originalFilename.lastIndexOf('.');
		    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
		    String fileExtension = originalFilename.substring(lastDotIndex + 1);
		    String uniqueFileName = homeWork.getHomeworkId() + "_homework_" + fileNameWithoutExtension + "." + fileExtension;

		    
		    try {	        
		    	InputStream inputStream = files.getInputStream();
		        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
		        fileNames.add(uniqueFileName.toLowerCase());       
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		homeWork.setFilePath(fileNames);
		homeworkRepository.save(homeWork);
        return "redirect:/StudentView/listStudent";
    }
	
	@PostMapping("/updateHomework")
    public String updateHomework(@RequestParam("homeworkId") long id,
    							@RequestParam("homeworkName") String name,
    							@RequestParam("description") String description,   
    							@RequestParam("deadline") LocalDate deadline,  
    							@RequestParam("filePath") MultipartFile[] multipartFile) {	       	               
        
		Optional<Homework> currentHomework = homeworkRepository.findById(id);
		Homework newHw = currentHomework.get();
		newHw.setHomeworkName(name);
		newHw.setLastModified(LocalDate.now());
		newHw.setDescription(description);
		newHw.setDeadline(deadline);
		
		if(multipartFile.length == 0)
		{
			homeworkRepository.save(newHw);
			return "redirect:/Teacher/enterClass/" + newHw.getClasses().getClassId();
		}
		deleteOldFiles(newHw);
		List<String> fileNames = new ArrayList<>();	
		Path path = Paths.get("fileUploads/");
		for (MultipartFile files : multipartFile)
		{
			String originalFilename = files.getOriginalFilename();
		    int lastDotIndex = originalFilename.lastIndexOf('.');
		    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
		    String fileExtension = originalFilename.substring(lastDotIndex + 1);
		    String uniqueFileName = newHw.getHomeworkId() + "_homework_" + fileNameWithoutExtension + "." + fileExtension;

		    
		    try {	        
		    	InputStream inputStream = files.getInputStream();
		        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
		        fileNames.add(uniqueFileName.toLowerCase());       
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		newHw.setFilePath(fileNames);
		homeworkRepository.save(newHw);
    	return "redirect:/Teacher/enterClass/" + newHw.getClasses().getClassId();
    }
	
	private void deleteOldFiles(Homework homework) {
	    Path path = Paths.get("fileUploads/");
	    for (String fileName : homework.getFilePath()) {
	        try {
	            Files.deleteIfExists(path.resolve(fileName));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
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
	    
	    	    
	    return "/Classes/Class-Content";
	}
	
	
	
		
	
	
	@GetMapping("/deleteHomework/{homeworkId}")
    public String deleteHomework(@PathVariable("homeworkId") long id) {	       	               
        
		Optional<Homework> currentHW = homeworkRepository.findById(id);
		Homework deletedHW = currentHW.get();
		deletedHW.setDeleted(true);	
		homeworkRepository.save(deletedHW);
    	return "redirect:/Class/enterClass/" + deletedHW.getClasses().getClassId();
    }
	
	
	@GetMapping("/submitHomeWorkView/{homeworkId}")
	public String submitHomeWorkView(@PathVariable long homeworkId,Model model,HttpServletRequest request) {
		
		HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
		
		Optional<Homework> homework = homeworkRepository.findById(homeworkId);
		//Kiểm tra deadline
//		if (homework.get().getDeadline().isBefore(LocalDate.now())) {
//		    model.addAttribute("lateDeadline", "You have your chance, but time is over! You cannot submit anymore.");
//			return enterClassView(homework.get().getClasses().getClassId(),model,request);
//		}

		model.addAttribute("homework", homework.get());
		return "/Homework/submitHomework";
      
	}
	
}
