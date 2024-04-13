package com.example.demo.controllers;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.helpers.PasswordResetTokenUtils;
import com.example.demo.models.Account;
import com.example.demo.models.Role;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.EmailService;
import com.example.demo.services.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ResourceLoader;



@Controller
@RequestMapping("/Authen")
public class AuthenController {
	
	
	@Autowired
    private AccountRepository accountRepository;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private AccountService accountService;
	
	@Autowired
    private RoleService roleService;
	
	@Autowired
    private EmailService emailService;
	@Autowired
	private HttpSession session;
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	
	
	@PostMapping("/login")
    public String checkLogin(@RequestParam("email") String email,
    						 @RequestParam("password") String password,
    						 Model m) {	       	               
        
    	Optional<Account> existingAccount = accountRepository.findByEmail(email.trim());
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();   
            if(passwordEncoder.matches(password,account.getPassword() ))
            {
                session.setAttribute("loggedInUser", account);
                return "redirect:/Class/listClass";
            }
            	
            else {
                m.addAttribute("loginFail", "Email hoặc mật khẩu không đúng, vui lòng đăng nhập lại!");
                m.addAttribute("currentEmail", email);
                return "/Authen/Login";
            }      
        } else {
        	m.addAttribute("loginFail", "Email hoặc mật khẩu không đúng, vui lòng đăng nhập lại!");
        	m.addAttribute("currentEmail", email);
            return "/Authen/Login";
        }
    }
	
	@GetMapping("/logout")
    public String logout(HttpSession session,Model m) {
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if(loggedInUser != null)
	    	m.addAttribute("currentEmail", loggedInUser.getEmail());
        session.invalidate();
        return "/Authen/Login";
    }
	

	@PostMapping("/register")
	public String createAccount(@RequestParam("role") Role role,
								@RequestParam("email") String email,
								@RequestParam("displayName") String displayName,
								@RequestParam("password") String password,
								@RequestParam("confirm-password") String confirmpassword,
								Model model)
			
	{      
    	Account account = new Account();
    	List<Role> roles = roleService.getAllRoles();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		if (accountService.isEmailTaken(email) || !email.matches(emailRegex) ) {
	        model.addAttribute("emailError", "Email is invalid or Email is already taken. Please choose a different one.");
	        model.addAttribute("roles", roles);
	        return "/Authen/Register";
	    }
	    
		if (password.length() < 6) {
            model.addAttribute("passwordError", "Password must be at least 6 characters long.");
            model.addAttribute("roles", roles);
            return "/Authen/Register";
        }
		if (!accountService.containsUppercaseAndLowercase(password)) {
	        model.addAttribute("passwordError", "Password must contain at least one uppercase letter and one lowercase letter.");
	        model.addAttribute("roles", roles);
	        return "/Authen/Register";
	    }
		if(!confirmpassword.equals(password))
    	{
			model.addAttribute("roles", roles);
	        model.addAttribute("passwordError", "New pass and confirm pass is not match");
			return "/Authen/Register";
    	}
		
		account.setRole(role);
		account.setEmail(email);
		account.setDisplayName(displayName);
		account.setTimeCreated(LocalDate.now());
		account.setImage(null);
		
        

		
        String hashedPassword = passwordEncoder.encode(password);        
        account.setPassword(hashedPassword);
		
        //set image default...
	    accountService.saveAccount(account);
	    model.addAttribute("currentEmail", account.getEmail());
	    model.addAttribute("currentPassword", password);
	    return "/Authen/Login";			
	}
	
	
	
	@GetMapping("/forgotPass")
    public String showEnterEmailForm() {
        return "/Authen/forgotPass_Email";
    }
	
	@PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Optional<Account> account = accountRepository.findByEmail(email);
       
        	
        if (account.isPresent()) {
            // Generate reset token and save it in the database
            String resetToken = PasswordResetTokenUtils.generateToken();
            account.get().setResetToken(resetToken);
            LocalDateTime expirationTime = LocalDateTime.now().plus(2, ChronoUnit.MINUTES);
            account.get().setResetTokenExpiration(expirationTime);
            accountRepository.save(account.get());

            // Send reset password email
            String resetLink = "http://localhost:8080/Authen/forgotPass_token?token=" + resetToken;
            String emailBody = "Click the following link to reset your password: <a href=\"" + resetLink + "\">Reset Password</a>";
            emailService.sendResetPasswordEmail(email, "Password Reset", emailBody);
        } else {
            model.addAttribute("emailError", "Không tìm thấy tài khoản có email vừa nhập!");
            return "/Authen/forgotPass_Email";
        }

        return "Right";
    }
	@GetMapping("/forgotPass_token")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Check if the token is valid
        Optional<Account> account = accountRepository.findByResetToken(token);
        
        if (account.isPresent() && !account.get().isResetTokenExpired(account.get().getResetTokenExpiration())) {
            model.addAttribute("resetToken", token);
            return "/Authen/forgotPass_Token";
        } else {
//            return "invalidTokenPage";
        	return "Wrong";
        }
    }
	
	@PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword, 
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        Optional<Account> account = accountRepository.findByResetToken(token);
        
        if (newPassword.length() < 6) {
            model.addAttribute("passwordError", "Password must be at least 6 characters long.");
            model.addAttribute("resetToken", token);
            return "/Authen/forgotPass_Token";
        }
		if (!accountService.containsUppercaseAndLowercase(newPassword)) {
	        model.addAttribute("passwordError", "Password must contain at least one uppercase letter and one lowercase letter.");
	        model.addAttribute("resetToken", token);
            return "/Authen/forgotPass_Token";
	    }
		if(!confirmPassword.equals(newPassword))
    	{
			model.addAttribute("resetToken", token);
	        model.addAttribute("passwordError", "New pass and confirm pass is not match");
	        return "/Authen/forgotPass_Token";
    	}
        
        if (account != null) {
            // Update the password and reset the token
            String hashedPassword = passwordEncoder.encode(newPassword);
            account.get().setPassword(hashedPassword);
            account.get().setResetToken(null);
            account.get().setResetTokenExpiration(null);
            accountRepository.save(account.get());
            model.addAttribute("passwordReset", "Thay đổi mật khẩu thành công, vui lòng đăng nhập lại!");
        } else {
            model.addAttribute("passwordReset", "Thay đổi mật khẩu không thành công!");
        }
        return "/Authen/Login";
    }
	@GetMapping("/changePassword")
    public String showChangePasswordForm(HttpServletRequest request,Model m) {
		
		HttpSession session = request.getSession();
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
	        // Xử lý khi tài khoản chưa đăng nhập
	        return "/Authen/Login";
	    }
        
	    Optional<Account> acc = accountRepository.findById(loggedInUser.getAccountId());
	    
        m.addAttribute("account",acc.get());
        return "/Authen/changePass";
    }
	
	@PostMapping("/changePassword")
    public String changePassword(@RequestParam("old-pass") String oldPass,
    							 @RequestParam("new-pass") String newPass,
    							 @RequestParam("confirm-pass") String confirmPass,
    							 Model model) {
		
	    Account loggedInUser = (Account) session.getAttribute("loggedInUser");
	    if(loggedInUser != null)
	    {
	    	if(!passwordEncoder.matches(oldPass,loggedInUser.getPassword()))
	    	{
	    		model.addAttribute("changePasswordFail", "Current password wrong");
	    		return "/Authen/changePass";
	    	}
	    	if (newPass.length() < 6) {
	            model.addAttribute("changePasswordFail", "Password must be at least 6 characters long.");
	            return "/Authen/changePass";
	        }
			if (!accountService.containsUppercaseAndLowercase(newPass)) {
		        model.addAttribute("changePasswordFail", "Password must contain at least one uppercase letter and one lowercase letter.");
		        return "/Authen/changePass";
		    }
	    	
	    	if(oldPass.equals(newPass))
	    	{
	    		model.addAttribute("changePasswordFail", "New pass have to diff old pass");
    			return "/Authen/changePass";
	    	}
	    	if(!newPass.equals(confirmPass))
	    	{
	    		model.addAttribute("changePasswordFail", "New pass and confirm pass is not match");
				return "/Authen/changePass";
	    	}
	    	String hashedPassword = passwordEncoder.encode(newPass);
			loggedInUser.setPassword(hashedPassword);	    	            
            accountRepository.save(loggedInUser);
            model.addAttribute("changePasswordSuccess", "Change pass sucessfully");  		    	
	    }
	    else
	    {
	    	model.addAttribute("changePasswordFail", "Have to loginnnn");
	    }
        return "/Authen/changePass";
    }
	
	

}
