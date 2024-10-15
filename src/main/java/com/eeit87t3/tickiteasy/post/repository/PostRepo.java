package com.eeit87t3.tickiteasy.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;


public interface PostRepo extends JpaRepository<PostEntity, Integer> {
	@Query("SELECT p FROM PostEntity p JOIN FETCH p.postCategory JOIN FETCH p.postTag JOIN FETCH p.member")
	List<PostEntity> findAllPostsWithDetails();
	
	List<PostEntity> findByPostCategory(CategoryEntity postCategory);
//	List<PostEntity> findByTag(TagEntity tag);
//	List<PostEntity> findByEnter(String enter);
}
