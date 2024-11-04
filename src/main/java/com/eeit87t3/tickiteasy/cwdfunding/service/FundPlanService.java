package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlanDTO;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundPlanRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjRepository;

/**
 * @author TingXD (chen19990627)
 */
@Service
public class FundPlanService {

	@Autowired
	private FundPlanRepository fundPlanRepo;
	
	@Autowired
	private FundProjRepository fundProjRepo;
	
	/* 查詢單筆fund plan */
	public FundPlan findFundPlanById(Integer planID) {
		Optional<FundPlan> optional = fundPlanRepo.findById(planID);
		return optional.get();
	}
	
	/* 新增募資活動方案 */
	public FundPlan savePlan(FundPlanDTO fundPlanDTO) {
		FundPlan newFundPlan = new FundPlan();
		FundProj fundProj = new FundProj();
		fundProj = null;
		//先用projectID找出對應到的FundProj實體，之後再塞進plan
		try {
			System.out.println("傳進來的dto projectID:"+fundPlanDTO.getProjectID());
			Optional<FundProj> optional = fundProjRepo.findById(fundPlanDTO.getProjectID());
			 if (optional.isPresent()) {			 
				 fundProj = optional.get();
				 System.out.println("fundProj的ID"+fundProj.getProjectID());
			 }
			 else {
				 System.out.println("沒有這個專案！");
			 }
		} catch (Exception e) {
		}

		
        // 設定FundPlan屬性
		newFundPlan.setFundProj(fundProj);
		newFundPlan.setPlanTitle(fundPlanDTO.getPlanTitle());
		newFundPlan.setPlanUnitPrice(fundPlanDTO.getPlanUnitPrice());
		newFundPlan.setPlanTotalAmount(fundPlanDTO.getPlanTotalAmount());
		newFundPlan.setPlanBuyAmount(fundPlanDTO.getPlanBuyAmount());
		newFundPlan.setPlanImage(fundPlanDTO.getPlanImage());
		newFundPlan.setPlanContent(fundPlanDTO.getPlanContent());
		
		return fundPlanRepo.save(newFundPlan);
	}
}
