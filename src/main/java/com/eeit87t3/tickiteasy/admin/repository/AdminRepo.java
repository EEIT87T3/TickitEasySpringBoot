package com.eeit87t3.tickiteasy.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.admin.entity.Admin;


/**
 * @author Lilian (Curriane)
 */
public interface AdminRepo extends JpaRepository<Admin, Integer> {
	 Optional<Admin> findByEmail(String email);
}
