package com.eeit87t3.tickiteasy.event.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.repository.TicketTypesRepo;

import jakarta.transaction.Transactional;

/**
 * @author Chuan(chuan13)
 */
@Service
public class TicketTypesProcessingService {

	@Autowired
	private TicketTypesRepo ticketTypesRepo;
	
	
	// 新增
	@Transactional
	public TicketTypesEntity save(TicketTypesEntity ticketTypesEntity) {
		EventsEntity event = ticketTypesEntity.getEvent();
		if (event.getEarliestStartSaleTime() == null || event.getEarliestStartSaleTime().isBefore(ticketTypesEntity.getStartSaleTime())) {
			event.setEarliestStartSaleTime(ticketTypesEntity.getStartSaleTime());
		}
		
		event.getTicketTypes().add(ticketTypesEntity);
		return ticketTypesRepo.save(ticketTypesEntity);
	}
	
	// 刪除
	public Boolean deleteById(Integer ticketTypeID) {
		ticketTypesRepo.deleteById(ticketTypeID);
		Optional<TicketTypesEntity> result = ticketTypesRepo.findById(ticketTypeID);
		if (result.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// 查詢
	// 原本應該要檢查狀態、切換啟售
	public TicketTypesEntity findById(Integer ticketTypeID) {
		return ticketTypesRepo.findById(ticketTypeID).orElse(null);
	}
}
