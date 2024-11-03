package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundOrder;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundOrderService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundPlanService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;

@Controller
public class UserFundProjViewController {

	@Autowired
	private FundProjService projService;
	
	@Autowired
	private FundPlanService planService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired FundOrderService orderService;
	
	@GetMapping("/fundprojects")
	public String showProjPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNumber, @RequestParam(required = false) Integer categoryID, Model model) {
		Integer pageSize = 9;
		Page<FundProjDTO> page = projService.findFundProjByPageAndStatus(pageNumber, pageSize, categoryID);
		List<CategoryEntity> categories = categoryService.findFundProjCategoryList();
		List<TagEntity> tags = tagService.findFundProjTagList();

		model.addAttribute("page", page);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		model.addAttribute("selectedCategory",categoryID != null ? categoryID : 0);

		return "cwdfunding/cust_showFundProj";
		
	}
	
	@GetMapping("/fundprojects/{projectID}")
	public String showOneProjPage(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		
		model.addAttribute("fundProjDTO",fundProjDTO);
		
		return "cwdfunding/cust_showOneFundProj";
	}
	
	@GetMapping("/fundprojects/payment/{planID}")
	public String paymentPage(@PathVariable Integer planID, Model model) {
		
		FundPlan fundPlan = planService.findFundPlanById(planID);
		
		model.addAttribute("fundPlan",fundPlan);
		
		return "cwdfunding/cust_payPage";
	}
	
	@GetMapping("/user/fundorderlist")
	public String fundOrderList(Model model) {
		return "cwdfunding/cust_fundOrderList";
	}
	
	@GetMapping("/user/fundOrderDetail/{tickitID}")
	public String fundOrderDetail(@PathVariable String tickitID, Model model) {
		FundOrder fundOrder = orderService.findFundOrderByTickitID(tickitID);
		model.addAttribute("order",fundOrder);
		return "cwdfunding/cust_fundOrderDetail";
	}
	
	
	@GetMapping("/user/fundproject/follow")
	public String fundProjFollow() {
		return "cwdfunding/cust_fundProjFollow";
	}
	
	// line pay畫面，使用者同意付款會導向至這裡
	@GetMapping("/fundproject/linepay/OK")
	public String linepayReqAcc() {
		return "cwdfunding/linepayOK";
	}
	
	// line pay畫面，使用者取消付款會導向至這裡
	@GetMapping("/fundproject/linepay/NO")
	public String linepayReqReject() {
		return "cwdfunding/linepayNO";
	}
	
	
}
