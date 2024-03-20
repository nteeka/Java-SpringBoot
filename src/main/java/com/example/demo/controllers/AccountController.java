package com.example.demo.controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.example.demo.models.Role;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Account")
public class AccountController {
	
	
	@Autowired
 	private AccountService accountService;
 @Autowired
    private AccountRepository accountRepository;
 
 @Autowired
    private RoleService roleService;
 
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Optional<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }
    
    
    
    

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
    	Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setDeleted(true);
            accountRepository.save(account); // Save the changes back to the database
            return "redirect:/StudentView/listStudent";   //change after
        }
        return "redirect:/StudentView/listStudent";//change after
    }
    
    @PostMapping("/{id}")
    public String updateAccount(@PathVariable("id") long id,
    							@RequestParam("displayName") String displayName,
    							@RequestParam("email") String Email,
    							@RequestParam("bio") String bio,    							
    							Model model) {	       	               
        
    	Optional<Account> existingAccount = accountRepository.findById(id);
        if (!existingAccount.isPresent()) {
        	return "redirect:/StudentView/listStudent"; // error, change after	    
        } 
        Account account = existingAccount.get();                   		        
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Email.matches(emailRegex) || accountService.isEmailTaken(Email,id)) {
        	Optional<Account> account1 = accountService.getAccountById(id);
	        model.addAttribute("emailError", "Email is already taken or Email is invalid. Please choose a different one.");
	        List<Role> roles = roleService.getAllRoles();		       
	        String imageUrl = "/image/getImage/" + account.getImage();
	        model.addAttribute("account",account1);  
	        model.addAttribute("imageUrl", imageUrl);
	        model.addAttribute("roles",roles);
	        return "/Account/Account-Edit";
	    }	
        account.setEmail(Email);
        account.setBio(bio);
        account.setDisplayName(displayName);
		accountRepository.save(account);   	
        return "redirect:/Account/edit/" + existingAccount.get().getAccountId();  
    }
    
    @GetMapping("/edit/{id}")
    public String editAccountForm(@PathVariable Long id,Model m,HttpServletRequest request) {
    	
//    	HttpSession session = request.getSession();
//	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
//	    if (loggedInUser == null) {
//	        return "/Authen/Login";
//	    }
    	Optional<Account> acc = accountRepository.findById(id);
    	m.addAttribute("account",acc.get()); 
        return "/Account/Account-Edit";
    }
    
    @PostMapping("/saveImage")
    public String saveImage(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if (loggedInUser == null) {
	        return "/Authen/Login";
	    }
	    Optional<Account> account = accountRepository.findById(loggedInUser.getAccountId()); // Thay userId bằng userId của người dùng
	    
	    Path path = Paths.get("uploads/");
	    try {
	        InputStream inputStream = file.getInputStream();
	        // Tạo tên file mới với định dạng account_img_id
	        String newFileName = "account_img_" + account.get().getAccountId();
	        Files.copy(inputStream, path.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
	        account.get().setImage(newFileName);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		accountRepository.save(account.get());
		return "redirect:/StudentView/listStudent";        
    }
    
   
    
    
}
