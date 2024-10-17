package com.eeit87t3.tickiteasy.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private EmailService emailService;
    //僅供測試使用 （PostMan) 
    @GetMapping("/sendMail")
    public String sendMail(
        @RequestParam String to, 
        @RequestParam String subject, 
        @RequestParam String body) {

        emailService.sendSimpleEmail(to, subject, body);
        return "郵件已發送";
    }
    //測試驗證信
    @GetMapping("/testSendEmail")
    public String testSendEmail() {
        // 測試郵件地址和驗證連結
        String testEmail = "h969698810@yahoo.com.tw";
        String verificationLink = "http://localhost:8080/TickitEasy/user/verify?token=123456";

        // 調用 EmailService 發送郵件
        emailService.sendVerificationEmail(testEmail, verificationLink);

        return "測試郵件已發送";
    }
    
}