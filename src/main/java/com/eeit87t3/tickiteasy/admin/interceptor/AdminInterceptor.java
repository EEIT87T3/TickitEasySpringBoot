package com.eeit87t3.tickiteasy.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 獲取 session
        HttpSession session = request.getSession();

        // 獲取當前的 servlet 路徑
        String servletPath = request.getServletPath();

        // 日誌輸出
        System.out.println("Servlet Path: " + servletPath);
        System.out.println("Admin in session: " + session.getAttribute("admin"));
//
//        // 檢查是否為登入頁面，並且管理員已登入
//        if ("/admin/login".equals(servletPath) && session.getAttribute("admin") != null) {
//            // 已登入管理員訪問登入頁面，重定向到 dashboard
//            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
//            return false; // 中斷請求
//        }

        // 如果 session 中沒有 "admin"，則重定向到登入頁面
        if (session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false; // 中斷請求
        }

        return true; // 已登入，允許請求繼續
    }
}



