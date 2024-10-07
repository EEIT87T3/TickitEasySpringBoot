package com.eeit87t3.tickiteasy.admin.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.admin.repository.AdminRepo;


@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理員登入
     * @param email 電子郵件
     * @param password 密碼
     * @return 如果登入成功，返回Admin對象；否則返回空
     */
    public Optional<Admin> login(String email, String password) {
        return adminRepo.findByEmail(email)
                .filter(admin -> passwordEncoder.matches(password, admin.getPassword()));
    }

    /**
     * 根據ID獲取管理員
     * @param id 管理員ID
     * @return 管理員對象（如果存在）
     */
    public Optional<Admin> getAdminById(Integer id) {
        return adminRepo.findById(id);
    }
}