package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.cwdfunding.entity.Category;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.entity.Tag;
import com.eeit87t3.tickiteasy.cwdfunding.service.CategoryServiceTemp;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.cwdfunding.service.TagServiceTemp;

@Controller
@RequestMapping("/admin")
public class FundProjController {

	@Autowired
	private FundProjService projService;

	@Autowired
	private CategoryServiceTemp categoryServiceTemp;

	@Autowired
	private TagServiceTemp tagServiceTemp;

	/* 查詢所有募資活動頁面, api */
	@GetMapping("/fundproject")
	public String showProjPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		model.addAttribute("page", page);

		return "cwdfunding/showFundProj";
	}

	@ResponseBody
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam Integer pageNumber, Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		return page;
	}
	
	/* [頁面] 查詢單筆募資活動by ID */
	@GetMapping("/fundproject/{projectID}")
	public String showProjPageByID(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		FundProj fundProj = projService.findFundProjById(projectID);
		List<FundPlan> fundPlans = fundProj.getFundPlan();
		
		model.addAttribute("projectDTO",fundProjDTO);
		model.addAttribute("plans",fundPlans);
		return "cwdfunding/showOneFundProj";
	}

	/* [API] 查詢單筆募資活動by ID */
	@ResponseBody
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjDTOById(projectID);
	}
	
