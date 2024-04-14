package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Homework;
import com.example.demo.models.SubmitHomework;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.SubmitHomeworkRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.cloudinary.*;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;

@Controller
@RequestMapping("/Homework")
public class HomeworkController {
	
	@Autowired
	HomeworkRepository homeworkRepository;
	
	@Autowired
	SubmitHomeworkRepository submitHomeworkRepository;
	
	@Autowired
    private AccountRepository accountRepository;	
	
	
	@PostMapping("/createHomework")
    public String createHomework(@RequestParam("homeworkName") String homeworkName,
    								@RequestParam("classes") Classes classes,
    								@RequestParam("description") String description,
    								
    								@RequestParam(value = "deadline", required = false) LocalDate deadline,
    								@RequestParam("filePath") MultipartFile[] multipartFile,
    								Model m,
    								RedirectAttributes redirectAttributes,
    								HttpServletRequest request) {
		Homework homeWork = new Homework();
		homeWork.setClasses(classes);
		homeWork.setHomeworkName(homeworkName);
		homeWork.setDescription(description);
		
		if(deadline != null && !deadline.isAfter(LocalDate.now()))
		{
	        redirectAttributes.addFlashAttribute("errorDeadline", "Dealine is invalid");
	        return "redirect:/Class/enterClass/" + classes.getClassId();
		}
		homeWork.setDeadline(deadline);
		homeWork.setDateCreated(LocalDateTime.now());
		homeworkRepository.save(homeWork);
		
		ArrayList<String> fileNames = new ArrayList<>();
		
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
		return "redirect:/Class/enterClass/" + classes.getClassId();
    }
	
	@PostMapping("/updateHomework")
    public String updateHomework(@RequestParam("homeworkId") long id,
    							@RequestParam("homeworkName") String name,
    							@RequestParam("description") String description,   
    							@RequestParam("deadline") LocalDate deadline,  
    							@RequestParam("filePath") MultipartFile[] multipartFile,
    							RedirectAttributes redirectAttributes) {	       	               
        
		Optional<Homework> currentHomework = homeworkRepository.findById(id);
		Homework newHw = currentHomework.get();
		
//		if(deadline != newHw.getDateCreated())		
//			if(deadline != null && !deadline.isAfter(LocalDate.now()))
//			{
//		        redirectAttributes.addFlashAttribute("errorDeadlineUpdate", "Dealine is invalid");
//		        return "redirect:/Class/enterClass/" + newHw.getClasses().getClassId();
//			}	
//		
		
		newHw.setHomeworkName(name);
		newHw.setLastModified(LocalDateTime.now());
		newHw.setDescription(description);
		newHw.setDeadline(deadline);
		
		if(multipartFile.length != 0)
		{
			deleteOldFiles(newHw);
			ArrayList<String> fileNames = new ArrayList<>();	
			Path path = Paths.get("fileUploads/");
			for (MultipartFile files : multipartFile)
			{
				String originalFilename = files.getOriginalFilename();
				if (originalFilename == null || originalFilename.isEmpty()) {
					homeworkRepository.save(newHw);
					  return "redirect:/Class/enterClass/" + newHw.getClasses().getClassId();
				    }
			    int lastDotIndex = originalFilename.lastIndexOf('.');
			    if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
			    	homeworkRepository.save(newHw);
			    	return "redirect:/Class/enterClass/" + newHw.getClasses().getClassId();
			    }
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
		}
		
		homeworkRepository.save(newHw);
    	return "redirect:/Class/enterClass/" + newHw.getClasses().getClassId();
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
		
		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
		model.addAttribute("account",acc.get());
		
		return "/Homework/submitHomework";
      
	}
	
//		@PostMapping("/submitHomeWork")
//	    public String submitHomework(@RequestParam("homeworkId") Homework homework,
//	    								@RequestParam("description") String description,	    								
//	    								@RequestParam("filePath") MultipartFile[] multipartFile,
//	    								Model m,
//	    								HttpServletRequest request) {					
//			HttpSession session = request.getSession();
//		    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
//
//		    if (loggedInUser == null) {
//		        return "/Authen/Login";
//		    }
//		    
//			SubmitHomework submit = new SubmitHomework();
//			submit.setAccount(loggedInUser);
//			submit.setHomework(homework);
//			submit.setDescription(description);
//			
//			Optional<Homework> hw = homeworkRepository.findById(homework.getHomeworkId());
//			
//			if(hw.get().getDeadline() != null)
//				if(hw.get().getDeadline().isAfter(LocalDate.now()))
//					submit.setStatus(true);
//				else
//					submit.setStatus(false);
//			
//			
//			submit.setDateSubmited(LocalDateTime.now());						
//			submitHomeworkRepository.save(submit);
//			
//			List<String> fileNames = new ArrayList<>();
//			
//			Path path = Paths.get("fileUploads/");
//			for (MultipartFile files : multipartFile)
//			{
//				String originalFilename = files.getOriginalFilename();
//			    int lastDotIndex = originalFilename.lastIndexOf('.');
//			    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
//			    String fileExtension = originalFilename.substring(lastDotIndex + 1);
//			    String uniqueFileName = submit.getSubmitHomeworkId() + "_submitHomework_" + fileNameWithoutExtension + "." + fileExtension;
//
//			    
//			    try {	        
//			    	InputStream inputStream = files.getInputStream();
//			        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
//			        fileNames.add(uniqueFileName.toLowerCase());       
//			    } catch (IOException e) {
//			        e.printStackTrace();
//			    }
//			}
//			submit.setFilePath(fileNames);
//
//			submitHomeworkRepository.save(submit);
//	        return "redirect:/Class/enterClass/" + homework.getClasses().getClassId() ;
//	    }
	@PostMapping("/submitHomeWork")
	public String submitHomework(@RequestParam("homeworkId") Homework homework,
	                             @RequestParam("description") String description,
	                             @RequestParam("filePath") MultipartFile[] multipartFile,
	                             Model m,
	                             HttpServletRequest request) {

	    HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");

	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }

