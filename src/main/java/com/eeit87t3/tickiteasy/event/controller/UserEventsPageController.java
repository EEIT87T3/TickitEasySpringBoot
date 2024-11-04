package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.event.dto.EventWithTicketTypesDTO;
import com.eeit87t3.tickiteasy.event.service.UserEventsService;

/**
 * @author Chuan (chuan13)
 */
@Controller
public class UserEventsPageController {
	
	@Autowired
	private UserEventsService userEventsService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	
	@Value("${google.cloud.api.key}")
	private String googleCloudApiKey;

	@GetMapping("/event")
	public String findAll(Model model) {
		model.addAttribute("categoryList", categoryService.findEventCategoryList());
		model.addAttribute("tagList", tagService.findEventTagList());
		return "event/user/findAll";
	}
	
	@GetMapping("/event/{eventID}")
	public String findOneById(Model model,
			@PathVariable Integer eventID) {
		EventWithTicketTypesDTO eventWithTicketTypesDTO = userEventsService.findById(eventID);
		model.addAttribute("titleEventName", eventWithTicketTypesDTO.getEvent().getEventName());
		model.addAttribute("googleCloudApiKey", googleCloudApiKey);
		return "event/user/findOneById";
	}
}
