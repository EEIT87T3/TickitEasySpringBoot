package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;

import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;

@RestController
@RequestMapping("/member")
public class MemberFundProjAPIController {

	@Autowired
	private FundProjService projService;

	
	@Autowired
	private ImageUtil imageUtil;
	
	
	/* [API] 查詢募資活動(可以by categoryID或不By categoryID)*/
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam(defaultValue = "1") Integer pageNumber,
			@RequestParam(required = false) Integer categoryID,Model model) {
		Integer pageSize = 9;
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber, pageSize, categoryID);
		return page;
	}

	/* [API] 查詢單筆募資活動by ID */
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjDTOById(projectID);
	}


}