	    SubmitHomework submit = new SubmitHomework();
	    submit.setAccount(loggedInUser);
	    submit.setHomework(homework);
	    submit.setDescription(description);

	    Optional<Homework> hw = homeworkRepository.findById(homework.getHomeworkId());

	    if (hw.get().getDeadline() != null) {
	        if (hw.get().getDeadline().isAfter(LocalDate.now())) {
	            submit.setStatus(true);
	        } else {
	            submit.setStatus(false);
	        }
	    }

	    submit.setDateSubmited(LocalDateTime.now());
	    submitHomeworkRepository.save(submit);

	    List<String> cloudinaryUrls = new ArrayList<>();

	    // Cloudinary configuration (replace with your credentials)
	    String cloudName = "dccmckgvc";
	    String apiKey = "891328625785465";
	    String apiSecret = "szFBRogObiQHosinNgfK9pA1W0I";
	    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
	            "cloud_name", cloudName,
	            "api_key", apiKey,
	            "api_secret", apiSecret));

	    for (MultipartFile file : multipartFile) {
	        String originalFilename = file.getOriginalFilename();

	        Map params = ObjectUtils.asMap(
	                "public_id", submit.getSubmitHomeworkId() + "_submitHomework_" + originalFilename, // Specify public_id for Cloudinary
	                "resource_type", "auto", // Treat uploaded files as raw data
	                "folder", "homework"
	        );

	        try {
	            // Upload to Cloudinary using byte array
	            byte[] fileBytes = file.getBytes();
	            Map result = cloudinary.uploader().upload(fileBytes, params);
	            String cloudinaryUrl = (String) result.get("url");
	            cloudinaryUrls.add(cloudinaryUrl);
	        } catch (IOException exception) {
	            System.out.println("Error uploading file: " + exception.getMessage());
	            // Handle upload exceptions (log or inform user)
	        }
	    }

	    submit.setFilePath(cloudinaryUrls); // Store Cloudinary URLs in filePath

	    submitHomeworkRepository.save(submit);

	    return "redirect:/Class/enterClass/" + homework.getClasses().getClassId();
	}

		
	

		@PostMapping("/updateSubmitHomework")
	    public String updateSubmitHomework(@RequestParam("submitHomeworkId") long id,
	    							@RequestParam("description") String description,   
	    							@RequestParam("filePath") MultipartFile[] multipartFile,
	    							RedirectAttributes redirectAttributes) {	       	               
	        
			Optional<SubmitHomework> currentSubmitHomework = submitHomeworkRepository.findById(id);
			SubmitHomework newSubmit = currentSubmitHomework.get();
			

			
			newSubmit.setLastModified(LocalDateTime.now());
			newSubmit.setDescription(description);

			if(multipartFile.length != 0)
			{
				//deleteOldFiles(newSubmit);
				List<String> fileNames = new ArrayList<>();	
				Path path = Paths.get("fileUploads/");
				for (MultipartFile files : multipartFile)
				{
					String originalFilename = files.getOriginalFilename();
					if (originalFilename == null || originalFilename.isEmpty()) {
						submitHomeworkRepository.save(newSubmit);
						  return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
					    }
				    int lastDotIndex = originalFilename.lastIndexOf('.');
				    if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
				    	submitHomeworkRepository.save(newSubmit);
				    	return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
				    }
				    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
				    String fileExtension = originalFilename.substring(lastDotIndex + 1);
				    String uniqueFileName = newSubmit.getSubmitHomeworkId() + "_submitHomework_" + fileNameWithoutExtension + "." + fileExtension;

				    
				    try {	        
				    	InputStream inputStream = files.getInputStream();
				        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
				        fileNames.add(uniqueFileName.toLowerCase());       
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
				newSubmit.setFilePath(fileNames);
			}
			
			submitHomeworkRepository.save(newSubmit);
	    	return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
	    }
		
		
		
		
		
		@GetMapping("/listMemberSubmited/{homeworkId}")
		public String listClassView(@PathVariable long homeworkId,Model m,HttpServletRequest request) {	
			HttpSession session = request.getSession();
		    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		    if (loggedInUser == null) {
		        return "/Authen/Login";
		    }
		    Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
	    	m.addAttribute("account",acc.get());
		    List<SubmitHomework> listSubmited = submitHomeworkRepository.listSubmitHomeworkByHomeworkId(homeworkId);
		    m.addAttribute("listSubmited",listSubmited);
		    for (SubmitHomework submitHomework : listSubmited) {
		    	submitHomework.getHomework().getDeadline().isBefore(LocalDate.now());
			}
		    
		    return "/Homework/ListMemberSubmited";
		}
		
		@GetMapping("/detailHomework/{homeworkId}")
		public String detailHomework(@PathVariable long homeworkId,Model m,HttpServletRequest request) {	
			HttpSession session = request.getSession();
		    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		    if (loggedInUser == null) {
		        return "/Authen/Login";
		    }
		    Optional<Homework> homework = homeworkRepository.findById(homeworkId);
		    m.addAttribute("homework",homework.get());
		    
		    List<SubmitHomework> listSubmit = submitHomeworkRepository.listSubmitHomeworkByHomeworkId(homeworkId);
		    m.addAttribute("listSubmit",listSubmit);
		    return "/Homework/Homework-Detail";
		}
		
		
		
}
