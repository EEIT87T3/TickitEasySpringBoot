package com.eeit87t3.tickiteasy.cwdfunding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;

/**
 * @author TingXD (chen19990627)
 */
public interface FundProjRepository extends JpaRepository<FundProj, Integer> {
	
	// 查詢目前最新一筆資料的projectID
	@Query(value="select top(1) * from fundingProj order by projectID desc", nativeQuery=true)
	FundProj findTopProjectById();
	
	// 查詢by categoryID
	@Query(value="SELECT * FROM fundingProj WHERE categoryID = :i", nativeQuery=true)
	Page<FundProj> findProjectByCategory(@Param("i") Integer categoryID, Pageable pgb);
	
	// 前台查詢by categoryID
	@Query(value="SELECT * FROM fundingProj WHERE categoryID = :i AND status=1", nativeQuery=true)
	Page<FundProj> findProjectByCategoryAndStatus(@Param("i") Integer categoryID, Pageable pgb);
	
	// 前台查詢All
	@Query(value="SELECT * FROM fundingProj WHERE status=1", nativeQuery=true)
	Page<FundProj> findProjectByStatus(Pageable pgb);
}
