package com.eeit87t3.tickiteasy.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.post.entity.ReportEntity;



public interface ReportRepo extends JpaRepository<ReportEntity,  Integer> {

}
