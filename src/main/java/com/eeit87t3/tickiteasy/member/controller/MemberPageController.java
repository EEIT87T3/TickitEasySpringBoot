package com.eeit87t3.tickiteasy.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    // 顯示註冊頁面
    @GetMapping("/register")
    public String showRegisterPage() {
        return "member/memberRegister"; 
    }

    // 顯示登入頁面
    @GetMapping("/login")
    public String showLoginPage() {
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
}
