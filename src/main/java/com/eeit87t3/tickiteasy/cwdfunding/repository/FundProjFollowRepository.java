package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjFollow;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjFollow.FundProjFollowPK;

public interface FundProjFollowRepository extends JpaRepository<FundProjFollow, FundProjFollowPK> {

	// 根據memberID查詢所有追蹤專案	
	List<FundProjFollow> findByFundProjFollowPKMemberID(Integer memberID);
	
	// 根據projectID查詢所有會員
	List<FundProjFollow> findByFundProjFollowPKProjectID(Integer projectID);
	
}
