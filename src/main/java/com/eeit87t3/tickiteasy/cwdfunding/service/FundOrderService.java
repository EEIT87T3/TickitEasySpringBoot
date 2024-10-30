package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundOrder;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundOrderRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundPlanRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjRepository;
import com.eeit87t3.tickiteasy.cwdfunding.testemail.TestEmailService;

@Service
public class FundOrderService {

	@Autowired
	private FundOrderRepository fundOrderRepository;
	
	@Autowired
	private FundProjRepository fundProjRepository;
	
	@Autowired
	private FundPlanRepository fundPlanRepository;
	

	
	/* 查詢所有募資訂單 */
	public Page<FundOrder> findFundOrderByPage(Integer pageNumber, Integer size){
		Pageable pgb = PageRequest.of(pageNumber-1, size,Sort.Direction.ASC,"orderID");
		return fundOrderRepository.findAll(pgb);
	}
	
	/* 查詢募資訂單by Tickit ID */
	public FundOrder findFundOrderByTickitID(String tickitID) {
		return fundOrderRepository.findByTickitID(tickitID);
	}
	
	/* 新增募資訂單 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean saveFundOrder(Map<String, Object> form,  Map<String, Object> fullForm) {
		
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
		String memberID = form.get("memberID").toString();
		String totalAmountString = form.get("amount").toString();
		String projectIDString =   form.get("projectID").toString();
		String planIDString =  form.get("planID").toString();
		
		
		/* 從fullForm取出資料 */
		String tickitID = fullForm.get("orderId").toString();
		
		/* 取得目前時間 */
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+8"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Timestamp nowTimestamp = Timestamp.valueOf(now.format(formatter));		
		
		/* 將fundOrder實體存進資料庫 */
		FundOrder fundOrder = new FundOrder();
		fundOrder.setMemberID(Integer.parseInt(memberID));
		fundOrder.setFundProj(fundProjRepository.findById(Integer.parseInt(projectIDString)).get());
		fundOrder.setFundPlan(fundPlanRepository.findById(Integer.parseInt(planIDString)).get());
		fundOrder.setBonus(Integer.parseInt(bonusString));
		fundOrder.setTotalAmount(Integer.parseInt(totalAmountString));
		fundOrder.setTickitID(tickitID);
		fundOrder.setOrderDate(nowTimestamp);
		
		
		try {
			fundOrderRepository.saveAndFlush(fundOrder);
			
		    // 更新FundProj的currentAmount
	        FundProj fundProj = fundProjRepository.findById(Integer.parseInt(projectIDString)).get();
	        Integer newCurrentAmount = Integer.parseInt(fundProj.getCurrentAmount()) + Integer.parseInt(totalAmountString);
	        fundProj.setCurrentAmount(newCurrentAmount.toString()); // 更新金額
	        fundProjRepository.save(fundProj); // 儲存更新
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isReached(Integer projectID) {
		Optional<FundProj> optional = fundProjRepository.findById(projectID);
		FundProj fundProj = optional.get();
		
		if (Integer.parseInt(fundProj.getCurrentAmount()) >= Integer.parseInt(fundProj.getTargetAmount())) {
			return true;
		}else {
			return false;
		}
	}

	/* 查詢募資訂單by member ID */
	public List<FundOrder> findFundOrderByMember(Integer memberID) {
		return fundOrderRepository.findByMemberID(memberID);
	}
	
	/* 查詢贊助過的會員ID */
	public List<Integer> findMemberIDByProjID(Integer projectID){
		return fundOrderRepository.memberIDList(projectID);
	}

	/* 查詢會員是否贊助過方案 */
	public boolean isDonated(Integer projectID, Integer memberID) {
		FundOrder fundOrder = fundOrderRepository.isDonated(projectID, memberID);
		if(fundOrder == null) {
			return false;
		}else {
			return true;
		}
	}
}
