package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundOrder;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundOrderRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundPlanRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjRepository;

@Service
public class FundOrderService {

	@Autowired
	private FundOrderRepository fundOrderRepository;
	
	@Autowired
	private FundProjRepository fundProjRepository;
	
	@Autowired
	private FundPlanRepository fundPlanRepository;
	
	public void saveFundOrder(Map<String, Object> form,  Map<String, Object> fullForm) {
		
		/* 從form取出資料 */
		
		List<Map<String, Object>> packages = (List<Map<String, Object>>) form.get("packages");
		String bonusString = null;
		
		for (Map<String, Object> packageItem : packages) {
		    if ("2".equals(packageItem.get("id"))) { 
		        List<Map<String, Object>> products = (List<Map<String, Object>>) packageItem.get("products");
		        
		        if (!products.isEmpty()) {
		        	bonusString = products.get(0).get("price").toString();
		        }
		        break; 
		    }
		}
		
		String totalAmountString = form.get("amount").toString();
		String projectIDString =   form.get("projectID").toString();
		String planIDString =  form.get("planID").toString();
		
		
		/* 從fullForm取出資料 */
		
		String tickitID = fullForm.get("orderId").toString();
		
		FundOrder fundOrder = new FundOrder();
		fundOrder.setFundProj(fundProjRepository.findById(Integer.parseInt(projectIDString)).get());
		fundOrder.setFundPlan(fundPlanRepository.findById(Integer.parseInt(planIDString)).get());
		fundOrder.setBonus(Integer.parseInt(bonusString));
		fundOrder.setTotalAmount(Integer.parseInt(totalAmountString));
		fundOrder.setTickitID(tickitID);
		
		fundOrderRepository.save(fundOrder);
	}
}
