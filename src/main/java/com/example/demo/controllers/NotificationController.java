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
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.FileAttach;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
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
	AccountRepository accountRepository;
	
	@Autowired
	ClassRepository classRepository;

	@Autowired
	ClassAccountRepository classAccountRepository;

	@Autowired
	HomeworkRepository homeworkRepository;

	@Autowired
	NotificationRepository notifyRepository;

	@Autowired
	SubmitHomeworkRepository submitHomeworkRepository;

	@Autowired
	private FileAttachRepository fileAttachRepository;

	private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dccmckgvc", "api_key",
			"891328625785465", "api_secret", "szFBRogObiQHosinNgfK9pA1W0I"));

	@GetMapping("/listNoti/{classId}")
	public String listNotification(@PathVariable("classId") String id, Model m,HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		//img header
		m.addAttribute("account", loggedInUser);

		List<Notification> listNoti = notifyRepository.findByClassId(id);
		m.addAttribute("listNoti", listNoti);
		m.addAttribute("classInfo", listNoti.get(0).getClasses());
		return "/Notification/Noti-List";
	}

	@PostMapping("/createNotification")
	public String createNotification(@RequestParam("classes") Classes classes, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("filePath") MultipartFile[] multipartFile,
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
		notify.setDateCreated(LocalDateTime.now());
		notifyRepository.save(notify);

		for (MultipartFile file : multipartFile) {
			FileAttach fileAttach = new FileAttach();
			fileAttach.setHomework(null);
			fileAttach.setNotify(notify);
			fileAttach.setSubmitHomework(null);
			String originalFilename = file.getOriginalFilename();

			Map params = ObjectUtils.asMap("public_id", notify.getNotifyId() + "_Notify_" + originalFilename, // Specify
																												// //
																												// public_id
																												// //
																												// Cloudinary
					"resource_type", "auto", // Treat uploaded files as raw data
					"folder", "notification");
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

		List<Long> listIdFile = fileAttachRepository.findIdByNotifyId(notify.getNotifyId());
		notify.setFilePath(listIdFile);

		notifyRepository.save(notify);
		return "redirect:/Class/enterClass/" + notify.getClasses().getClassId();
	}

	@GetMapping("/editNoti/{notifyId}")
	public String editHomeworkView(@PathVariable long notifyId, Model m, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
//		if (loggedInUser == null) {
//			return "/Authen/Login";
//		}
		// ảnh user login trên header
		m.addAttribute("account", loggedInUser);

		Optional<Notification> noti = notifyRepository.findById(notifyId);
		m.addAttribute("noti", noti.get());
		
		List<FileAttach> listFile = fileAttachRepository.findByNotifyId(notifyId);
		m.addAttribute("listFile", listFile);
		
		return "/Notification/Noti-Edit";
	}
	
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

	@PostMapping("/updateNoti")
	public String updateNotification(@RequestParam("notifyId") long id, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("filePath") MultipartFile[] multipartFile) {

		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification newNoti = currentNoti.get();
		newNoti.setContent(content);
		newNoti.setLastModifed(LocalDateTime.now());
		newNoti.setTitle(title);

		List<FileAttach> fileAttachs = fileAttachRepository.findByNotifyId(newNoti.getNotifyId());

		if (multipartFile.length != 0) {
			// xóa file trên cloud
			for (FileAttach fileAttach : fileAttachs) {
				deleteFile(fileAttach.getFilePath());
				fileAttachRepository.delete(fileAttach);
			}

			for (MultipartFile file : multipartFile) {
				FileAttach fileAttach = new FileAttach();
				fileAttach.setSubmitHomework(null);
				fileAttach.setNotify(newNoti);
				fileAttach.setHomework(null);
				String originalFilename = file.getOriginalFilename();

				Map params = ObjectUtils.asMap("public_id", newNoti.getNotifyId() + "_Notify_" + originalFilename, // Specify																								// Cloudinary
						"resource_type", "auto", // Treat uploaded files as raw data
						"folder", "notification");
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
			List<Long> listIdFile = fileAttachRepository.findIdBySubmitId(newNoti.getNotifyId());
			newNoti.setFilePath(listIdFile);

		}
		notifyRepository.save(newNoti);

		return "redirect:/Class/enterClass/" + newNoti.getClasses().getClassId();
	}

	@GetMapping("/deleteNoti/{notifyId}")
    public String deleteNotification(@PathVariable("notifyId") long id) {	       	               
        
		Optional<Notification> currentNoti = notifyRepository.findById(id);
		Notification deletedNoti = currentNoti.get();
		deletedNoti.setDeleted(true);	
		notifyRepository.save(deletedNoti);
    	return "redirect:/Class/enterClass/" + deletedNoti.getClasses().getClassId();
    }

	@GetMapping("/detailNoti/{notifyId}")
	public String showNotiDetail(@PathVariable long notifyId, HttpServletRequest request, Model m) {

		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		// ảnh header
		Optional<Account> accountImg = accountRepository.findById(loggedInUser.getAccountId());
		m.addAttribute("account", accountImg.get());

		Optional<Notification> notiDetail = notiRepository.findById(notifyId);
		Notification currentNoti;

		currentNoti = notiDetail.get();
		m.addAttribute("noti", currentNoti);

		// get class id
		Optional<Classes> c = classRepository.findById(currentNoti.getClasses().getClassId());
		m.addAttribute("id", c.get().getClassId());
		m.addAttribute("c", c.get());
		// get class list which is already joined
		List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		List<Classes> classes = new ArrayList<Classes>();
		for (ClassAccount classes2 : account) {
			if (classes2.getClasses().getClassId() != currentNoti.getClasses().getClassId())
				classes.add(classes2.getClasses());
		}
		m.addAttribute("classes", classes);

		List<Homework> hw = homeworkRepository.findByClassId(currentNoti.getClasses().getClassId());
		// findByClassId

		m.addAttribute("hw", hw);

		// get all account on this class
		List<ClassAccount> acc = classAccountRepository.findByClassId(currentNoti.getClasses().getClassId());
		List<Account> listAccount = new ArrayList<Account>();
		for (ClassAccount lstAcc : acc) {
			// phân biệt người tạo ra lớp với người tham gia
			if (lstAcc.getAccount().getAccountId() != c.get().getAccount().getAccountId())
				listAccount.add(lstAcc.getAccount());
		}
		m.addAttribute("listAccount", listAccount);

		List<Notification> notifies = notifyRepository.findByClassId(c.get().getClassId());
		m.addAttribute("notifies", notifies);

		// check done or not done
		List<Long> listSubmit = submitHomeworkRepository.listHomeworkByAccountId(loggedInUser.getAccountId());
		m.addAttribute("listSubmit", listSubmit);

		List<Comment> listComment = commentRepository.findAllNotDeleted();
		m.addAttribute("listComment", listComment);

		List<ReplyComment> listReply = replyRepository.findAll();
		m.addAttribute("listReply", listReply);

		List<CommentLike> checkLikeComment = commentLikeRepository.findByAccountId(loggedInUser.getAccountId());
		List<Long> check = new ArrayList<Long>();
		for (CommentLike commentLike : checkLikeComment) {
			for (Comment cmt22 : listComment) {
				if (commentLike.getComment().getCommentId() == cmt22.getCommentId())
					check.add(cmt22.getCommentId());
			}
		}
		m.addAttribute("checkLikeComment", check);
		
		//danh sách file
		List<FileAttach> listFile = fileAttachRepository.findByNotifyId(notifyId);
		m.addAttribute("listFile", listFile);
		

		return "/Notification/Noti-Detail";

	}
}
