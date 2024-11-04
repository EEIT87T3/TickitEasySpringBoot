package com.eeit87t3.tickiteasy.categoryandtag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;

/**
 * @author Chuan (chuan13)
 */
public interface TagRepo extends JpaRepository<TagEntity, Integer> {

	List<TagEntity> findByTagStatus(Short tagStatus);
	
	TagEntity findByTagString(String tagString);
}
