package com.eeit87t3.tickiteasy.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.post.entity.CommentEntity;

public interface CommentRepo extends JpaRepository<CommentEntity,  Integer> {
	List<CommentEntity> findByPostPostID(int postID);
}
