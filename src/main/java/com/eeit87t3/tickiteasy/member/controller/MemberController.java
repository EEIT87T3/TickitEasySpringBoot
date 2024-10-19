package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.test.EmailService;
import com.eeit87t3.tickiteasy.util.JWTUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;  // 用來發驗證信
    
    @Autowired
    private JWTUtil jwtUtil;	//用來處理JWT Token
    
    @Autowired
    private ImageUtil imageUtil;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> payload) {
    	Map<String, String> response = new HashMap<>();
    	try {
			//拿數據
    		String email = (String) payload.get("email");
    		String password = (String) payload.get("password");
            String confirmPassword = (String) payload.get("confirmPassword");
            String name = (String) payload.get("name");
            String nickname = (String) payload.get("nickname");
            String birthDateStr = (String) payload.get("birthDate");
            String phone = (String) payload.get("phone");
            
            //後端正規表達式驗證
            if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                response.put("error", "請輸入有效的電子信箱");
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")) {
                response.put("error", "密碼需至少8位，包含大小寫字母、數字和特殊字符");
                return ResponseEntity.badRequest().body(response);
            }

            if (!password.equals(confirmPassword)) {
                response.put("error", "兩次密碼輸入不一致");
                return ResponseEntity.badRequest().body(response);
            }

            if (name == null || name.trim().isEmpty()) {
                response.put("error", "姓名不得為空");
                return ResponseEntity.badRequest().body(response);
            }

            if (nickname == null || nickname.trim().isEmpty()) {
                response.put("error", "暱稱不得為空");
                return ResponseEntity.badRequest().body(response);
            }

            if (birthDateStr == null || birthDateStr.isEmpty()) {
                response.put("error", "請選擇您的生日");
                return ResponseEntity.badRequest().body(response);
            }

            LocalDate birthDate = LocalDate.parse(birthDateStr);

            if (birthDate.isAfter(LocalDate.now())) {
                response.put("error", "生日不得為未來日期");
                return ResponseEntity.badRequest().body(response);
            }

            if (phone == null || !phone.matches("^09\\d{8}$")) {
                response.put("error", "請輸入有效的手機號碼");
                return ResponseEntity.badRequest().body(response);
            }
            
            //註冊會員並拿到Token
            String verificationToken = memberService.register(email, password, name, nickname, birthDate, phone);
            
            //寄驗證信
            String verificationLink = "http://localhost:8080/TickitEasy/member/verify?token=" + verificationToken;
            emailService.sendVerificationEmail(email, verificationLink);

    		response.put("message", "註冊成功！請檢查您的信箱以驗證帳號。");
    		return ResponseEntity.ok(response);
            
		} catch (IllegalArgumentException e) {
			// 參數異常錯誤訊息
			logger.error("註冊失敗：{}", e.getMessage());
			response.put("error", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}catch (Exception e) {
			//其他異常
			logger.error("發生錯誤：{}", e.getMessage());
			response.put("error", "發生錯誤，請稍後再試");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
    }

    
 // 處理會員登入，返回 JWT Token
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = payload.get("email");
            String password = payload.get("password");

            Optional<String> token = memberService.login(email, password);

            if (token.isPresent()) {
                response.put("status", "success");
                response.put("token", token.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "無效的電子信箱或密碼");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "發生錯誤，請稍後再試。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
 // 處理會員登出（JWT為無狀態性，後端不用處理，只需要用前端刪除Token)
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "您已成功登出。");
        return ResponseEntity.ok(response);
    }


 // 顯示會員個人資料（需要驗證Token）
    @GetMapping("/profile")
    public ResponseEntity<Member> showProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // 從 Authorization Header拿Token
            String token = authHeader.replace("Bearer ", "");
            // 從Token拿電子信箱
            String email = jwtUtil.getEmailFromToken(token);
            // 根據電子信箱拿到會員的資料
            Member member = memberService.findByEmail(email);

            if (member != null) {
                return ResponseEntity.ok(member);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error("讀取個人資料失敗：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    // 處理會員個人資料更新，不包括圖片上傳（需要驗證Token)
    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
        	// 從 Authorization Header拿Token
            String token = authHeader.replace("Bearer ", "");
            // 從Token拿電子信箱
            String email = jwtUtil.getEmailFromToken(token);
            // 根據電子信箱拿到會員的資料
            Member currentMember = memberService.findByEmail(email);

            if (currentMember == null) {
                response.put("error", "會員不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 更新會員資料
            String name = (String) payload.get("name");
            String nickname = (String) payload.get("nickname");
            String phone = (String) payload.get("phone");
            String birthDateStr = (String) payload.get("birthDate");

            LocalDate birthDate = null;
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                birthDate = LocalDate.parse(birthDateStr);
            }

            // 後端正規表示式驗證
            if (name == null || name.trim().isEmpty()) {
                response.put("error", "姓名不得為空");
                return ResponseEntity.badRequest().body(response);
            }

            if (nickname == null || nickname.trim().isEmpty()) {
                response.put("error", "暱稱不得為空");
                return ResponseEntity.badRequest().body(response);
            }

            if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
                response.put("error", "生日不得為未來日期");
                return ResponseEntity.badRequest().body(response);
            }

            if (phone == null || !phone.matches("^\\d{10}$")) {
                response.put("error", "請輸入有效的手機號碼");
                return ResponseEntity.badRequest().body(response);
            }

            Member updatedInfo = new Member();
            updatedInfo.setName(name);
            updatedInfo.setNickname(nickname);
            updatedInfo.setPhone(phone);
            updatedInfo.setBirthDate(birthDate);

            memberService.updateProfile(currentMember, updatedInfo);

            response.put("message", "資料更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("更新個人資料失敗：{}", e.getMessage());
            response.put("error", "資料更新失敗");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


 // 處理會員頭貼上傳（需驗證Token)
    @PostMapping("/profilePic")
    public ResponseEntity<Map<String, String>> uploadProfilePic(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("profilePic") MultipartFile profilePic) {
        Map<String, String> response = new HashMap<>();
        try {
            // 從 Authorization Header拿Token
            String token = authHeader.replace("Bearer ", "");
            // 從Token拿電子信箱
            String email = jwtUtil.getEmailFromToken(token);
            // 根據電子信箱拿到會員的資料
            Member currentMember = memberService.findByEmail(email);

            if (currentMember == null) {
                response.put("error", "會員不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 處理頭貼上傳
            memberService.uploadProfilePic(currentMember.getMemberID(), profilePic);

            response.put("message", "頭貼上傳成功");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("頭貼上傳失敗：{}", e.getMessage());
            response.put("error", "頭貼上傳失敗");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            logger.error("錯誤：{}", e.getMessage());
            response.put("error", "發生錯誤，請稍後再試。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 讀取會員頭像（不用Token，可以直接讀取)
    @GetMapping("/profilePic/{memberId}")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable Integer memberId) {
        try {
            byte[] imageBytes = memberService.getProfilePic(memberId);
            MediaType mediaType = imageUtil.determineMediaType(memberService.getProfilePicFilename(memberId));
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (IOException e) {
            logger.error("讀取頭貼失敗：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
