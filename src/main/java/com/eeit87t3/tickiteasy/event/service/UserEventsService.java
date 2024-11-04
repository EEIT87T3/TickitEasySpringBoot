package com.eeit87t3.tickiteasy.event.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.event.dto.EventWithTicketTypesDTO;
import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;

import jakarta.transaction.Transactional;

/**
 * @author Chuan (chuan13)
 */
@Service
public class UserEventsService {

	@Autowired
	private EventsProcessingService eventsProcessingService;
	@Autowired
	private TicketTypesProcessingService ticketTypesProcessingService;
	
	/**
	 * 依條件查詢活動。
	 * 
	 * @param eventsSearchingDTO
	 * @return 查詢結果。
	 */
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		eventsSearchingDTO.setStatuses(Arrays.asList((short) 1, (short) 2));  // 前台只能查詢到已上架的活動
		eventsSearchingDTO.setPageSize(12);  // 前台的單頁筆數固定為 12
		return eventsProcessingService.findBySpecification(eventsSearchingDTO);
	}
	
	/**
	 * 依活動編號查詢活動。
	 * 
	 * @param eventID
	 * @return 查詢結果，包含活動資訊與其內的所有票種資訊。
	 */
	public EventWithTicketTypesDTO findById(Integer eventID) {
		return new EventWithTicketTypesDTO(eventsProcessingService.findById(eventID));
	}
	
	public List<EventsEntity> findTopThree() {
		EventsSearchingDTO eventsSearchingDTO = new EventsSearchingDTO();
		eventsSearchingDTO.setSearchingStartTime(LocalDateTime.now());  // 比現在更晚
		eventsSearchingDTO.setSearchingEndTime(LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999999));  // 查詢時間段的結束是無限遠
		eventsSearchingDTO.setOrderByProperty("eventStartTime");  // 以「活動開始時間」排序
		eventsSearchingDTO.setDirectionString("ASC");
		eventsSearchingDTO.setPageSize(3);  // 3 筆
		Page<EventsEntity> topThreePage = eventsProcessingService.findBySpecification(eventsSearchingDTO);
		return topThreePage.getContent();
	}
	
	/**
	 * 訂單成立時，改變資料庫內的已購買數量紀錄。
	 * 
	 * @param ticketTypeID Integer：票種編號
	 * @param quantityChange Integer：要新增／減少的數量
	 * @return
	 */
	@Transactional
	public TicketTypesEntity changeQuantity(Integer ticketTypeID, Integer quantityChange) {
		TicketTypesEntity ticketTypesEntity = ticketTypesProcessingService.findById(ticketTypeID);
		ticketTypesEntity.setQuantityPurchased(ticketTypesEntity.getQuantityPurchased() + quantityChange);
		ticketTypesEntity.getEvent().setQuantityTotalPurchased(ticketTypesEntity.getEvent().getQuantityTotalPurchased() + quantityChange);
		return ticketTypesEntity;
	}
}
