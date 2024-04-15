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
import com.example.demo.models.FileAttach;
import com.example.demo.models.Homework;
import com.example.demo.models.SubmitHomework;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.FileAttachRepository;
import com.example.demo.repositories.HomeworkRepository;
import com.example.demo.repositories.SubmitHomeworkRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.cloudinary.*;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/Homework")
public class HomeworkController {

	@Autowired
	HomeworkRepository homeworkRepository;

	@Autowired
	SubmitHomeworkRepository submitHomeworkRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FileAttachRepository fileAttachRepository;

	@PostMapping("/createHomework")
	public String createHomework(@RequestParam("homeworkName") String homeworkName,
			@RequestParam("classes") Classes classes, @RequestParam("description") String description,

			@RequestParam(value = "deadline", required = false) LocalDate deadline,
			@RequestParam("filePath") MultipartFile[] multipartFile, Model m, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		
		Homework homeWork = new Homework();
		homeWork.setClasses(classes);
		homeWork.setHomeworkName(homeworkName);
		homeWork.setDescription(description);

		if (deadline != null && !deadline.isAfter(LocalDate.now())) {
			redirectAttributes.addFlashAttribute("errorDeadline", "Dealine is invalid");
			return "redirect:/Class/enterClass/" + classes.getClassId();
		}
		homeWork.setDeadline(deadline);
		homeWork.setDateCreated(LocalDateTime.now());
		homeworkRepository.save(homeWork);

		for (MultipartFile file : multipartFile) {
			FileAttach fileAttach = new FileAttach();
			fileAttach.setHomework(homeWork);
			fileAttach.setNotify(null);
			fileAttach.setSubmitHomework(null);
			String originalFilename = file.getOriginalFilename();

			Map params = ObjectUtils.asMap("public_id", homeWork.getHomeworkId() + "_Homework_" + originalFilename, // Specify																							// Cloudinary
					"resource_type", "auto", // Treat uploaded files as raw data
					"folder", "homework");
			try {
				// Upload to Cloudinary using byte array
				byte[] fileBytes = file.getBytes();
				Map result = cloudinary.uploader().upload(fileBytes, params);
				String cloudinaryUrl = (String) result.get("url").toString();
				fileAttach.setFilePath(cloudinaryUrl);
				fileAttachRepository.save(fileAttach);
			} catch (IOException exception) {
				System.out.println("Error uploading file: " + exception.getMessage());
			}
		}

		List<Long> listIdFile = fileAttachRepository.findIdByHomeworkId(homeWork.getHomeworkId());
		homeWork.setFilePath(listIdFile);
		homeworkRepository.save(homeWork);
		return "redirect:/Class/enterClass/" + classes.getClassId();
	}

	@PostMapping("/updateHomework")
	public String updateHomework(@RequestParam("homeworkId") long id, @RequestParam("homeworkName") String name,
			@RequestParam("description") String description, @RequestParam("deadline") LocalDate deadline,
			@RequestParam("filePath") MultipartFile[] multipartFile, RedirectAttributes redirectAttributes) {

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

		List<FileAttach> fileAttachs = fileAttachRepository.findByHomeworkId(newHw.getHomeworkId());

		if (multipartFile.length != 0) {
			// xóa file trên cloud
			for (FileAttach fileAttach : fileAttachs) {
				deleteFile(fileAttach.getFilePath());
				fileAttachRepository.delete(fileAttach);
			}

			for (MultipartFile file : multipartFile) {
				FileAttach fileAttach = new FileAttach();
				fileAttach.setSubmitHomework(null);
				fileAttach.setNotify(null);
				fileAttach.setHomework(newHw);
				String originalFilename = file.getOriginalFilename();

				Map params = ObjectUtils.asMap("public_id", newHw.getHomeworkId() + "_Homework_" + originalFilename, // Specify
																														// public_id
																														// for
																														// Cloudinary
						"resource_type", "auto", // Treat uploaded files as raw data
						"folder", "homework");
				try {
					// Upload to Cloudinary using byte array
					byte[] fileBytes = file.getBytes();
					Map result = cloudinary.uploader().upload(fileBytes, params);
					String cloudinaryUrl = (String) result.get("url").toString();
					fileAttach.setFilePath(cloudinaryUrl);
					fileAttachRepository.save(fileAttach);
				} catch (IOException exception) {
					System.out.println("Error uploading file: " + exception.getMessage());
				}
			}
			List<Long> listIdFile = fileAttachRepository.findIdBySubmitId(newHw.getHomeworkId());
			newHw.setFilePath(listIdFile);

		}

		homeworkRepository.save(newHw);
		return "redirect:/Class/enterClass/" + newHw.getClasses().getClassId();
	}

