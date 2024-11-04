package com.eeit87t3.tickiteasy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eeit87t3.tickiteasy.admin.interceptor.AdminInterceptor;

/**
 * @author Lilian (Curriane)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AdminInterceptor adminInterceptor;

    public WebConfig(AdminInterceptor adminInterceptor) {
        this.adminInterceptor = adminInterceptor;
    }

    /**
     * 註冊 AdminInterceptor。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 註冊 AdminInterceptor，攔截所有 /admin/** 開頭的請求
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**")  // 攔截 /admin 開頭的路徑
                .excludePathPatterns("/admin/login", "/admin/api/login"); // 排除登入頁面
    }
}
