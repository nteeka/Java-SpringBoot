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
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.SubmitHomeworkRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Homework")
public class HomeworkController {
	
	@Autowired
	HomeworkRepository homeworkRepository;
	
	@Autowired
	SubmitHomeworkRepository submitHomeworkRepository;
	
		
	
	
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
	        return "redirect:/Teacher/enterClass/" + classes.getClassId();
		}
		homeWork.setDeadline(deadline);
		homeWork.setDateCreated(LocalDateTime.now());
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
		return "redirect:/Teacher/enterClass/" + classes.getClassId();
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
//		        return "redirect:/Teacher/enterClass/" + newHw.getClasses().getClassId();
//			}	
//		
		
		newHw.setHomeworkName(name);
		newHw.setLastModified(LocalDateTime.now());
		newHw.setDescription(description);
		newHw.setDeadline(deadline);
		
		if(multipartFile.length != 0)
		{
			deleteOldFiles(newHw);
			List<String> fileNames = new ArrayList<>();	
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
			
			if(hw.get().getDeadline() != null)
				if(hw.get().getDeadline().isAfter(LocalDate.now()))
					submit.setStatus(true);
				else
					submit.setStatus(false);
			
			
			submit.setDateSubmited(LocalDate.now());
			
			
			submitHomeworkRepository.save(submit);
			
			List<String> fileNames = new ArrayList<>();
			
			Path path = Paths.get("fileUploads/");
			for (MultipartFile files : multipartFile)
			{
				String originalFilename = files.getOriginalFilename();
			    int lastDotIndex = originalFilename.lastIndexOf('.');
			    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
			    String fileExtension = originalFilename.substring(lastDotIndex + 1);
			    String uniqueFileName = submit.getSubmitHomeworkId() + "_submitHomework_" + fileNameWithoutExtension + "." + fileExtension;

			    
			    try {	        
			    	InputStream inputStream = files.getInputStream();
			        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
			        fileNames.add(uniqueFileName.toLowerCase());       
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			submit.setFilePath(fileNames);
			submitHomeworkRepository.save(submit);
	        return "redirect:/StudentView/listStudent";
	    }
		
		@GetMapping("/listMemberSubmited/{homeworkId}")
		public String listClassView(@PathVariable long homeworkId,Model m,HttpServletRequest request) {	
			HttpSession session = request.getSession();
		    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		    if (loggedInUser == null) {
		        return "/Authen/Login";
		    }
		    List<SubmitHomework> listSubmited = submitHomeworkRepository.listSubmitHomeworkByHomeworkId(homeworkId);
		    m.addAttribute("listSubmited",listSubmited);
		    return "/Homework/ListMemberSubmited";
		}
		
		
		
}
