package com.eeit87t3.tickiteasy.categoryandtag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;

/**
 * @author Chuan(chuan13)
 */
public interface CategoryRepo extends JpaRepository<CategoryEntity, Integer> {

	List<CategoryEntity> findByCategoryStatus(Short categoryStatus);
	
	CategoryEntity findByCategoryString(String categoryString);
}
