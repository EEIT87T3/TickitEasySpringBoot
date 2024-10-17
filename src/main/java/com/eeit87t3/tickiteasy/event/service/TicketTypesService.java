package com.eeit87t3.tickiteasy.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.event.dto.TicketTypesDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;

import jakarta.transaction.Transactional;

/**
 * @author Chuan(chuan13)
 */
@Service
public class TicketTypesService {

	@Autowired
	private TicketTypesProcessingService ticketTypesProcessingService;
	@Autowired
	private EventsProcessingService eventsProcessingService;
	
	// 查詢
	
	
	public TicketTypesEntity findById(Integer ticketTypeID) {
		return ticketTypesProcessingService.findById(ticketTypeID);
	}
	
	// 新增
	
	/**
	 * 新增票種的輸入驗證。
	 * 
	 * @param createTicketTypesDTO
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateCreateInput(TicketTypesDTO createTicketTypesDTO) {
		
		// 活動編號
		if (createTicketTypesDTO.getEventID() == null) {
			return "輸入錯誤：未輸入活動編號。";
		} else {
			EventsEntity eventsEntity = eventsProcessingService.findById(createTicketTypesDTO.getEventID());
			if (eventsEntity == null) {
				return "輸入錯誤：找不到該筆活動。";
			}
			
			// 可購買限量
			if (createTicketTypesDTO.getQuantityAvailable() != null) {
				if (createTicketTypesDTO.getQuantityAvailable() > eventsEntity.getQuantityTotalAvailable()) {
					return "輸入錯誤：可購買限量超出可購買總量。";
				}
			}
		}
		
		// 票種序號
		// （無限制）
		
		// 票種名稱
		if (createTicketTypesDTO.getTypeName() == null || createTicketTypesDTO.getTypeName().isBlank()) {
			return "輸入錯誤：未輸入票種名稱。";
		}
		
		// 票種說明
		// （無限制）
		
		// 價格
		if (createTicketTypesDTO.getPrice() == null) {
			return "輸入錯誤：未輸入價格。";
		} else if (createTicketTypesDTO.getPrice() < 0) {
			return "輸入錯誤：價格不可小於 0。";
		}
		
		// 開始售票時間
		if (createTicketTypesDTO.getStartSaleTime() == null) {
			return "輸入錯誤：未輸入開始售票時間。";
		}
		
		// 結束售票時間
		if (createTicketTypesDTO.getEndSaleTime() == null) {
			return "輸入錯誤：未輸入結束售票時間。";
		}
		
		
		return "輸入正確！";
	}
	
	
	/**
	 * 新增票種。
	 * 
	 * @param ticketTypesDTO：新增內容。
	 * @return 新增後的 TicketTypesEntity。
	 */
	public TicketTypesEntity create(TicketTypesDTO editTicketTypesDTO) {
		TicketTypesEntity ticketTypesEntity = new TicketTypesEntity();
		
		// 活動編號
		EventsEntity eventsEntity = eventsProcessingService.findById(editTicketTypesDTO.getEventID());
		ticketTypesEntity.setEvent(eventsEntity);
		
		// 票種序號
		if (editTicketTypesDTO.getTicketTypeNo() != null) {
			ticketTypesEntity.setTicketTypeNo(editTicketTypesDTO.getTicketTypeNo());
		}
		
		// 票種名稱
		ticketTypesEntity.setTypeName(editTicketTypesDTO.getTypeName());
		
		// 票種說明
		if (editTicketTypesDTO.getTypeDesc() != null && !editTicketTypesDTO.getTypeDesc().isBlank()) {
			ticketTypesEntity.setTypeDesc(editTicketTypesDTO.getTypeDesc());
		}
		
		// 價格
		ticketTypesEntity.setPrice(editTicketTypesDTO.getPrice());
		
		// 可購買限量
		if (editTicketTypesDTO.getQuantityAvailable() != null) {
			ticketTypesEntity.setQuantityAvailable(editTicketTypesDTO.getQuantityAvailable());
		}
		
		// 開始售票時間
		ticketTypesEntity.setStartSaleTime(editTicketTypesDTO.getStartSaleTime());
		
		// 結束售票時間
		ticketTypesEntity.setEndSaleTime(editTicketTypesDTO.getEndSaleTime());
		
		return ticketTypesProcessingService.save(ticketTypesEntity);
	}
	
	
	// 修改
	
