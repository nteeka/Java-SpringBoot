package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.example.demo.models.Account;
import com.example.demo.models.ClassAccount;
import com.example.demo.models.Classes;
import com.example.demo.models.Comment;
import com.example.demo.models.CommentLike;
import com.example.demo.models.FileAttach;
import com.example.demo.models.Homework;
import com.example.demo.models.Notification;
import com.example.demo.models.ReplyComment;
import com.example.demo.models.SubmitHomework;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Class")
public class ClassesController {

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

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FileAttachRepository fileAttachRepository;

	@GetMapping("/listClass")
	public String listClassView(Model m, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		// Kiểm tra xem tài khoản đã đăng nhập chưa
		if (loggedInUser == null) {
			// Xử lý khi tài khoản chưa đăng nhập
			session.setAttribute("redirectUrl", "/Class/listClass");
			return "/Authen/Login";
		}
		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());

		List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		if (account.isEmpty()) {
			// lấy ảnh user cho header
			m.addAttribute("account", acc.get());
			return "/Classes/Class-List_Empty";

		}
		List<Classes> classes = new ArrayList<Classes>();
		for (ClassAccount classes2 : account) {
			classes.add(classes2.getClasses());
		}
		// sẽ thấy được toàn bộ lớp mà người trong session login đã tạo
//	    List<Classes> c = classRepository.findByAccountId(loggedInUser.getAccountId());
//	    classes.addAll(c);
		m.addAttribute("classes", classes);
		// lấy ảnh user cho header
		m.addAttribute("account", acc.get());
		return "/Classes/Class-List";
	}

	@PostMapping("/addNewClass")
	public String createClass(@RequestParam("className") String name, @RequestParam("description") String desc,
			HttpServletRequest request) {
		Classes classes = new Classes();
		classes.setClassName(name);
		classes.setDescription(desc);
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null)
			classes.setAccount(null);
		classes.setAccount(loggedInUser);
		classRepository.save(classes);

		// người tạo đc add vào lớp
		ClassAccount classAccount = new ClassAccount();
		classAccount.setClasses(classes);
		classAccount.setAccount(loggedInUser);
		classAccount.setNum(1); // Set số lượng hoặc các thuộc tính khác nếu cần
		classAccountRepository.save(classAccount);

		return "redirect:/Class/listClass";

	}

	@PostMapping("/joinClass")
	public String joinClass(@RequestParam("classId") String id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		// return về List Class
		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
		List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		List<Classes> listClass = new ArrayList<Classes>();
		for (ClassAccount classes2 : account) {
			listClass.add(classes2.getClasses());
		}

		// check account have join class or not
		int checkAccountNotJoin = 0;
		List<ClassAccount> isAccountJoin = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		if (isAccountJoin.isEmpty())
			checkAccountNotJoin = 1;
		// Tìm lớp học dựa trên ID
		Optional<Classes> optionalClass = classRepository.findById(id);
		List<ClassAccount> optional = classAccountRepository.findByClassId(id);

		if (optionalClass.isPresent()) {
			Classes classes = optionalClass.get();

			// Kiểm tra xem tài khoản đã tham gia lớp học chưa
			for (ClassAccount classAccount : optional) {
				// Xử lý khi tài khoản đã tham gia lớp học
				if (classAccount.getAccount().getAccountId() == loggedInUser.getAccountId()) {

					model.addAttribute("error", "you already join class with ID: " + id);
					model.addAttribute("classes", classes);
					model.addAttribute("account", acc.get());
					return "/Classes/Class-List";
				}
			}

			// Tạo một đối tượng ClassAccount mới và lưu vào cơ sở dữ liệu
			ClassAccount classAccount = new ClassAccount();
			classAccount.setClasses(classes);
			classAccount.setAccount(loggedInUser);
			classAccount.setNum(1); // Set số lượng hoặc các thuộc tính khác nếu cần

			classAccountRepository.save(classAccount);

			// Xử lý khi tham gia lớp học thành công
			model.addAttribute("success", "Join class with ID: " + id + " successfull");
			return "redirect:/Class/listClass";
		} else {

			if (checkAccountNotJoin != 1) {
				model.addAttribute("error", "Class not found with ID: " + id);
				model.addAttribute("classes", listClass);
				model.addAttribute("account", acc.get());
				return "/Classes/Class-List";
			}
			model.addAttribute("account", acc.get());
			model.addAttribute("error", "Class not found with ID: " + id);
			return "/Classes/Class-List_Empty";
		}
	}

	@GetMapping("/leaveClass/{classId}")
	public String leaveClass(@PathVariable("classId") String id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "/Authen/Login";
		}
		ClassAccount deleteClassAccount = classAccountRepository.findByClassIdAndAccountId(id.trim(),
				loggedInUser.getAccountId());
		classAccountRepository.deleteById(deleteClassAccount.getClassAccountId());
		Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
		List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		List<Classes> listClass = new ArrayList<Classes>();
		for (ClassAccount classes2 : account) {
			listClass.add(classes2.getClasses());
		}
		if(listClass.isEmpty())
		{
			model.addAttribute("account", acc.get());
			return "/Classes/Class-List_Empty";
		}
			
		model.addAttribute("success", "Leave class with ID: " + id + " successfull");
		model.addAttribute("classes", listClass);
		model.addAttribute("account", acc.get());
		return "/Classes/Class-List";

	}

	@GetMapping("/enterClass/{classId}")
	public String enterClassView(@PathVariable("classId") String id, Model m, HttpServletRequest request) {



		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			session.setAttribute("redirectUrl", "/Class/enterClass/" + id);
			return "/Authen/Login";
		}
		// get class id
		Optional<Classes> c = classRepository.findById(id);
		m.addAttribute("id", c.get().getClassId());
		// get class
		m.addAttribute("c", c.get());
		
		// get class list which is loggedInUser already joined
		List<ClassAccount> account = classAccountRepository.findByAccountId(loggedInUser.getAccountId());
		List<Classes> classes = new ArrayList<Classes>();
		for (ClassAccount classes2 : account) {
			if (classes2.getClasses().getClassId() != id)
				classes.add(classes2.getClasses());
		}
		m.addAttribute("classes", classes);
		
		
		// all homework
		List<Homework> hw = homeworkRepository.findByClassId(id);
		m.addAttribute("hw", hw);

		// check hw done or not done
		List<Long> listSubmit = submitHomeworkRepository.listHomeworkByAccountId(loggedInUser.getAccountId());
		m.addAttribute("listSubmit", listSubmit);

		// get all account on this class
		List<ClassAccount> acc = classAccountRepository.findByClassId(id);
		List<Account> listAccount = new ArrayList<Account>();
		for (ClassAccount lstAcc : acc) {
			// phân biệt người tạo ra lớp với người tham gia
			if (lstAcc.getAccount().getAccountId() != c.get().getAccount().getAccountId())
				listAccount.add(lstAcc.getAccount());
		}
		m.addAttribute("listAccount", listAccount);
		
		
		List<Notification> listPinnedNoti = notifyRepository.listPinnedNotiByClassId(c.get().getClassId());
		if(!listPinnedNoti.isEmpty())			
			m.addAttribute("listPinnedNoti", listPinnedNoti);


		//count member in class
		Long countMember = classAccountRepository.countAccountsInClass(id);
		//trừ đi người tạo lớp
		countMember = countMember - 1 ;
		m.addAttribute("countMember", countMember);

		
		//img header account
		Optional<Account> accountImg = accountRepository.findById(loggedInUser.getAccountId());
		m.addAttribute("account", accountImg.get());


		return "/Classes/Class-Content";
	}

}
