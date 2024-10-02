package com.eeit87t3.tickiteasy.event.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.event.dto.EventsDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsRepo;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;

import jakarta.transaction.Transactional;

/**
 * 活動功能的 Service。
 * 
 * @author Chuan(chuan13)
 */
@Service
public class EventsService {
	
	@Autowired
	private EventsRepo eventsRepo;
	@Autowired
	private EventsFindingService eventsFindingService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private ImageUtil imageUtil;
	
	
	// 查詢
	
	/**
	 * 以活動編號查詢單筆活動、並更新狀態。
	 * 
	 * @param eventID：活動編號。
	 * @return 查詢結果。
	 */
	public EventsEntity findById(Integer eventID) {
		return eventsFindingService.findById(eventID);
	}

	/**
	 * 以活動類別查詢多筆活動、並更新狀態。
	 * 
	 * @param categoryString：活動類別字串。
	 * @return 查詢結果。
	 */
	public List<EventsEntity> findByCategoryString(String categoryString) {
		CategoryEntity categoryEntity = categoryService.findByCategoryString(categoryString);
		if (categoryEntity == null) {
			return new ArrayList<>();
		} else {			
			return eventsFindingService.findByEventCategory(categoryEntity);
		}
	};
	
	/**
	 * 以活動標籤查詢多筆活動、並更新狀態。
	 * 
	 * @param tagString：活動標籤字串。
	 * @return 查詢結果。
	 */
	public List<EventsEntity> findByTagString(String tagString) {
		TagEntity tagEntity = tagService.findByTagString(tagString);
		if (tagEntity == null) {
			return new ArrayList<>();
		} else {			
			return eventsFindingService.findByEventTag(tagEntity);
		}
	}
	
	
	// 修改
	
	public String validateEdit(EventsDTO editEventsDTO) {
		Optional<EventsEntity> optional = eventsRepo.findById(editEventsDTO.getEventID());
		if (optional.isEmpty()) {
			return "輸入錯誤：這筆活動不存在。";
		} else {
			EventsEntity eventsEntity = optional.get();
			switch (eventsEntity.getStatus()) {
				case 0:
					if (editEventsDTO.getStatusString() != null && editEventsDTO.getStatusString() != "listing") {
						return "輸入錯誤：「未上架」狀態只能修改為「已上架」狀態。";
					}
					return "未完成驗證。";
				case 1:
					if (editEventsDTO.getStatusString() != null && editEventsDTO.getStatusString() != "draft") {
						return "輸入錯誤：「已上架」狀態只能修改為「未上架」狀態。";
					}
					return "未完成驗證。";
				case 2:
					if (editEventsDTO.getStatusString() != null) {
						return "輸入錯誤：「已啟售」狀態不可修改狀態。";
					}
					return "未完成驗證。";
				default:
					return "輸入錯誤：狀態錯誤。";
			}
		}
	}
	
	/**
	 * 編輯活動。
	 * 
	 * @param editEventsDTO：編輯內容。
	 * @return 編輯後的 EventsEntity。
	 */
	@Transactional
	public EventsEntity edit(EventsDTO editEventsDTO) {
		Optional<EventsEntity> optional = eventsRepo.findById(editEventsDTO.getEventID());
		if (optional.isPresent()) {
			EventsEntity eventsEntity = optional.get();
			
			if (editEventsDTO.getStatusString() != null) {
				switch (editEventsDTO.getStatusString()) {
					case "draft":
						eventsEntity.setStatus((short) 0);
						break;
					case "listing":
						eventsEntity.setStatus((short) 1);
						break;
				}
			}
			if (editEventsDTO.getEventName() != null) {
				eventsEntity.setEventName(editEventsDTO.getEventName());
			}
			if (editEventsDTO.getEventPicFile() != null && editEventsDTO.getEventPicFile().getSize() != 0) {
				String baseName = eventsEntity.getEventID().toString() + "_" + UUID.randomUUID().toString();
				try {
					String newPathString = imageUtil.saveImage(ImageDirectory.EVENT, editEventsDTO.getEventPicFile(), baseName);
					imageUtil.deleteImage(eventsEntity.getEventPic());
					eventsEntity.setEventPic(newPathString);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
			if (editEventsDTO.getCategoryString() != null) {
				CategoryEntity categoryEntity = categoryService.findByCategoryString(editEventsDTO.getCategoryString());
				if (categoryEntity != null) {					
					eventsEntity.setEventCategory(categoryEntity);
				}
			}
			if (editEventsDTO.getTagString() != null) {
				TagEntity tagEntity = tagService.findByTagString(editEventsDTO.getTagString());
				if (tagEntity != null) {
					eventsEntity.setEventTag(tagEntity);
				}
			}
			if (editEventsDTO.getEventDesc() != null) {
				eventsEntity.setEventDesc(editEventsDTO.getEventDesc());
			}
			
			return eventsEntity;
		} else {
			return null;
		}
	}
}
