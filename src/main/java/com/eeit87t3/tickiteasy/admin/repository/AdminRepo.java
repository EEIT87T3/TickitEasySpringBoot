package com.eeit87t3.tickiteasy.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.admin.entity.AdminBean;

public interface AdminRepo extends JpaRepository<AdminBean, Integer> {

}
