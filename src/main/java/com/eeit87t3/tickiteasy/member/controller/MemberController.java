package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.config.ReCaptchaConfig;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.test.EmailService;
import com.eeit87t3.tickiteasy.util.JWTUtil;
import com.eeit87t3.tickiteasy.util.OAuthLoginRequest;
import com.eeit87t3.tickiteasy.util.ProfileValidator;

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
	private ReCaptchaConfig reCaptchaConfig;
	
    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;  // 用來發驗證信
    
    @Autowired
    private JWTUtil jwtUtil;	//用來處理JWT Token
    
    @Autowired
    private ImageUtil imageUtil;
    
    // 在登入和註冊方法中添加驗證
    private void validateRecaptcha(String token) {
        if (token == null || !reCaptchaConfig.verifyRecaptcha(token)) {
            throw new IllegalArgumentException("驗證失敗，請稍後再試");
        }
    }
    //註冊
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> payload) {
    	Map<String, String> response = new HashMap<>();
    	try {
    		validateRecaptcha((String) payload.get("recaptchaToken"));
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
            String verificationLink = "http://localhost:8080/TickitEasy/verify?token=" + verificationToken;
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
        	validateRecaptcha((String) payload.get("recaptchaToken"));
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
    
    @PostMapping("/oauth/login")
    public ResponseEntity<Map<String, Object>> oauthLogin(@RequestBody OAuthLoginRequest request) {
        // 處理第三方登入
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<String> token = memberService.oauthLogin(request);
            
            if (token.isPresent()) {
                Member member = memberService.findByEmail(request.getEmail());
                boolean needsCompletion = ProfileValidator.needsProfileCompletion(member);
                
                response.put("status", "success");
                response.put("token", token.get());
                response.put("needsCompletion", needsCompletion);

                if (needsCompletion) {
                    Map<String, Boolean> missingFields = new HashMap<>();
                    missingFields.put("phone", member.getPhone() == null || member.getPhone().trim().isEmpty());
                    missingFields.put("birthDate", member.getBirthDate() == null);
                    missingFields.put("name", member.getName() == null || member.getName().trim().isEmpty());
                    missingFields.put("nickname", member.getNickname() == null || member.getNickname().trim().isEmpty());
                    response.put("missingFields", missingFields);
                }
                
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "第三方登入失敗");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("第三方登入失敗：{}", e.getMessage());
            response.put("status", "error");
            response.put("message", "登入過程中發生錯誤");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
   
    //檢查會員資料狀態
    @GetMapping("/profile/status")
    public ResponseEntity<Map<String, Object>> getProfileStatus(
            @RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 從 Token 中獲取 email
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);
            Member member = memberService.findByEmail(email);
            
            if (member == null) {
                response.put("status", "error");
                response.put("message", "找不到會員資料");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // 檢查資料狀態
            boolean isComplete = ProfileValidator.isProfileComplete(member);
            boolean needsCompletion = ProfileValidator.needsProfileCompletion(member);
            
            response.put("status", "success");
            response.put("isComplete", isComplete);
            response.put("needsCompletion", needsCompletion);
            
            // 如果資料不完整,告訴前端缺少哪些資料
            if (!isComplete) {
                Map<String, Boolean> missingFields = new HashMap<>();
                missingFields.put("phone", member.getPhone() == null || member.getPhone().trim().isEmpty());
                missingFields.put("birthDate", member.getBirthDate() == null);
                missingFields.put("name", member.getName() == null || member.getName().trim().isEmpty());
                missingFields.put("nickname", member.getNickname() == null || member.getNickname().trim().isEmpty());
                response.put("missingFields", missingFields);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("檢查資料狀態時發生錯誤：{}", e.getMessage());
            response.put("status", "error");
            response.put("message", "檢查資料狀態時發生錯誤");
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

            if (phone == null || !phone.matches("^09\\d{8}$")) {  // 改為09開頭的手機號碼格式
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
    
    //密碼變更
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> passwordData) {
        Map<String, String> response = new HashMap<>();
        try {
            // 從 Authorization Header 拿 Token
            String token = authHeader.replace("Bearer ", "");
            // 從 Token 拿電子信箱
            String email = jwtUtil.getEmailFromToken(token);            
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            String confirmPassword = passwordData.get("confirmPassword");
            // 驗證新密碼
            if (newPassword == null || !newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")) {
                response.put("error", "新密碼需至少8位，包含大小寫字母、數字和特殊字符");
                return ResponseEntity.badRequest().body(response);
            }
            // 驗證確認密碼
            if (!newPassword.equals(confirmPassword)) {
                response.put("error", "新密碼與確認密碼不一致");
                return ResponseEntity.badRequest().body(response);
            }
            memberService.changePassword(email, currentPassword, newPassword);
            response.put("message", "密碼已成功變更");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("變更密碼失敗：{}", e.getMessage());
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("變更密碼時發生錯誤：{}", e.getMessage());
            response.put("error", "變更密碼失敗，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //忘記密碼
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            logger.info("收到忘記密碼請求");
            String email = payload.get("email");
            
            if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                logger.warn("無效的電子信箱格式：{}", email);
                response.put("error", "請輸入有效的電子信箱");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 檢查email是否存在
            Member member = memberService.findByEmail(email);
            if (member == null) {
                logger.warn("找不到對應的會員帳號：{}", email);
                response.put("message", "如果此電子信箱已註冊，您將收到重設密碼的郵件");
                return ResponseEntity.ok(response);
            }
            
            logger.info("開始生成重設密碼Token");
            String resetToken = memberService.generatePasswordResetToken(email);
            String resetLink = "http://localhost:8080/TickitEasy/reset-password?token=" + resetToken;
            
            try {
                logger.info("準備發送重設密碼郵件");
                emailService.sendPasswordResetEmail(email, resetLink);
                logger.info("重設密碼郵件發送成功");
                
                response.put("message", "重設密碼連結已發送至您的電子信箱");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("發送重設密碼郵件失敗：{}", e.getMessage());
                response.put("error", "發送郵件失敗，請稍後再試");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            logger.error("處理忘記密碼請求時發生錯誤：{}", e.getMessage());
            response.put("error", "發生錯誤，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //忘記密碼重設
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");
            String confirmPassword = payload.get("confirmPassword");
            
            if (newPassword == null || !newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")) {
                response.put("error", "新密碼需至少8位，包含大小寫字母、數字和特殊字符");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (!newPassword.equals(confirmPassword)) {
                response.put("error", "新密碼與確認密碼不一致");
                return ResponseEntity.badRequest().body(response);
            }
            
            memberService.resetPassword(token, newPassword);
            
            response.put("message", "密碼重設成功，請使用新密碼登入");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "發生錯誤，請稍後再試");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}



