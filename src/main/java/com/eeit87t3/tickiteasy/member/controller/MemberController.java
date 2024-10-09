package com.eeit87t3.tickiteasy.member.controller;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;

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


    // 顯示登入頁面
    @GetMapping("/login")
    public String showLoginPage(Model model) {
    	model.addAttribute("memberForm", new Member()); // 添加 memberForm 到模型中
        return "member/memberLogin";  // 返回登入頁面
    }

    // 處理登入邏輯
    @PostMapping("/login")
    public String login(@ModelAttribute("memberForm") Member memberForm, HttpSession session, Model model) {
        Optional<Member> member = memberService.login(memberForm.getEmail(), memberForm.getPassword());
        if (member.isPresent()) {
            session.setAttribute("member", member.get());
            return "redirect:/member/profile"; // 登入成功後跳轉至會員頁面
        } else {
            model.addAttribute("error", "錯誤的電子郵件或密碼");
            return "member/memberLogin"; // 返回登入頁面，並顯示錯誤信息
        }
    }

 // 顯示註冊頁面
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("memberForm", new Member());  // 初始化一個空的 Member 對象
        return "member/memberRegister";  // 返回註冊頁面
    }

    // 處理註冊邏輯
    @PostMapping("/register")
    public String register(
            @RequestParam String email, @RequestParam String password,
            @RequestParam String name, @RequestParam String nickname,
            @RequestParam LocalDate birthDate, @RequestParam String phone, 
            Model model) {
        try {
            memberService.register(email, password, name, nickname, birthDate, phone);
            return "redirect:/member/login";  // 註冊成功，跳轉至登入頁面
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/memberRegister";  // 註冊失敗，返回註冊頁面
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




    // 處理登出邏輯
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("member");  // 清除 session 中的會員資料
        return "redirect:/member/login";  // 重定向至登入頁面
    }
}
