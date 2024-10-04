package com.eeit87t3.tickiteasy.cwdfunding.repository;

import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;



public interface FundProjRepository extends JpaRepository<FundProj, Integer> {
	
	@Query(value="select top(1) * from fundingProj order by projectID desc", nativeQuery=true)
	FundProj findTopProjectById();
}