	@GetMapping("/submitHomeWorkView/{homeworkId}")
	public String submitHomeWorkView(@PathVariable long homeworkId, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "/Authen/Login";
		}

		Optional<Homework> homework = homeworkRepository.findById(homeworkId);
		// Kiểm tra deadline
//		if (homework.get().getDeadline().isBefore(LocalDate.now())) {
//		    model.addAttribute("lateDeadline", "You have your chance, but time is over! You cannot submit anymore.");
//			return enterClassView(homework.get().getClasses().getClassId(),model,request);
//		}

		model.addAttribute("homework", homework.get());

		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
		model.addAttribute("account", acc.get());

		return "/Homework/submitHomework";

	}

	private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dccmckgvc", "api_key",
			"891328625785465", "api_secret", "szFBRogObiQHosinNgfK9pA1W0I"));

	@PostMapping("/submitHomeWork")
	public String submitHomework(@RequestParam("homeworkId") Homework homework,
			@RequestParam("description") String description, @RequestParam("filePath") MultipartFile[] multipartFile,
			Model m, HttpServletRequest request) {

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

		for (MultipartFile file : multipartFile) {
			FileAttach fileAttach = new FileAttach();
			fileAttach.setHomework(null);
			fileAttach.setNotify(null);
			fileAttach.setSubmitHomework(submit);
			String originalFilename = file.getOriginalFilename();

			Map params = ObjectUtils.asMap("public_id",
					submit.getSubmitHomeworkId() + "_submitHomework_" + originalFilename, // Specify public_id for
																							// Cloudinary
					"resource_type", "auto", // Treat uploaded files as raw data
					"folder", "submitedhomework");
			try {
				// Upload to Cloudinary using byte array
				byte[] fileBytes = file.getBytes();
				Map result = cloudinary.uploader().upload(fileBytes, params);
				String cloudinaryUrl = (String) result.get("url").toString();
				fileAttach.setFilePath(cloudinaryUrl);
				fileAttachRepository.save(fileAttach);
			} catch (IOException exception) {
				System.out.println("Error uploading file: " + exception.getMessage());
			}
		}

		List<Long> listIdFile = fileAttachRepository.findIdBySubmitId(submit.getSubmitHomeworkId());
		submit.setFilePath(listIdFile);

		submitHomeworkRepository.save(submit);

		return "redirect:/Class/enterClass/" + homework.getClasses().getClassId();
	}

	// xóa file trên cloud
	public static void deleteFile(String filePath) {
			filePath = extractPublicId(filePath);
			try {
				cloudinary.uploader().destroy((filePath), ObjectUtils.asMap("type", "upload", "resource_type", "raw"));
				System.out.println("Delete Success");
			} catch (IOException exception) {
				System.out.println("Error deleting file: " + exception.getMessage());
			}
		}

	// return public_id của link file
	private static String extractPublicId(String fileUrl) {
		// Định dạng của đường dẫn URL trên Cloudinary:
		// http://res.cloudinary.com/<cloud_name>/<resource_type>/<upload_type>/<version>/<public_id>.<format>
		// Ví dụ:
		// http://res.cloudinary.com/dccmckgvc/raw/upload/v1713149910/homework/32_submitHomework_homework.txt
		// Chúng ta cần lấy public_id từ đây

		// Tìm vị trí của chuỗi "upload/" trong đường dẫn URL
		int uploadIndex = fileUrl.indexOf("upload/");
		if (uploadIndex == -1) {
			// Không tìm thấy "upload/", trả về null hoặc thực hiện xử lý phù hợp với trường
			// hợp này
			return null;
		}

		// Lấy phần dư sau chuỗi "upload/"
		String publicIdPart = fileUrl.substring(uploadIndex + "upload/".length());

		// Xóa phần "v1713155326/" nếu có
		publicIdPart = publicIdPart.replaceFirst("^v[0-9]+/", "");

		// Trả về public_id
		System.out.println(publicIdPart);
		return publicIdPart.trim();
	}

	@PostMapping("/updateSubmitHomework")
	public String updateSubmitHomework(@RequestParam("submitHomeworkId") long id,
			@RequestParam("description") String description, @RequestParam("filePath") MultipartFile[] multipartFile,
			RedirectAttributes redirectAttributes) {

		Optional<SubmitHomework> currentSubmitHomework = submitHomeworkRepository.findById(id);
		SubmitHomework newSubmit = currentSubmitHomework.get();

		List<FileAttach> fileAttachs = fileAttachRepository.findBySubmitId(newSubmit.getSubmitHomeworkId());

		newSubmit.setLastModified(LocalDateTime.now());
		newSubmit.setDescription(description);

		if (multipartFile.length != 0) {
			// deleteOldFiles(newSubmit);

			// xóa file trên cloud
			for (FileAttach fileAttach : fileAttachs) {
				deleteFile(fileAttach.getFilePath());
				fileAttachRepository.delete(fileAttach);
			}

//				List<String> fileNames = new ArrayList<>();	
//				Path path = Paths.get("fileUploads/");
//				for (MultipartFile files : multipartFile)
//				{
//					String originalFilename = files.getOriginalFilename();
//					if (originalFilename == null || originalFilename.isEmpty()) {
//						submitHomeworkRepository.save(newSubmit);
//						  return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
//					    }
//				    int lastDotIndex = originalFilename.lastIndexOf('.');
//				    if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
//				    	submitHomeworkRepository.save(newSubmit);
//				    	return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
//				    }
//				    String fileNameWithoutExtension = originalFilename.substring(0, lastDotIndex); // Loại bỏ phần mở rộng nếu có
//				    String fileExtension = originalFilename.substring(lastDotIndex + 1);
//				    String uniqueFileName = newSubmit.getSubmitHomeworkId() + "_submitHomework_" + fileNameWithoutExtension + "." + fileExtension;
//
//				    
//				    try {	        
//				    	InputStream inputStream = files.getInputStream();
//				        Files.copy(inputStream, path.resolve(uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
//				        fileNames.add(uniqueFileName.toLowerCase());       
//				    } catch (IOException e) {
//				        e.printStackTrace();
//				    }
//				}
			for (MultipartFile file : multipartFile) {
				FileAttach fileAttach = new FileAttach();
				fileAttach.setHomework(null);
				fileAttach.setNotify(null);
				fileAttach.setSubmitHomework(newSubmit);
				String originalFilename = file.getOriginalFilename();

				Map params = ObjectUtils.asMap("public_id",
						newSubmit.getSubmitHomeworkId() + "_submitHomework_" + originalFilename, // Specify public_id
																									// for Cloudinary
						"resource_type", "auto", // Treat uploaded files as raw data
						"folder", "submitedhomework");
				try {
					// Upload to Cloudinary using byte array
					byte[] fileBytes = file.getBytes();
					Map result = cloudinary.uploader().upload(fileBytes, params);
					String cloudinaryUrl = (String) result.get("url").toString();
					fileAttach.setFilePath(cloudinaryUrl);
					fileAttachRepository.save(fileAttach);
				} catch (IOException exception) {
					System.out.println("Error uploading file: " + exception.getMessage());
				}
			}
			List<Long> listIdFile = fileAttachRepository.findIdBySubmitId(newSubmit.getSubmitHomeworkId());
			newSubmit.setFilePath(listIdFile);
		}

		submitHomeworkRepository.save(newSubmit);
		return "redirect:/Class/enterClass/" + newSubmit.getHomework().getClasses().getClassId();
	}

	@GetMapping("/listMemberSubmited/{homeworkId}")
	public String listClassView(@PathVariable long homeworkId, Model m, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
		m.addAttribute("account", acc.get());
		List<SubmitHomework> listSubmited = submitHomeworkRepository.listSubmitHomeworkByHomeworkId(homeworkId);
		m.addAttribute("listSubmited", listSubmited);

		List<FileAttach> listFile = fileAttachRepository.findAll();
		m.addAttribute("listFile", listFile);
		for (SubmitHomework submitHomework : listSubmited) {
			submitHomework.getHomework().getDeadline().isBefore(LocalDate.now());
		}

		return "/Homework/ListMemberSubmited";
	}

	@GetMapping("/detailHomework/{homeworkId}")
	public String detailHomework(@PathVariable long homeworkId, Model m, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		// ảnh user login trên header
		m.addAttribute("account", loggedInUser);

		Optional<Homework> homework = homeworkRepository.findById(homeworkId);
		m.addAttribute("homework", homework.get());

		List<SubmitHomework> listSubmit = submitHomeworkRepository.listSubmitHomeworkByHomeworkId(homeworkId);
		m.addAttribute("listSubmit", listSubmit);
		return "/Homework/Homework-Detail";
	}

	@GetMapping("/deleteHomework/{homeworkId}")
	public String deleteHomework(@PathVariable("homeworkId") long id) {

		Optional<Homework> currentHW = homeworkRepository.findById(id);
		Homework deletedHW = currentHW.get();
		deletedHW.setDeleted(true);
		homeworkRepository.save(deletedHW);
		return "redirect:/Class/enterClass/" + deletedHW.getClasses().getClassId();
	}
	
	

}
