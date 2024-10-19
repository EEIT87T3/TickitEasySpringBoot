package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.repository.TicketTypesRepo;
import com.eeit87t3.tickiteasy.event.service.EventsService;

@RestController
public class EventTestController {
	
	@Autowired
	private EventsService eventsService;
	@Autowired
	private TicketTypesRepo ticketTypesRepo;

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
}
