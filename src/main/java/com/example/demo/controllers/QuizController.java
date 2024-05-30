package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Account;
import com.example.demo.models.Classes;
import com.example.demo.models.Quiz;
import com.example.demo.models.QuizQuestion;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.ClassRepository;
import com.example.demo.repositories.QuizQuestionRepository;
import com.example.demo.repositories.QuizRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Quiz")
public class QuizController {
	
	@Autowired
    private QuizRepository quizRepository;
	
	@Autowired
    private QuizQuestionRepository quizQuestionRepository;
	
	@Autowired
    private ClassRepository classRepository;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@GetMapping("/list/{classId}")
	public String listQuiz(@PathVariable("classId") String classId ,Model m, HttpServletRequest request) {
		
		List<Quiz> quiz1 = quizRepository.findByClassId(classId);
		if(!quiz1.isEmpty())
			m.addAttribute("listQuiz1",quiz1);
		m.addAttribute("idClass",classId);
		
		return "/Quiz/Quiz-List";
	}
	
	@GetMapping("/detail/{quizId}")
	public String quizDetailView(@PathVariable("quizId") String id ,Model m, HttpServletRequest request) {
		
		List<QuizQuestion> quiz = quizQuestionRepository.findByQuizId(id);
		if(!quiz.isEmpty())
			m.addAttribute("listQuestion",quiz);
		return "/Quiz/Quiz-Detail";
	}
	
	
	
	@GetMapping("/do/{quizId}")
    public String showDoQuizView(@PathVariable("quizId") String id ,Model m, HttpServletRequest request
    		, RedirectAttributes redirectAttributes) {
		
		
		Optional<Quiz> quizA = quizRepository.findById(id);
		m.addAttribute("quizDetail",quizA.get());
		
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		// Kiểm tra xem tài khoản đã đăng nhập chưa
		if (loggedInUser == null) {
			// Xử lý khi tài khoản chưa đăng nhập
			session.setAttribute("redirectUrl", "/Quiz/do/" + id);
			redirectAttributes.addFlashAttribute("notLogin","You must login to access this!!");
			return "redirect:/Authen/loginView";
		}
		m.addAttribute("userInfo",loggedInUser.getAccountId());
		m.addAttribute("quizInfo",id);
		
		
		List<QuizQuestion> quiz = quizQuestionRepository.findByQuizId(id);		
		if(quiz.isEmpty())
		{
			redirectAttributes.addFlashAttribute("listEmpty", "Quiz chưa tạo câu hỏi !!");
			return "redirect:/Quiz/list";
		}
			
	    List<Map<String, Object>> formattedQuestions = new ArrayList<>();
	    List<Map<String, Object>> formattedAnswers = new ArrayList<>();
	    for (QuizQuestion question : quiz) {
	        Map<String, Object> formattedQuestion = new HashMap<>();
	        formattedQuestion.put("quiz_id", question.getQuizAnswerId());
	        List<String> ques = new ArrayList<>();        
	        	ques.add(question.getFakeAnswer1());
	        	ques.add(question.getFakeAnswer2());
	        	ques.add(question.getFakeAnswer3());
	        	ques.add(question.getAnswer());		
	        formattedQuestion.put("question", question.getQuestion());
	        formattedQuestion.put("answers", ques);
	        formattedQuestions.add(formattedQuestion);
	        
	        Map<String, Object> formattedAnswer = new HashMap<>();
	        formattedAnswer.put("quiz_id", question.getQuizAnswerId());      	
	        formattedAnswer.put("answer", question.getAnswer());
	        formattedAnswers.add(formattedAnswer);
	        
	      }
	   
	    m.addAttribute("listQuestion",formattedQuestions);
	    m.addAttribute("numQuestion",formattedQuestions.size());
	    m.addAttribute("listAnswer",formattedAnswers);
		System.out.println(quizA.get().getListMember());

        return "/Quiz/Quiz-Do";

    }
	
	@GetMapping("/create/{classId}")
    public String showCreateQuizView(@PathVariable("classId") String classId, Model m) {
		
		Optional<Classes> getClass = classRepository.findById(classId);
	    m.addAttribute("c",getClass.get());

        return "/Quiz/Quiz-Create";

    }
	
	@PostMapping("/createQuiz")
	public String createQuiz(@RequestParam("quizName") String name,
							@RequestParam("description") String description,
							@RequestParam("classes") Classes classes)			
	{      
		Quiz quiz = new Quiz();
		quiz.setQuizName(name);
		quiz.setDescription(description);
		quiz.setDateCreated(LocalDateTime.now());
		quiz.setClasses(classes);
		HashMap<Long, Integer> listMember = new HashMap<Long, Integer>();
		quiz.setListMember(listMember);
		quizRepository.save(quiz);
	    return "redirect:/Quiz/list/" + classes.getClassId();
		
	}
	
	
	@GetMapping("/createQuestion/{quizId}")
    public String showCreateAnswerView(@PathVariable("quizId") String id,Model m) {
		
		Optional<Quiz> getQuiz = quizRepository.findById(id);
		
		if(getQuiz.isPresent())
			m.addAttribute("id",getQuiz.get().getQuizId());			
        return "/Quiz/Quiz-CreateQuestion";

    }
	
	@PostMapping("/createQuestion")
	public String createQuizAnswer(
									@RequestParam("question") String question,
									@RequestParam("fakeAnswer1") String fakeAnswer1,
									@RequestParam("fakeAnswer2") String fakeAnswer2,
									@RequestParam("fakeAnswer3") String fakeAnswer3,
									@RequestParam("answer") String answer,
									@RequestParam("quiz") Quiz quiz)			
	{      
		QuizQuestion quizQuestion = new QuizQuestion();
		quizQuestion.setQuestion(question);
		quizQuestion.setAnswer(answer);
		quizQuestion.setFakeAnswer1(fakeAnswer1);
		quizQuestion.setFakeAnswer2(fakeAnswer2);
		quizQuestion.setFakeAnswer3(fakeAnswer3);
		quizQuestion.setQuiz(quiz);
		quizQuestionRepository.save(quizQuestion);
		return "redirect:/Quiz/createQuestion/" + quiz.getQuizId();

		
	}
	
	@GetMapping("/submitQuiz/{quizId}")
	public String submitQuiz(@PathVariable("quizId") String quizId, 
			@RequestParam("correct") int correct,
			HttpServletRequest request)			
	{      
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		Optional<Quiz> quiz = quizRepository.findById(quizId);
		// nếu người đó chưa làm rồi thì put vào HashMap
		quiz.get().getListMember().put(loggedInUser.getAccountId(), correct);
		System.out.println("AAAA");
		System.out.println(quiz.get().getListMember().get(loggedInUser.getAccountId()));
		quizRepository.save(quiz.get());
		return "redirect:/Quiz/list/" + quiz.get().getClasses().getClassId();

		
	}
	
	@GetMapping("/listSubmitted/{quizId}")
	public String listSubmitQuiz(@PathVariable("quizId") String quizId, 
			HttpServletRequest request, Model m)			
	{      
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		List<Account> listAccount = accountRepository.findAll();
		Optional<Quiz> quiz = quizRepository.findById(quizId);
		if(!quiz.get().getListMember().isEmpty())
			m.addAttribute("listSubmit",quiz.get().getListMember());	
		m.addAttribute("listAccount",listAccount);	

		
		

		return "/Quiz/Quiz-ListSubmitted";

		
	}
	
	
	
	
	
	
	
	
}
