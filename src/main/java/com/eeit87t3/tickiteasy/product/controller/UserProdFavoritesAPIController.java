package com.eeit87t3.tickiteasy.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.UserProdFavoritesService;
import com.eeit87t3.tickiteasy.util.JWTUtil;


@RestController
@RequestMapping("/user/api/product")
public class UserProdFavoritesAPIController {

	@Autowired
    private UserProdFavoritesService favoritesService;
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private MemberService memberService;

    // 收藏/取消收藏商品
    @PostMapping("/favorite/{productID}")
    public ResponseEntity<Map<String, String>> toggleFavorite(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer productID) {
        
        Map<String, String> response = new HashMap<>();
        try {
            // 驗證會員身分
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            Member member = memberService.findByEmail(email);
            
            if (member == null) {
                response.put("error", "會員不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // 處理收藏
            Map<String, String> result = favoritesService.toggleFavorite(member, productID);
            
            if (result.containsKey("error")) {
                return ResponseEntity.badRequest().body(result);
            }
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            response.put("error", "操作失敗，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 設定補貨通知
    @PostMapping("/notify/{productID}")
    public ResponseEntity<Map<String, String>> setNotification(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer productID) {
            
        Map<String, String> response = new HashMap<>();
        try {
            // 驗證會員身分
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            Member member = memberService.findByEmail(email);
            
            if (member == null) {
                response.put("error", "會員不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // 處理補貨通知
            Map<String, String> result = favoritesService.setNotification(member, productID);
            
            if (result.containsKey("error")) {
                return ResponseEntity.badRequest().body(result);
            }
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            response.put("error", "操作失敗，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 查詢會員的收藏商品列表
    @GetMapping("/favorite")
    public ResponseEntity<?> getMemberFavorites(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // 驗證會員身分
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            Member member = memberService.findByEmail(email);
            
            if (member == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "會員不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // 查詢收藏商品
            List<ProductEntity> favorites = favoritesService.getMemberFavorites(member);
            return ResponseEntity.ok(favorites);
            
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "查詢失敗，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
}
