package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eeit87t3.tickiteasy.event.dto.EventWithTicketTypesDTO;
import com.eeit87t3.tickiteasy.event.service.UserEventsService;

@Controller
public class UserEventsPageController {
	
	@Autowired
	private UserEventsService userEventsService;

	@GetMapping("/event")
	public String findAll() {
		return "event/user/findAll";
	}
	
	@GetMapping("/event/{eventID}")
	public String findOneById(Model model,
			@PathVariable Integer eventID) {
		EventWithTicketTypesDTO eventWithTicketTypesDTO = userEventsService.findById(eventID);
		model.addAttribute("titleEventName", eventWithTicketTypesDTO.getEvent().getEventName());
		return "event/user/findOneById";
	}
}
