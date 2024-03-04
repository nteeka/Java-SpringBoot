package com.example.demo.services;

public interface EmailService {
    void sendResetPasswordEmail(String to, String subject, String body);
}
