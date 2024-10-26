package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.service.AdminEventsService;

/**
 * @author Chuan(chuan13)
 */
@Controller
@RequestMapping("/admin/event")
public class AdminEventsPageController {
	
	@Autowired
	private AdminEventsService eventsService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;

	@GetMapping
	public String findAllByDynamic() {
		return "event/findAllByDynamic";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("categoryList", categoryService.findEventCategoryList());
		model.addAttribute("tagList", tagService.findEventTagList());
		return "event/createEvent";
	}
	
	@GetMapping("/{id}")
	public String findOneByEventId(Model model,
			@PathVariable(value = "id") Integer eventID) {
		EventsEntity eventsEntity = eventsService.findById(eventID);
		if (eventsEntity != null) {
			model.addAttribute("event", eventsEntity);
		} else {
			model.addAttribute("error", true);
		}
		return "event/findOneByEventId";
	}
	
	@GetMapping("/{id}/edit")
	public String editByEventId(Model model,
			@PathVariable(value = "id") Integer eventID) {
		EventsEntity eventsEntity = eventsService.findById(eventID);
		if (eventsEntity != null) {
			model.addAttribute("event", eventsEntity);
			model.addAttribute("categoryList", categoryService.findEventCategoryList());
			model.addAttribute("tagList", tagService.findEventTagList());
		} else {
			model.addAttribute("error", true);
		}
		return "event/editByEventId";
	}
}
