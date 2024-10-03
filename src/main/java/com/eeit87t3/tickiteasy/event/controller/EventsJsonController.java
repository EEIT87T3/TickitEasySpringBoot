package com.eeit87t3.tickiteasy.event.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.event.dto.EventsDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.service.EventsService;

/**
 * @author Chuan(chuan13)
 */
@RestController
@RequestMapping("/admin/api/event")
public class EventsJsonController {
	
	@Autowired
	private EventsService eventsService;


	@GetMapping
	public List<EventsEntity> findByCategoryOrTagString(
			@RequestParam(value = "category-string", required = false) String categoryString,
			@RequestParam(value = "tag-string", required = false) String tagString) {
		if (categoryString != null && tagString == null) {
			return eventsService.findByCategoryString(categoryString);
		} else if (categoryString == null && tagString != null) {
			return eventsService.findByTagString(tagString);
		} else {
			EventsEntity eventsEntity = new EventsEntity();
			eventsEntity.setEventName("暫時訊息：else");
			List<EventsEntity> list = new ArrayList<>();
			list.add(eventsEntity);
			return list;
		}
	}
	
	@GetMapping("/{eventID}")
	public EventsEntity findById(@PathVariable Integer eventID) {
		return eventsService.findById(eventID);
	}
	
	@PutMapping("/{eventID}")
	public EventsEntity edit(@ModelAttribute EventsDTO editEventsDTO) {
		// 輸入驗證：422 Unprocessable Entity
		return eventsService.edit(editEventsDTO);
	}
}