	/**
	 * 編輯票種的輸入驗證。
	 * 
	 * @param editTicketTypesDTO
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateEditInput(TicketTypesDTO editTicketTypesDTO) {
		
		// 票種編號
		if (ticketTypesProcessingService.findById(editTicketTypesDTO.getTicketTypeID()) == null) {
			return "輸入錯誤：此筆票種不存在。";
		}
		
		
		// 活動編號
		if (editTicketTypesDTO.getEventID() == null) {
			return "輸入錯誤：未輸入活動 ID。";
		} else {
			EventsEntity eventsEntity = eventsProcessingService.findById(editTicketTypesDTO.getEventID());
			if (eventsEntity == null) {
				return "輸入錯誤：此筆活動不存在。";
			}
			
			// 可購買限量
			// 應該要做「若已啟售，修改只能變多」驗證，但是暫時擱置
			if (editTicketTypesDTO.getQuantityAvailable() != null) {
				if (editTicketTypesDTO.getQuantityAvailable() > eventsEntity.getQuantityTotalAvailable()) {
					return "輸入錯誤：可購買限量超出可購買總量。";
				}
			}
		}
			
		
		// 票種序號
		// （無限制）
		
		// 票種名稱
		// 應該要做「若已啟售，不能修改票種名稱」驗證，但是暫時擱置
		// （無限制）
		
		// 票種說明
		// （無限制）
		
		// 價格
		// 應該要做「若已啟售，不能修改價格」驗證，但是暫時擱置
		if (editTicketTypesDTO.getPrice() != null &&editTicketTypesDTO.getPrice() < 0) {
			return "輸入錯誤：價格不可小於 0。";
		}
		
		// 開始售票時間
		// （無限制）
		
		// 結束售票時間
		// （無限制）
		
		
		return "輸入正確！";
	}
	
	/**
	 * 編輯票種。
	 * 
	 * @param editTicketTypesDTO：編輯內容。
	 * @return 編輯後的 TicketTypesEntity。
	 */
	@Transactional
	public TicketTypesEntity edit(TicketTypesDTO editTicketTypesDTO) {
		TicketTypesEntity ticketTypesEntity = ticketTypesProcessingService.findById(editTicketTypesDTO.getTicketTypeID());

		// 票種序號
		if (editTicketTypesDTO.getTicketTypeNo() != null) {
			ticketTypesEntity.setTicketTypeNo(editTicketTypesDTO.getTicketTypeNo());
		}
		
		// 票種名稱
		if (editTicketTypesDTO.getTypeName() != null && !editTicketTypesDTO.getTypeName().isBlank()) {
			ticketTypesEntity.setTypeName(editTicketTypesDTO.getTypeName());
		}
		
		// 票種說明
		if (editTicketTypesDTO.getTypeDesc()  != null && !editTicketTypesDTO.getTypeDesc().isBlank()) {
			ticketTypesEntity.setTypeDesc(editTicketTypesDTO.getTypeDesc());
		}
		
		// 價格
		if (editTicketTypesDTO.getPrice() != null) {
			ticketTypesEntity.setPrice(editTicketTypesDTO.getPrice());
		}
		
		// 可購買限量
		if (editTicketTypesDTO.getQuantityAvailable() != null) {
			ticketTypesEntity.setQuantityAvailable(editTicketTypesDTO.getQuantityAvailable());
		}
		
		// 開始售票時間
		if (editTicketTypesDTO.getStartSaleTime() != null) {
			ticketTypesEntity.setStartSaleTime(editTicketTypesDTO.getStartSaleTime());
		}
		
		// 結束售票時間
		if (editTicketTypesDTO.getEndSaleTime() != null) {
			ticketTypesEntity.setEndSaleTime(editTicketTypesDTO.getEndSaleTime());
		}
		
		return ticketTypesEntity;
	}
	
	
	// 刪除
}
