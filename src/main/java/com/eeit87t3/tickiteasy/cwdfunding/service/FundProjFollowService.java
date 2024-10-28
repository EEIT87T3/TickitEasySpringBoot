package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjFollow;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjFollowRepository;

@Service
public class FundProjFollowService {

	@Autowired
	private FundProjFollowRepository fundProjFollowRepository;
	
	// 新增 會員追蹤專案
	public FundProjFollow createFundProjFollow(FundProjFollow fundProjFollow) {
		/* 取得目前時間 */
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+8"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Timestamp nowTimestamp = Timestamp.valueOf(now.format(formatter));		
		
		fundProjFollow.setFollowDate(nowTimestamp);
		
		return fundProjFollowRepository.save(fundProjFollow);
	}
	
	// 根據memberID查詢所有追蹤專案	
	public List<FundProjFollow> findByFundProjFollowPKMemberID(Integer memberID){
		return fundProjFollowRepository.findByFundProjFollowPKMemberID(memberID);
	}
	
	// 根據projectID查詢所有會員
	public List<FundProjFollow> findByFundProjFollowPKProjectID(Integer projectID){
		return fundProjFollowRepository.findByFundProjFollowPKProjectID(projectID);
	}
	
}
