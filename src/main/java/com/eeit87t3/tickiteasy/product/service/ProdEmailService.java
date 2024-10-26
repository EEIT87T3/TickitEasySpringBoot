package com.eeit87t3.tickiteasy.product.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.product.entity.ProdFavoritesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.repository.ProdFavoritesRepo;


@Service
public class ProdEmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ProdFavoritesRepo prodFavoritesRepo;

    public void sendRestockNotification(ProductEntity product) {
        try {
            // 直接查詢所有需要通知的記錄
            List<ProdFavoritesEntity> notifyList = prodFavoritesRepo.findByProductProductIDAndNotifyStatus(
                product.getProductID(), 1);

            for (ProdFavoritesEntity notify : notifyList) {
                // 從關聯實體中直接獲取會員信息
                Member member = notify.getMember();
                
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                
                helper.setTo(member.getEmail());
                helper.setSubject("商品上架通知 - " + product.getProductName());
                
                ClassPathResource resource = new ClassPathResource("templates/product/restockNotification.html");
                String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                
                content = content.replace("{{memberName}}", member.getName())
                               .replace("{{productName}}", product.getProductName())
                               .replace("{{productLink}}", "http://localhost:8080/TickitEasy/product/" + product.getProductID());
                
                helper.setText(content, true);
                mailSender.send(mimeMessage);
                
                // 更新通知狀態
                notify.setNotifyStatus(0);
                prodFavoritesRepo.save(notify);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("發送上架通知郵件失敗", e);
        }
    }
}