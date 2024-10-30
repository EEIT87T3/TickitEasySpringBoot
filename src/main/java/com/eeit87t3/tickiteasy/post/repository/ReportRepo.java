package com.eeit87t3.tickiteasy.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.ReportEntity;



public interface ReportRepo extends JpaRepository<ReportEntity,  Integer> {
	 List<ReportEntity> findByPostAndIsDeleted(PostEntity post, boolean isDeleted);
}
