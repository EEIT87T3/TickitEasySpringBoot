package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.test.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;


@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;  // 注入 EmailService

    // 顯示註冊頁面
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("memberForm", new Member());
        return "member/memberRegister";
    }
    
 // 處理註冊邏輯
    @PostMapping("/register")
    public String register(
            @RequestParam String email, @RequestParam String password,
            @RequestParam String name, @RequestParam String nickname,
            @RequestParam LocalDate birthDate, @RequestParam String phone,
            Model model) {
        try {
            // 註冊會員，返回生成的驗證 token
            String verificationToken = memberService.register(email, password, name, nickname, birthDate, phone);

            // 發送驗證信件
            String verificationLink = "http://localhost:8080/TickitEasy/member/verify?token=" + verificationToken;
            emailService.sendVerificationEmail(email, verificationLink);

            model.addAttribute("message", "註冊成功！請檢查您的信箱以驗證帳號。");
            return "member/memberRegisterSuccess";  // 顯示註冊成功提示頁面
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/memberRegister";
        }
    }

    // 處理驗證邏輯
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, Model model) {
        try {
            memberService.verifyMember(token);
            model.addAttribute("message", "帳號驗證成功！您現在可以登入。");
            return "member/memberLogin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/memberLogin";
        }
    }


 
 // 顯示會員個人資料頁面
    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/login";  // 如果未登入，跳轉到登入頁面
        }
        model.addAttribute("member", member);  // 將會員資料添加到模型中
        return "member/memberProfile";  // 返回個人資料頁面
    }

    // 處理會員個人資料更新，不包括圖片上傳
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("member") Member updatedMember,
                                HttpSession session, Model model) {
        Member currentMember = (Member) session.getAttribute("member");

        try {
            memberService.updateProfile(currentMember, updatedMember);  // 更新基本資料
            session.setAttribute("member", currentMember);  // 更新 session 中的會員資料
            return "redirect:/member/profile";  // 成功後重定向到個人資料頁面
        } catch (Exception e) {
            model.addAttribute("error", "資料更新失敗：" + e.getMessage());
            return "member/memberProfile";  // 如果有錯誤，返回個人資料頁面並顯示錯誤信息
        }
    }

 // 處理會員頭像上傳
    @PostMapping("/profilePic")
    public String uploadProfilePic(@RequestParam("profilePic") MultipartFile profilePic,
                                   HttpSession session, Model model) {
        Member currentMember = (Member) session.getAttribute("member");

        try {
            memberService.uploadProfilePic(currentMember.getMemberID(), profilePic);  // 處理頭像上傳
            
         // 重新從資料庫中取得更新後的會員資料，確保資料最新
            Optional<Member> updatedMember = memberService.getMemberById(currentMember.getMemberID());

            if (updatedMember.isPresent()) {
                session.setAttribute("member", updatedMember.get());  // 更新 session 中的會員資料
            } else {
                throw new IllegalArgumentException("會員不存在");
            }


            return "redirect:/member/profile";  // 成功後重定向到個人資料頁面
        } catch (IOException e) {
            model.addAttribute("error", "頭像上傳失敗：" + e.getMessage());
            return "member/memberProfile";  // 如果有錯誤，返回個人資料頁面並顯示錯誤信息
        }
    }


}
