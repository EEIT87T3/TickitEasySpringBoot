package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.event.dto.EventWithTicketTypesDTO;
import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.service.UserEventsService;

@RestController
@RequestMapping("/api/event")
public class UserEventsJsonController {
	
	@Autowired
	private UserEventsService userEventsService;
	
	
	/**
	 * 「查詢全部活動」
	 * 以篩選條件與排序條件查詢全部。
	 * 
	 * @param eventsSearchingDTO
	 * @return 查詢結果。
	 */
	@GetMapping
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		return userEventsService.findBySpecification(eventsSearchingDTO);
	}
	
	/**
	 * 「查詢單筆活動」
	 * 
	 * @param eventID
	 * @return
	 */
	@GetMapping("/{eventID}")
	public EventWithTicketTypesDTO findById(@PathVariable Integer eventID) {
		return userEventsService.findById(eventID);
	}
}
