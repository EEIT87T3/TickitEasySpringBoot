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
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;

@Controller
@RequestMapping("/admin/fundproject")
public class AdminFundProjViewController {

	@Autowired
	private FundProjService projService;

	@Autowired
	private FundOrderService orderService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TagService tagService;


	/* [頁面] 查詢所有募資活動*/
	@GetMapping
	public String showProjPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNumber, @RequestParam(required = false) Integer categoryID, Model model) {
		Integer pageSize = 10;
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber,pageSize,categoryID);
		model.addAttribute("page", page);

		return "cwdfunding/showFundProj";
	}
	
	/* [頁面] 查詢單筆募資活動by ID */
	@GetMapping("/{projectID}")
	public String showProjPageByID(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		FundProj fundProj = projService.findFundProjById(projectID);
		List<FundPlan> fundPlans = fundProj.getFundPlan();
		
		model.addAttribute("projectDTO",fundProjDTO);
		model.addAttribute("plans",fundPlans);
		return "cwdfunding/showOneFundProj";
	}
	
	/* [頁面] 查詢所有訂單 */
	@GetMapping("/order")
	public String showFundOrderPage() {
//		Integer pageSize = 10;
//		Page<FundOrder> page = orderService.findFundOrderByPage(pageNumber,pageSize);
//		model.addAttribute("page", page);

		return "cwdfunding/showFundOrder";
	}	
	
	/* [頁面] 新增募資活動 */
	@GetMapping("/create")
	public String addProjPage(Model model) {
		List<CategoryEntity> categories = categoryService.findFundProjCategoryList();
		List<TagEntity> tags = tagService.findFundProjTagList();
		//String topProject = projService.findTopProject();

		//model.addAttribute("projectID", topProject);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		return "cwdfunding/addFundProjnPlan";
	}
	
	/* [頁面] 修改單筆募資活動by ID */
	@GetMapping("/{projectID}/edit")
	public String showEditProjPageByID(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		FundProj fundProj = projService.findFundProjById(projectID);
		List<FundPlan> fundPlans = fundProj.getFundPlan();
		List<CategoryEntity> categories = categoryService.findFundProjCategoryList();
		List<TagEntity> tags = tagService.findFundProjTagList();
		
		model.addAttribute("projectDTO",fundProjDTO);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		model.addAttribute("plans",fundPlans);
		return "cwdfunding/editOneFundProj";
	}
	

}
