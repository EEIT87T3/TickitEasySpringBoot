package com.eeit87t3.tickiteasy.test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    //測試用
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("eeit87t3@gmail.com");  // 替換為你的郵件地址
        mailSender.send(message);
    }
    
    public void sendVerificationEmail(String to, String verificationLink) {
    	try {
            // 使用 ClassPathResource 讀取 HTML 模板
            ClassPathResource resource = new ClassPathResource("templates/mail/verificationEmail.html");
            InputStream inputStream = resource.getInputStream();
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);  // 讀取並轉換成字串
            
            // 替換模板中的變數
            content = content.replace("{{verificationLink}}", verificationLink);

            // 發送郵件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("請驗證您的帳號");
            helper.setText(content, true);  // true 表示 HTML 內容

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new IllegalStateException("無法發送驗證郵件", e);
        }
    }
}