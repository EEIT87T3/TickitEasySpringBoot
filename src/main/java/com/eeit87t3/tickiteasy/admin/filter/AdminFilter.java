package com.eeit87t3.tickiteasy.admin.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminFilter implements Filter {
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 獲取請求的 URI
        String requestURI = httpRequest.getRequestURI();

        // 檢查是否是管理員 API 請求（除了登入請求）
        if (requestURI.startsWith("/admin") && !requestURI.equals("/admin/login") && !requestURI.equals("/admin/api/login")) {
            // 檢查 session 是否存在管理員資訊
            if (session == null || session.getAttribute("admin") == null) {
                // 如果是 API 請求，返回 401 未授權狀態
                if (requestURI.startsWith("/admin/api/")) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else {
                    // 如果是頁面請求，重定向到登入頁面
                    httpResponse.sendRedirect("/admin/login");
                    return;
                }
            }
        }

        // 如果是已登入的用戶並且訪問登入頁面，重定向到 dashboard
        if (requestURI.equals("/admin/login") && session != null && session.getAttribute("admin") != null) {
            httpResponse.sendRedirect("/admin/dashboard");
            return;
        }

        // 如果通過驗證或不需要驗證，繼續請求
        chain.doFilter(request, response);
    }

    // 初始化方法
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    // 銷毀方法
    @Override
    public void destroy() {
    }
}
