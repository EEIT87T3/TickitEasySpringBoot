package com.eeit87t3.tickiteasy.cwdfunding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;

@Controller
@RequestMapping("/member")
public class MemberFundProjViewController {

	@Autowired
	private FundProjService projService;
	
	
	@GetMapping("/fundproject")
	public String showProjPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNumber, @RequestParam(required = false) Integer categoryID, Model model) {
		Integer pageSize = 9;
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber, pageSize, categoryID);
		model.addAttribute("page", page);

		return "cwdfunding/cust_showFundProj";
		
	}
}
