package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjFollow;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjFollow.FundProjFollowPK;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjFollowRepository;

/**
 * @author TingXD (chen19990627)
 */
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
	// 刪除 會員追蹤專案
	public boolean deleteFundProjFollow(FundProjFollowPK fundProjFollowPK) {
		Optional<FundProjFollow> optional = fundProjFollowRepository.findById(fundProjFollowPK);
		if(optional.isEmpty()) {
			return false;
		} else {
			FundProjFollow fundProjFollow = optional.get();
			fundProjFollowRepository.delete(fundProjFollow);
			return true;
		}
	}
	
	// 根據memberID查詢所有追蹤專案	
	public List<FundProjFollow> findByFundProjFollowPKMemberID(Integer memberID){
		return fundProjFollowRepository.findByFundProjFollowPKMemberID(memberID);
	}
	
	// 根據projectID查詢所有會員
	public List<FundProjFollow> findByFundProjFollowPKProjectID(Integer projectID){
		return fundProjFollowRepository.findByFundProjFollowPKProjectID(projectID);
	}
	
	// 根據memberID & projectID 查詢會員是否有追蹤
	public boolean isFollowing(FundProjFollowPK fundProjFollowPK) {
		 Optional<FundProjFollow> optional = fundProjFollowRepository.findById(fundProjFollowPK);
		 if(optional.isEmpty()) {
			 return false;
		 }else {
			 return true;
		 }
	}
}
