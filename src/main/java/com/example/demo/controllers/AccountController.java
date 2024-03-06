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
    							@RequestParam("email") String Email,
    							@RequestParam("role") Role role,
    							@RequestParam("image") MultipartFile multipartFile,
    							Model model) {	       	               
        
    	Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
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
            account.setRole(role);
    		Path path = Paths.get("uploads/");
    		try{
    			InputStream inputStream = multipartFile.getInputStream();
    			Files.copy(inputStream, path.resolve(multipartFile.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
    			account.setImage(multipartFile.getOriginalFilename().toLowerCase());
    		}catch (Exception e) {
    			e.printStackTrace();
    		}           
    		accountRepository.save(account);
    		return "redirect:/StudentView/listStudent";   // change after    
        } else {
        	return "redirect:/StudentView/listStudent"; // change after 
        }
    }
}
