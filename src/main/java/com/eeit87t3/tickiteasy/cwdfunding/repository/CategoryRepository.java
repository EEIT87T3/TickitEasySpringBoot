package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.cwdfunding.entity.Category;

/**
 * @author Chuan(chuan13)
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByCategoryStatus(Short categoryStatus);
}
