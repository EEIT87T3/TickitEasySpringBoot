package com.eeit87t3.tickiteasy.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;


public interface PostRepo extends JpaRepository<PostEntity, Integer> {

	List<PostEntity> findByPostCategory(CategoryEntity postCategory);
//	List<PostEntity> findByTag(TagEntity tag);
//	List<PostEntity> findByEnter(String enter);
}
