package com.eeit87t3.tickiteasy.event.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.event.dto.EventsDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsRepo;
import com.eeit87t3.tickiteasy.event.repository.EventsSpecification;
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
	
	
	public Page<EventsEntity> findByDynamic(Integer pageNumber, String categoryString, String tagString, LocalDateTime searchingTime) {
		Specification<EventsEntity> specification = Specification.where(
				EventsSpecification.hasCategoryString(categoryString)
				.and(EventsSpecification.hasTagString(tagString))
				.and(EventsSpecification.hasSerchingTime(searchingTime))
				);
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Direction.ASC, "eventID");  // （第幾頁（從 0 起算）, 一頁幾筆, 排序方向, 排序依據欄位）
		return eventsFindingService.findBySpecificationAndPageable(specification, pageable);
	}
	
	
	// 新增
	
	/**
	 * 新增活動的輸入驗證。
	 * 
	 * @param createEventsDTO
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateCreateInput(EventsDTO createEventsDTO) {

		// 活動名稱
		if (createEventsDTO.getEventName() != null && !createEventsDTO.getEventName().isBlank()) {  // 未輸入活動名稱
			if (eventsRepo.findByEventName(createEventsDTO.getEventName()) != null) {
				return "輸入錯誤：此活動名稱已經存在。";
			}
		} else {
			return "輸入錯誤：未輸入活動名稱。";
		}
		
		// 活動主圖
		// （無限制）
		
		// 活動類別
		if (createEventsDTO.getCategoryString() != null && !createEventsDTO.getCategoryString().isBlank()) {  // 有輸入活動類別
			if (categoryService.findByCategoryString(createEventsDTO.getCategoryString()) == null) {
				return "輸入錯誤：活動類別不存在。";
			}
		} else {  // 未輸入活動分類
			return "輸入錯誤：未輸入活動類別。";
		}
		
		// 活動標籤
		if (createEventsDTO.getTagString() != null && !createEventsDTO.getTagString().isBlank()) {  // 有輸入活動標籤
			if (tagService.findByTagString(createEventsDTO.getTagString()) == null) {
				return "輸入錯誤：活動標籤不存在。";
			}
		}
		
		// 活動介紹
		// （無限制）
		
		return "輸入正確！";
	}
	
	/**
	 * 新增活動。
	 * 
	 * @param createEventsDTO：新增內容。
	 * @return 新增後的 EventsEntity。
	 */
	@Transactional
	public EventsEntity create(EventsDTO createEventsDTO) {
		EventsEntity eventsEntity = new EventsEntity();
		
		// 活動名稱
		eventsEntity.setEventName(createEventsDTO.getEventName());
		
		// 活動類別
		if (createEventsDTO.getCategoryString() != null && !createEventsDTO.getCategoryString().isBlank()) {  // 有輸入活動類別
			CategoryEntity categoryEntity = categoryService.findByCategoryString(createEventsDTO.getCategoryString());
			if (categoryEntity != null) {					
				eventsEntity.setEventCategory(categoryEntity);
			}
		}
		
		// 活動標籤
		if (createEventsDTO.getTagString() != null && !createEventsDTO.getTagString().isBlank()) {  // 有輸入活動標籤
			TagEntity tagEntity = tagService.findByTagString(createEventsDTO.getTagString());
			if (tagEntity != null) {
				eventsEntity.setEventTag(tagEntity);
			}
		}
		
		// 活動介紹
		if (createEventsDTO.getEventDesc() != null && !createEventsDTO.getEventDesc().isBlank()) {
			eventsEntity.setEventDesc(createEventsDTO.getEventDesc());
		}
		
		eventsRepo.save(eventsEntity);  // 先 persist、取得活動編號
		// 活動主圖
		if (createEventsDTO.getEventPicFile() != null && createEventsDTO.getEventPicFile().getSize() != 0) {  // 有替換活動主圖
			String baseName = eventsEntity.getEventID().toString() + "_" + UUID.randomUUID().toString();
			try {
				String pathString = imageUtil.saveImage(ImageDirectory.EVENT, createEventsDTO.getEventPicFile(), baseName);
				eventsEntity.setEventPic(pathString);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		
		return eventsEntity;
	}
	
	
	// 修改
	
	/**
	 * 編輯活動的輸入驗證。
	 * 
	 * @param editEventsDTO：編輯內容。
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateEditInput(EventsDTO editEventsDTO) {
		// 活動編號
		EventsEntity eventsEntity = eventsFindingService.findById(editEventsDTO.getEventID());
		if (eventsEntity == null) {
			return "輸入錯誤：此筆活動不存在。";
		} else {
			
			// 狀態
			if (editEventsDTO.getStatusString() != null && !editEventsDTO.getStatusString().isBlank()) {  // 有修改狀態
				return "輸入錯誤：無法在此修改狀態。";
			}
			
			// 活動名稱
			if (editEventsDTO.getEventName() != null && !editEventsDTO.getEventName().isBlank()) {  // 有修改活動名稱
				switch (eventsEntity.getStatus()) {
					case 0:  // 原本為「未上架」
					case 1:  // 原本為「已上架」
						if (eventsRepo.findByEventName(editEventsDTO.getEventName()) != null) {
							return "輸入錯誤：此活動名稱已經存在。";
						}
						break;
					case 2:  // 原本為「已啟售」
						return "輸入錯誤：「已啟售」狀態不可修改活動名稱。";
				}
			}
			
			// 活動主圖
			// （無限制）
			
			// 活動類別
			if (editEventsDTO.getCategoryString() != null && !editEventsDTO.getCategoryString().isBlank()) {  // 有輸入活動類別
				if (categoryService.findByCategoryString(editEventsDTO.getCategoryString()) == null) {
					return "輸入錯誤：活動類別不存在。";
				}
			} else {  // 未輸入活動分類
				return "輸入錯誤：未輸入活動類別。";
			}
			
			// 活動標籤
			if (editEventsDTO.getTagString() != null && !editEventsDTO.getTagString().isBlank()) {  // 有輸入活動標籤
				if (tagService.findByTagString(editEventsDTO.getTagString()) == null) {
					return "輸入錯誤：活動標籤不存在。";
				}
			}
			
			// 活動介紹
			// （無限制）
			
			return "輸入正確！";
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
		EventsEntity eventsEntity = eventsFindingService.findById(editEventsDTO.getEventID());
		if (eventsEntity != null) {
			
			// 狀態			
			if (editEventsDTO.getStatusString() != null && !editEventsDTO.getStatusString().isBlank()) {  // 有修改狀態
				switch (editEventsDTO.getStatusString()) {
					case "draft":
						eventsEntity.setStatus((short) 0);
						break;
					case "listing":
						eventsEntity.setStatus((short) 1);
						break;
				}
			}
			
			// 活動名稱
			if (editEventsDTO.getEventName() != null && !editEventsDTO.getEventName().isBlank()) {  // 有修改活動名稱
				eventsEntity.setEventName(editEventsDTO.getEventName());
			}
			
			// 活動主圖
			if (editEventsDTO.getEventPicFile() != null && editEventsDTO.getEventPicFile().getSize() != 0) {  // 有替換活動主圖
				String baseName = eventsEntity.getEventID().toString() + "_" + UUID.randomUUID().toString();
				try {
					String newPathString = imageUtil.saveImage(ImageDirectory.EVENT, editEventsDTO.getEventPicFile(), baseName);
					imageUtil.deleteImage(eventsEntity.getEventPic());
					eventsEntity.setEventPic(newPathString);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
			
			// 活動類別
			if (editEventsDTO.getCategoryString() != null && !editEventsDTO.getCategoryString().isBlank()) {  // 有輸入活動類別
				CategoryEntity categoryEntity = categoryService.findByCategoryString(editEventsDTO.getCategoryString());
				if (categoryEntity != null) {					
					eventsEntity.setEventCategory(categoryEntity);
				}
			}
			
			// 活動標籤
			if (editEventsDTO.getTagString() != null && !editEventsDTO.getTagString().isBlank()) {  // 有輸入活動標籤
				TagEntity tagEntity = tagService.findByTagString(editEventsDTO.getTagString());
				if (tagEntity != null) {
					eventsEntity.setEventTag(tagEntity);
				}
			}
			
			// 活動介紹
			if (editEventsDTO.getEventDesc() != null && !editEventsDTO.getEventDesc().isBlank()) {
				eventsEntity.setEventDesc(editEventsDTO.getEventDesc());
			}
			
			return eventsEntity;
		} else {
			return null;
		}
	}
	
	
	/**
	 * 編輯活動狀態的輸入驗證。
	 * 
	 * @param eventID
	 * @param editStatus：要改為的狀態值。
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateEditStatus(Integer eventID, Short editStatus) {
		// 活動編號
		EventsEntity eventsEntity = eventsFindingService.findById(eventID);
		if (eventsEntity == null) {
			return "輸入錯誤：此筆活動不存在。";
		} else {
			// 狀態
			switch (eventsEntity.getStatus()) {
				case 0:  // 原本為「未上架」
					if (editStatus != 1) {
						return "輸入錯誤：「未上架」狀態只能修改為「已上架」狀態。";
					}
					break;
				case 1:  // 原本為「已上架」
					if (editStatus != 0) {
						return "輸入錯誤：「已上架」狀態只能修改為「未上架」狀態。";
					}
					break;
				case 2:  // 原本為「已啟售」
					return "輸入錯誤：「已啟售」狀態不可修改狀態。";
				default:
					return "輸入錯誤：狀態錯誤。";
			}
		}
		return "輸入正確！";
	}
	
	/**
	 * 編輯活動狀態。
	 * 
	 * @param eventID
	 * @param editStatus：要改為的狀態值。
	 * @return
	 */
	@Transactional
	public EventsEntity editStatus(Integer eventID, Short editStatus) {
		EventsEntity eventsEntity = eventsFindingService.findById(eventID);
		if (eventsEntity != null) {
			eventsEntity.setStatus(editStatus);
		}
		return eventsEntity;
	}
	
	
	// 刪除
	
	/**
	 * 刪除活動的輸入驗證。
	 * 
	 * @param eventID：要刪除的活動之活動編號。
	 * @return 驗證結果：若正確，回傳「輸入正確！」；若錯誤，會包含錯誤訊息。
	 */
	public String validateDeleteInput(Integer eventID) {
		EventsEntity eventsEntity = eventsFindingService.findById(eventID);
		if (eventsEntity == null) {
			return "輸入錯誤：此筆活動不存在。";
		} else {
			if (eventsEntity.getStatus() != 0) {
				return "輸入錯誤：此筆活動為「已上架」或「已啟售」狀態，不可刪除。";
			} else {
				return "輸入正確！";
			}
		}
	}
	
	/**
	 * 刪除活動。
	 * 
	 * @param eventID：要刪除的活動之活動編號。
	 * @return 刪除執行結果。
	 */
	public Boolean delete(Integer eventID) {
		EventsEntity eventsEntity = eventsFindingService.findById(eventID);
		
		// 刪除圖片
		try {
			imageUtil.deleteImage(eventsEntity.getEventPic());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		eventsRepo.delete(eventsEntity);
		EventsEntity result = eventsFindingService.findById(eventID);
		if (result == null) {
			return true;
		} else {
			return false;
		}
	}
}
