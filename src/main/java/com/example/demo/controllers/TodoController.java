package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Account;
import com.example.demo.models.Todo;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.TodoRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Todo")
public class TodoController {
	@Autowired
    private TodoRepository todoRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	
    @GetMapping("/listTodo")
    public String getAllTodos(Model m, HttpServletRequest request) {
    	HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");
		
		// Kiểm tra xem tài khoản đã đăng nhập chưa
		if (loggedInUser == null) {
			// Xử lý khi tài khoản chưa đăng nhập
			session.setAttribute("redirectUrl", "/Todo/listTodo");
			m.addAttribute("notLogin","You must login to access this!!");

			return "/Authen/Login";
		}
		List<Todo> todo = todoRepository.listTodoByAccountId(loggedInUser.getAccountId());
		if(!todo.isEmpty())
			m.addAttribute("listTodo",todo );
		//ảnh header
		Optional<Account> accountImg = accountRepository.findById(loggedInUser.getAccountId());
		m.addAttribute("account", accountImg.get());
		
		return "/Todo/Todo-List";
    }

    @PostMapping("/addTodo")
    public String createTodo(@RequestParam("taskName") String taskName,Model m , HttpServletRequest request) {
    	HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

		Todo todo = new Todo();
		todo.setTaskName(taskName);
		todo.setAccount(loggedInUser);
		todoRepository.save(todo);
        return "redirect:/Todo/listTodo";
    }

    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Long id, @RequestParam("taskName") String taskName ) {
        Optional<Todo> todo = todoRepository.findById(id);     
        todo.get().setTaskName(taskName);
        todoRepository.save(todo.get());
        return "redirect:/Todo/listTodo";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
    	Optional<Todo> todo = todoRepository.findById(id);
        todoRepository.delete(todo.get());
        return "redirect:/Todo/listTodo";
    }
}
