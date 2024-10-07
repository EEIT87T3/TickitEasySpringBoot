package com.eeit87t3.tickiteasy.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eeit87t3.tickiteasy.admin.filter.AdminFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public FilterRegistrationBean<AdminFilter> adminAuthFilter() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new AdminFilter());
        // 添加 Filter 的 URL 模式
        registrationBean.addUrlPatterns("/admin/*");
        // 設置 Filter 的順序
        registrationBean.setOrder(1);
  
        return registrationBean;
    }
    
    
    
}