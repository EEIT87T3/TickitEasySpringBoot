package com.eeit87t3.tickiteasy.event.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsRepo;

import jakarta.transaction.Transactional;

/**
 * @author Chuan(chuan13)
 */
@Service
public class EventsFindingService {
	
	@Autowired
	private EventsRepo eventsRepo;
	
	/**
	 * 每次查詢時，根據「最早開始售票時間」確認並更新該活動的狀態。
	 * 
	 * @param eventsEntity
	 * @return 確認並修改後的 EventsEntity。
	 */
	@Transactional
	private EventsEntity updateStatus(EventsEntity eventsEntity) {
		if (eventsEntity.getStatus() == 1) {
			LocalDateTime now = LocalDateTime.now();
			if (eventsEntity.getEarliestStartSaleTime() != null && now.isAfter(eventsEntity.getEarliestStartSaleTime())) {
				eventsEntity.setStatus((short) 2);
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

}