//	/* [API] 查詢單筆募資活動方案 by project ID */
//	@ResponseBody
//	@GetMapping("/api/fundproject/planID/{projectID}")
//	public List<FundPlan> findPlanByProjID(@PathVariable Integer projectID){
//		FundProj fundProj = projService.findFundProjById(projectID);
//		List<FundPlan> fundPlans = fundProj.getFundPlan();
//		return fundPlans;
//	}

	/* [API] 刪除募資活動by ID */
	@ResponseBody
	@DeleteMapping("/api/fundproject/{projectID}")
	public ResponseEntity<String> deleteFundProj(@PathVariable Integer projectID) {
		try {
			boolean isDeleted = projService.deleteFundProjById(projectID);
			if (isDeleted) {
				// 刪除成功，返回 204 No Content
				return ResponseEntity.noContent().build();
			} else {
				// 如果沒有找到對應的項目，返回 404

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
			}
		} catch (Exception e) {
			// 如果發生異常，返回 500 內部伺服器錯誤
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}
	
	/* [頁面] 修改單筆募資活動by ID */
	@GetMapping("/fundproject/{projectID}/edit")
	public String showEditProjPageByID(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		FundProj fundProj = projService.findFundProjById(projectID);
		List<FundPlan> fundPlans = fundProj.getFundPlan();
		
		model.addAttribute("projectDTO",fundProjDTO);
		model.addAttribute("plans",fundPlans);
		return "cwdfunding/editOneFundProj";
	}

	/* [頁面] 新增募資活動 */
	@GetMapping("/fundproject/create")
	public String addProjPage(Model model) {
		List<Category> categories = categoryServiceTemp.findFundProjCategoryList();
		List<Tag> tags = tagServiceTemp.findFundProjTagList();
		String topProject = projService.findTopProject();

		model.addAttribute("projectID", topProject);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		return "cwdfunding/addFundProjnPlan";
	}
	
	/* [API]新增募資活動 */
	@ResponseBody
	@PostMapping("/api/fundproject")
	public ResponseEntity<Map<String, Object>> addProject(@RequestParam String projectID, @RequestParam String title,
			@RequestParam String categoryID, @RequestParam String tagID, @RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, @RequestParam String targetAmount,
			@RequestParam String currentAmount, @RequestParam String threshold,
			@RequestParam("postponeDate") String postponeDateStr, @RequestParam MultipartFile image,
			@RequestParam String description, @RequestParam List<String> planTitles,
			@RequestParam List<String> planUnitPrices, @RequestParam List<String> planTotalAmounts,
			@RequestParam List<String> planBuyAmounts, @RequestParam List<MultipartFile> planImages,
			@RequestParam List<String> planContents) {
			// project 變數初始化
			String filename = null;
	
			// plan 變數初始化
			String planTitle = null;
			String planUnitPrice = null;
			String planTotalAmount = null;
			String planBuyAmount = null;
			String planContent = null;
			String planFilename = null;
			
			// response定義
		    Map<String, Object> response = new HashMap<>();

	
			try {
		        // 1. Validate input (basic validation examples)
		        if (title == null || title.isEmpty()) {
		            response.put("status", "error");
		            response.put("message", "Project title is required");
		            return ResponseEntity.badRequest().body(response);
		        }
	
				/*
				 * 1. 存取fund project (1) 取得最新應填的projectID (2) 將image的檔名讀取出來
				 */
				filename = image.getOriginalFilename();
	
				projService.saveProj(title, categoryID, tagID, startDateStr, endDateStr, targetAmount, currentAmount,
						threshold, postponeDateStr, filename, description);
	
				/*
				 * 2. 存取fund plan (1) 取出所有陣列的元素
				 */
				for (int i = 0; i < planTitles.size(); i++) {
					planTitle = planTitles.get(i);
					planUnitPrice = planUnitPrices.get(i);
					planTotalAmount = planTotalAmounts.get(i);
					planBuyAmount = planBuyAmounts.get(i);
					planContent = planContents.get(i);
					planFilename = planImages.get(i).getOriginalFilename();
					System.out.println("projectID" + projectID);
					System.out.println("planTitle: " + planTitle);
					projService.savePlan(projectID, planTitle, planUnitPrice, planTotalAmount, planBuyAmount, planFilename,
							planContent);
				}
				response.put("status", "success");
				response.put("message", "Project added successfully!");
				return ResponseEntity.ok(response);
			}catch (Exception e) {
				response.put("status", "error");
				response.put("message", "Something wrong!");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
	}

	/* [API]修改募資活動 */
	@ResponseBody
	@PutMapping("/api/fundproject/{projectID}")
	public ResponseEntity<Map<String, Object>> editProject(@PathVariable String projectID, @RequestParam String title,
			@RequestParam String categoryID, @RequestParam String tagID, @RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, @RequestParam String targetAmount,
			@RequestParam String currentAmount, @RequestParam String threshold,
			@RequestParam("postponeDate") String postponeDateStr, @RequestParam MultipartFile image,
			@RequestParam String description, @RequestParam List<String> planTitles,
			@RequestParam List<String> planUnitPrices, @RequestParam List<String> planTotalAmounts,
			@RequestParam List<String> planBuyAmounts, @RequestParam List<MultipartFile> planImages,
			@RequestParam List<String> planContents) {
			// project 變數初始化
			String filename = null;
	
			// plan 變數初始化
			String planTitle = null;
			String planUnitPrice = null;
			String planTotalAmount = null;
			String planBuyAmount = null;
			String planContent = null;
			String planFilename = null;
			
			// response定義
		    Map<String, Object> response = new HashMap<>();

	
			try {
		        // 1. Validate input (basic validation examples)
		        if (title == null || title.isEmpty()) {
		            response.put("status", "error");
		            response.put("message", "Project title is required");
		            return ResponseEntity.badRequest().body(response);
		        }
	
				/*
				 * 1. 存取fund project (1) 取得最新應填的projectID (2) 將image的檔名讀取出來
				 */
				filename = image.getOriginalFilename();
	
				projService.editProj(projectID ,title, categoryID, tagID, startDateStr, endDateStr, targetAmount, currentAmount,
						threshold, postponeDateStr, filename, description);
	
				/*
				 * 2. 存取fund plan (1) 取出所有陣列的元素
				 */
				for (int i = 0; i < planTitles.size(); i++) {
					planTitle = planTitles.get(i);
					planUnitPrice = planUnitPrices.get(i);
					planTotalAmount = planTotalAmounts.get(i);
					planBuyAmount = planBuyAmounts.get(i);
					planContent = planContents.get(i);
					planFilename = planImages.get(i).getOriginalFilename();
					System.out.println("projectID" + projectID);
					System.out.println("planTitle: " + planTitle);
					projService.savePlan(projectID, planTitle, planUnitPrice, planTotalAmount, planBuyAmount, planFilename,
							planContent);
				}
				response.put("status", "success");
				response.put("message", "Project added successfully!");
				return ResponseEntity.ok(response);
			}catch (Exception e) {
				response.put("status", "error");
				response.put("message", "Something wrong!");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

	}

}
