package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundPlanRepository;

@Service
public class FundPlanService {

	@Autowired
	private FundPlanRepository fundPlanRepo;
	
	/* 查詢單筆fund plan */
	public FundPlan findFundPlanById(Integer planID) {
		Optional<FundPlan> optional = fundPlanRepo.findById(planID);
		return optional.get();
	}
}
