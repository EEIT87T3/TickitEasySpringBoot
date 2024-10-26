package com.eeit87t3.tickiteasy.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eeit87t3.tickiteasy.member.service.MemberService;

@Controller
@RequestMapping
public class MemberPageController {
	
	@Autowired
    private MemberService memberService;

	
	@Value("${oauth.google.client-id}")
	private String googleClientId;
	
	@Value("${google.recaptcha.site-key}")
	private String recaptchaSiteKey;
	
    // 顯示註冊頁面
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
    	model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "member/memberRegister"; 
    }

    // 顯示登入頁面
    @GetMapping("/login")
    public String showLoginPage(Model model) {
    	model.addAttribute("googleClientId", googleClientId);
    	model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "member/memberLogin";
    }

    // 顯示驗證成功頁面
    @GetMapping("/registerSuccess")
    public String showVerifySuccessPage() {
        return "member/memberRegisterSuccess";
    }

    // 顯示會員個人資料頁面
    @GetMapping("/profile")
    public String showProfilePage() {
        return "member/memberProfile";
    }

    // 會員認證
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        try {
            memberService.verifyMember(token);
            // 把驗證結果傳到前端
            model.addAttribute("status", "success");
            model.addAttribute("message", "帳號驗證成功！您現在可以進行登入。");
        } catch (IllegalArgumentException e) {
            // 將驗證失敗的結果傳到前端
            model.addAttribute("status", "error");
            model.addAttribute("message", e.getMessage());
        }
        // 返回驗證頁面
        return "member/verify";
    }
    
    //完善個人資料
    @GetMapping("/complete-profile")
    public String showCompleteProfilePage() {
        return "member/completeProfile";
    }
    
    //偽隱私政策
    @GetMapping("/privacy-policy")
    public String showPrivacyPolicy() {
        return "policy/privacyPolicy";
    }
    //偽服務條款
    @GetMapping("/terms-of-service")
    public String showTermsOfService() {
        return "policy/termsOfService";
    }
    
    //忘記密碼
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "member/forgotPassword";
    }
    //忘記密碼重設
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        try {
            // 檢查 token 是否有效
            if (memberService.isValidResetToken(token)) {
                model.addAttribute("token", token);
                return "member/resetPassword";
            } else {
                model.addAttribute("error", "無效的重設密碼連結或連結已過期");
                return "member/resetPasswordError";
            }
        } catch (Exception e) {
            model.addAttribute("error", "發生錯誤，請稍後再試");
            return "member/resetPasswordError";
        }
    }

}
