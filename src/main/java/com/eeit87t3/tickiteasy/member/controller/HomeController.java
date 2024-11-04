package com.eeit87t3.tickiteasy.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeit87t3.tickiteasy.event.service.UserEventsService;

/**
 * @author Lilian (Curriane), Chuan (chuan13)
 */
@Controller
@RequestMapping
public class HomeController {
	
	@Autowired
	private UserEventsService userEventsService;

    @GetMapping
    public String showHomePage(Model model) {
    	model.addAttribute("topEvents", userEventsService.findTopThree());
        return "index";
    }
}
