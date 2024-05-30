package com.example.demo.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;






@Configuration
public class SecurityConfig   {
	@Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//        .authorizeRequests()
//            .requestMatchers("/**").permitAll() // Cho phép truy cập mọi request mà không cần xác thực
//            .anyRequest().authenticated() // Tất cả các request còn lại cần xác thực
//        .and()
//        .formLogin() // Cho phép sử dụng form login
//            .loginPage("/Home/login") // Trang đăng nhập tùy chỉnh
//            .permitAll() // Cho phép truy cập vào trang đăng nhập
//        .and()
//        .logout() // Cho phép logout
//            .permitAll(); // Cho phép truy cập vào trang logout
//        return http.build();
//    }

	


	
}
