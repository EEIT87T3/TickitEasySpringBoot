package com.eeit87t3.tickiteasy.event.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.repository.EventsRepo;
import com.eeit87t3.tickiteasy.event.repository.TicketTypesRepo;
import com.eeit87t3.tickiteasy.event.service.EventsProcessingService;
import com.eeit87t3.tickiteasy.event.service.AdminEventsService;

@RestController
public class EventTestController {
	
	@Autowired
	private AdminEventsService eventsService;
	@Autowired
	private TicketTypesRepo ticketTypesRepo;
	@Autowired
	private EventsRepo eventsRepo;
	@Autowired
	private EventsProcessingService eventsProcessingService;


	@GetMapping("/eventtest1")
//	@JsonView(EventsJsonView.OnlyEvent.class)
	public EventsEntity situation1(@RequestParam Integer id) {
		return eventsService.findById(id);
	}
	
	@GetMapping("/eventtest2")
//	@JsonView(EventsJsonView.EventWithTicketTypes.class)
	public EventsEntity situation2(@RequestParam Integer id) {
		return eventsService.findById(id);
	}
	
	@GetMapping("/eventtest3")
//	@JsonView(EventsJsonView.EventWithTicketTypes.class)
	public TicketTypesEntity situation3(@RequestParam Integer id) {
		return ticketTypesRepo.findById(id).orElse(null);
	}
	
	@GetMapping("/eventtest4")
	public List<EventsEntity> findByDynamic(@RequestParam(required = false) String eventName,
			@RequestParam(required = false) String categoryString,
			@RequestParam(required = false) LocalDateTime searchingStartTime,
			@RequestParam(required = false) LocalDateTime searchingEndTime) {
		List<Short> userStatusList = Arrays.asList((short) 1, (short) 2);
		
		return eventsRepo.findByDynamic(eventName, userStatusList, categoryString, searchingStartTime, searchingEndTime);
	}
	
//	@GetMapping("/eventtest5")
//	public Page<EventsEntity> findByCriteria(
//			@RequestParam(value = "p", defaultValue = "1") Integer pageNumber,
//			@RequestParam(value = "category-string", required = false) String categoryString,
//			@RequestParam(value = "tag-string", required = false) String tagString,
//			@RequestParam(value = "time-range-start", required = false) LocalDateTime searchingStartTime,
//			@RequestParam(value = "time-range-end", required = false) LocalDateTime searchingEndTime
//			) {
//		List<Short> userStatusList = Arrays.asList((short) 1, (short) 2);
//		return eventsService.findByDynamic(pageNumber, userStatusList, categoryString, tagString, searchingStartTime, searchingEndTime);
//	}
	
	@GetMapping("/eventtest6")
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		return eventsProcessingService.findBySpecification(eventsSearchingDTO);
	}
}
