package com.eeit87t3.tickiteasy.event.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsRepo;
import com.eeit87t3.tickiteasy.image.ImageUtil;

import jakarta.transaction.Transactional;

/**
 * @author Chuan(chuan13)
 */
@Service
public class EventsProcessingService {
	
	@Autowired
	private EventsRepo eventsRepo;
	@Autowired
	private ImageUtil imageUtil;
	
	
	// 新增
	/**
	 * 新增活動。
	 * 
	 * @param eventsEntity：新增內容。
	 * @return 新增後的 EventsEntity。
	 */
	public EventsEntity save(EventsEntity eventsEntity) {
		return eventsRepo.save(eventsEntity);
	}
	
	
	// 刪除
	/**
	 * 先刪除圖片、再刪除活動。
	 * 
	 * @param eventID：活動 ID。
	 * @return 執行結果。
	 */
	public Boolean deleteById(Integer eventID) {
		EventsEntity eventsEntity = findById(eventID);
		
		// 刪除圖片
		try {
			imageUtil.deleteImage(eventsEntity.getEventPic());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		eventsRepo.delete(eventsEntity);
		EventsEntity result = findById(eventID);
		if (result == null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// 查詢
	
	/**
	 * 每次查詢時，根據「最早開始售票時間」確認並更新該活動的狀態。
	 * 
	 * @param eventsEntity
	 * @return 確認並修改後的 EventsEntity。
	 */
	@Transactional
	private EventsEntity updateStatus(EventsEntity eventsEntity) {
		if (eventsEntity.getStatus() == 1) {  // 目前活動狀態為「已上架」
			LocalDateTime now = LocalDateTime.now();

			// 檢查最早售票時間，若已超過就修改狀態為「已啟售」
			if (eventsEntity.getEarliestStartSaleTime() != null && now.isAfter(eventsEntity.getEarliestStartSaleTime())) {
				eventsEntity.setStatus((short) 2);
			}
			
			// 檢查所有票種
			for (TicketTypesEntity ticketTypesEntity : eventsEntity.getTicketTypes()) {
				// 檢查開始售票時間，若已超過就修改狀態為「已啟售」
				if (now.isAfter(ticketTypesEntity.getStartSaleTime())) {
					ticketTypesEntity.setStatus((short) 2);
				}
			}
		}
		return eventsEntity;
	}
	
	
	/**
	 * 以活動編號查詢單筆活動、並更新狀態。
	 * 
	 * @param eventID：活動編號。
	 * @return 查詢結果。
	 */
	public EventsEntity findById(Integer eventID) {
		Optional<EventsEntity> optional = eventsRepo.findById(eventID);
		if (optional.isPresent()) {
			return updateStatus(optional.get());
		} else {
			return null;
		}
	}
	
	/**
	 * 以活動名稱查詢單筆活動、並更新狀態。
	 * 
	 * @param eventName：活動名稱。
	 * @return 查詢結果。
	 */
	public EventsEntity findByEventName(String eventName) {
		EventsEntity result = eventsRepo.findByEventName(eventName);
		if (result != null) {
			return updateStatus(result);
		} else {
			return null;
		}
	}
	
	/**
	 * 以活動類別查詢多筆活動、並更新狀態。
	 * 
	 * @param eventCategory：活動類別。
	 * @return 查詢結果。
	 */
	public List<EventsEntity> findByEventCategory(CategoryEntity eventCategory) {
		List<EventsEntity> eventsList = eventsRepo.findByEventCategory(eventCategory);
		for (EventsEntity eventsEntity : eventsList) {
			updateStatus(eventsEntity);
		}
		return eventsList;
	}
	
	/**
	 * 以活動標籤查詢多筆活動、並更新狀態。
	 * 
	 * @param tagEntity：活動標籤。
	 * @return 查詢結果。
	 */
	public List<EventsEntity> findByEventTag(TagEntity tagEntity) {
		List<EventsEntity> eventsList = eventsRepo.findByEventTag(tagEntity);
		for (EventsEntity eventsEntity : eventsList) {
			updateStatus(eventsEntity);
		}
		return eventsList;
	}

	/**
	 * 以 Pageable 查詢多筆活動、並更新狀態。
	 * 
	 * @param pageable：分頁條件。
	 * @return 查詢結果。
	 */
	public Page<EventsEntity> findByPageable(Pageable pageable) {
		Page<EventsEntity> resultPage = eventsRepo.findAll(pageable);
		for (EventsEntity eventsEntity : resultPage) {
			updateStatus(eventsEntity);
		}
		return resultPage;
	}
	
	/**
	 * 以 EventsSearchingDTO 查詢多筆活動、並更新狀態。
	 * 
	 * @param eventsSearchingDTO：查詢條件。
	 * @return 查詢結果。
	 */
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		Page<EventsEntity> resultPage =  eventsRepo.findAll(eventsSearchingDTO.getSpecification(), eventsSearchingDTO.getPageable());
		for (EventsEntity eventsEntity : resultPage) {
			updateStatus(eventsEntity);
		}
		return resultPage;
	}
	
	public List<EventsEntity> findByListingAndOnsale() {
		List<Integer> statuses = Arrays.asList(1, 2);
		return eventsRepo.findByStatusIn(statuses);
	}
}
