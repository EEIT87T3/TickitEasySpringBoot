package com.eeit87t3.tickiteasy.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/TickitEasy")
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";  // 臨時的首頁
    }
}
