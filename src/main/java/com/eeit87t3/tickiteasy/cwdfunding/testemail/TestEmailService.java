package com.eeit87t3.tickiteasy.cwdfunding.testemail;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.service.FundOrderService;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;

import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

/**
 * @author TingXD (chen19990627)
 */
@Service
public class TestEmailService {
	
	
	   @Autowired
	   private JavaMailSender mailSender;
	   
	   @Autowired
	   private FundOrderService fundOrderService;
	   
	   @Autowired
	   private MemberService memberService;
	   
	   public void sendDonateSuccessEmail(String to) {
	    	try {
	            // 使用 ClassPathResource 讀取 HTML 模板
	            ClassPathResource resource = new ClassPathResource("templates/mail/donateSuccess.html");
	            InputStream inputStream = resource.getInputStream();
	            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);  // 讀取並轉換成字串
	            
	            // 替換模板中的變數
	            //content = content.replace("{{verificationLink}}", verificationLink);

	            // 發送郵件
	            MimeMessage mimeMessage = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	            helper.setTo(to);
	            helper.setSubject("專案募資成功");
	            helper.setText(content, true);  // true 表示 HTML 內容

	            mailSender.send(mimeMessage);
	        } catch (Exception e) {
	            throw new IllegalStateException("無法發送驗證郵件", e);
	        }
	    }
	   
		/* 找出贊助過募資專案的會員ID */
	   public List<String> findDonatedMembersEmail(Integer projectID){
		    
			List<Integer> memberIDList = fundOrderService.findMemberIDByProjID(projectID);
			List<String> memberEmailList = new ArrayList<>();

				for (Integer memberID : memberIDList) {
					Member member = memberService.getMemberById(memberID).get();
					String toEmail = member.getEmail();
					memberEmailList.add(toEmail);
					System.out.println(toEmail);
				}
				
				return memberEmailList;
			
	   }
	   

	   
}
