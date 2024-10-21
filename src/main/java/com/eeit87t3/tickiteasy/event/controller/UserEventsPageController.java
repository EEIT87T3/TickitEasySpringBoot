package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserEventsPageController {

	@GetMapping("/event")
	public String findAll() {
		return "event/user/findAll";
	}
}
