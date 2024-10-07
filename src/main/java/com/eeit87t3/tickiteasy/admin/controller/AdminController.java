package com.eeit87t3.tickiteasy.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.admin.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 顯示管理員登入頁面
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/adminLogin";
    }

    /**
     * 處理管理員登入
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        return adminService.login(email, password)
                .map(admin -> {
                    session.setAttribute("admin", admin);
                    System.out.println("Admin stored in session: " + session.getAttribute("admin")); // 加入此行
                    return "redirect:/admin/dashboard";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "無效的電子郵件或密碼");
                    return "admin/adminLogin";
                });
    }

    /**
     * 處理管理員登出
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	 System.out.println("Admin before logout: " + session.getAttribute("admin")); // 加入此行
    	    session.removeAttribute("admin");
    	    System.out.println("Admin after logout: " + session.getAttribute("admin"));  // 加入此行
        return "redirect:/admin/login?logout=true"; // 添加 query string 表示已成功登出
    }
    /**
     * 顯示管理員儀表板
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin/adminDashboard";
    }

    /**
     * API: 管理員登入
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> apiLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<Admin> adminOptional = adminService.login(email, password);
        
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            session.setAttribute("admin", admin);
            System.out.println("Admin stored in session (API): " + session.getAttribute("admin")); // 加入此行
            return ResponseEntity.ok().body(admin);
        } else {
            return ResponseEntity.badRequest().body("無效的電子信箱或密碼");
        }
    }

    /**
     * API: 獲取當前登入的管理員信息
     */
    @GetMapping("/api/current")
    @ResponseBody
    public ResponseEntity<?> getCurrentAdmin(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(401).body("未登入");
        }
    }
}