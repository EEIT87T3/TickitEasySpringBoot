package com.eeit87t3.tickiteasy.test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

/**
 * @author Lilian (Curriane)
 */
@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
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
    
    //寄註冊驗證信
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
    
    //密碼重設驗證信
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            logger.info("開始發送重設密碼郵件給：{}", to);
            
            ClassPathResource resource = new ClassPathResource("templates/mail/resetPassword.html");
            InputStream inputStream = resource.getInputStream();
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            
            content = content.replace("{{resetLink}}", resetLink);
            
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject("重設您的密碼");
            helper.setText(content, true);
            helper.setFrom("eeit87t3@gmail.com");  // 確保設置發件人
            
            mailSender.send(mimeMessage);
            logger.info("重設密碼郵件發送成功");
            
        } catch (Exception e) {
            logger.error("發送重設密碼郵件失敗：{}", e.getMessage(), e);
            throw new IllegalStateException("無法發送重設密碼郵件：" + e.getMessage());
        }
    }
}