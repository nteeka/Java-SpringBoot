package com.example.demo.controllers;

import java.io.Console;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.helpers.PasswordResetTokenUtils;
import com.example.demo.models.Student;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.EmailService;
import com.example.demo.services.StudentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Authen")
public class AuthenController {
	
	@Autowired
    private StudentRepository studentRepository;
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private EmailService emailService;
	@Autowired
	private HttpSession session;
	@PostMapping("/login")
    public String checkLogin(@RequestParam("email") String email,
    						 @RequestParam("password") String password,
    						 Model m) {	       	               
        
    	Optional<Student> existingStudent = studentRepository.findByEmail(email.trim());
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();   
            if(passwordEncoder.matches(password,student.getPassword() ))
            {
                session.setAttribute("loggedInUser", student);
                return "redirect:/StudentView/listStudent";
            }
            	
            else {
                m.addAttribute("loginFail", "Email hoặc mật khẩu không đúng, vui lòng đăng nhập lại!");
                return "Login"; //validate sau
            }      
        } else {
        	m.addAttribute("loginFail", "Email hoặc mật khẩu không đúng, vui lòng đăng nhập lại!");
            return "Login"; //validate sau
        }
    }
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Home/login";
    }
	
	
	@GetMapping("/forgotPass")
    public String showEnterEmailForm() {
        return "forgotPass_Email";
    }
	
	@PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Optional<Student> student = studentRepository.findByEmail(email);
       
        	
        if (student.isPresent()) {
            // Generate reset token and save it in the database
            String resetToken = PasswordResetTokenUtils.generateToken();
            student.get().setResetToken(resetToken);
            LocalDateTime expirationTime = LocalDateTime.now().plus(2, ChronoUnit.MINUTES);
            student.get().setResetTokenExpiration(expirationTime);
            studentRepository.save(student.get());

            // Send reset password email
            String resetLink = "http://localhost:8080/Authen/forgotPass_token?token=" + resetToken;
            String emailBody = "Click the following link to reset your password: <a href=\"" + resetLink + "\">Reset Password</a>";
            emailService.sendResetPasswordEmail(email, "Password Reset", emailBody);
        } else {
            model.addAttribute("emailError", "Không tìm thấy student có email vừa nhập!");
            return "forgotPass_Email";
        }

        return "Right";
    }
	@GetMapping("/forgotPass_token")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Check if the token is valid
        Optional<Student> student = studentRepository.findByResetToken(token);
        
        if (student.isPresent() && !student.get().isResetTokenExpired(student.get().getResetTokenExpiration())) {
            model.addAttribute("resetToken", token);
            return "forgotPass_Token";
        } else {
//            return "invalidTokenPage";
        	return "Wrong";
        }
    }
	
	@PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        Optional<Student> student = studentRepository.findByResetToken(token);

        if (student != null) {
            // Update the password and reset the token
            String hashedPassword = passwordEncoder.encode(newPassword);
            student.get().setPassword(hashedPassword);
            student.get().setResetToken(null);
            student.get().setResetTokenExpiration(null);
            studentRepository.save(student.get());
            model.addAttribute("passwordReset", "Thay đổi mật khẩu thành công, vui lòng đăng nhập lại!");
        } else {
            model.addAttribute("passwordReset", "Thay đổi mật khẩu không thành công!");
        }
        return "Login";
    }
	@GetMapping("/changePassword")
    public String showChangePasswordForm() {
        return "changePass";
    }
	
	@PostMapping("/changePassword")
    public String changePassword(@RequestParam("old-pass") String oldPass,
    							 @RequestParam("new-pass") String newPass,
    							 @RequestParam("confirm-pass") String confirmPass,
    							 Model model) {
		
	    Student loggedInUser = (Student) session.getAttribute("loggedInUser");
	    if(loggedInUser != null)
	    {    	
	    	if(!passwordEncoder.matches(oldPass,loggedInUser.getPassword()))
	    	{
	    		model.addAttribute("changePasswordFail", "Current password wrong");
	    		return "changePass";
	    	}
	    	if(oldPass.equals(newPass))
	    	{
	    		model.addAttribute("changePasswordFail", "New pass have to diff old pass");
    			return "changePass";
	    	}
	    	if(!newPass.equals(confirmPass))
	    	{
	    		model.addAttribute("changePasswordFail", "New pass and confirm pass is not match");
				return "changePass";
	    	}
	    	String hashedPassword = passwordEncoder.encode(newPass);
			loggedInUser.setPassword(hashedPassword);	    	            
            studentRepository.save(loggedInUser);
            model.addAttribute("changePasswordSuccess", "Change pass sucessfully");  		    	
	    }
	    else
	    {
	    	model.addAttribute("changePasswordFail", "Have to loginnnn");
	    }
        return "changePass";
    }
	
	

}
