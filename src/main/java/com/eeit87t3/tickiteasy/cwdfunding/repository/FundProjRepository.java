package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;



public interface FundProjRepository extends JpaRepository<FundProj, Integer> {
	
	// 查詢目前最新一筆資料的projectID
	@Query(value="select top(1) * from fundingProj order by projectID desc", nativeQuery=true)
	FundProj findTopProjectById();
	
	// 查詢by categoryID
	@Query(value="SELECT * FROM fundingProj WHERE categoryID = :i", nativeQuery=true)
	Page<FundProj> findProjectByCategory(@Param("i") Integer categoryID, Pageable pgb);
}
