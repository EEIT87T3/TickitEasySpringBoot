package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.cwdfunding.entity.Category;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.entity.Tag;
import com.eeit87t3.tickiteasy.cwdfunding.service.CategoryServiceTemp;
import com.eeit87t3.tickiteasy.cwdfunding.service.FileService;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;
import com.eeit87t3.tickiteasy.cwdfunding.service.TagServiceTemp;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.test.TestImagesEntity;
import com.eeit87t3.tickiteasy.test.TestImagesRepo;

@Controller
@RequestMapping("/admin")
public class FundProjController {

	@Autowired
	private FundProjService projService;

	@Autowired
	private CategoryServiceTemp categoryServiceTemp;

	@Autowired
	private TagServiceTemp tagServiceTemp;
	
	@Autowired
	private ImageUtil imageUtil;
	
	@Autowired
	private TestImagesRepo testImagesRepo;
	
	@Autowired
	private FileService fileService;

	/* [頁面] 查詢所有募資活動*/
	@GetMapping("/fundproject")
	public String showProjPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNumber, Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		model.addAttribute("page", page);

		return "cwdfunding/showFundProj";
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
	
	/* [頁面] 修改單筆募資活動by ID */
	@GetMapping("/fundproject/{projectID}/edit")
	public String showEditProjPageByID(@PathVariable Integer projectID, Model model) {
		FundProjDTO fundProjDTO = projService.findFundProjDTOById(projectID);
		FundProj fundProj = projService.findFundProjById(projectID);
		List<FundPlan> fundPlans = fundProj.getFundPlan();
		List<Category> categories = categoryServiceTemp.findFundProjCategoryList();
		List<Tag> tags = tagServiceTemp.findFundProjTagList();
		
		model.addAttribute("projectDTO",fundProjDTO);
		model.addAttribute("categories", categories);
		model.addAttribute("tags", tags);
		model.addAttribute("plans",fundPlans);
		return "cwdfunding/editOneFundProj";
	}
	
	/* [API] 查詢所有募資活動*/
	@ResponseBody
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam Integer pageNumber, Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		return page;
	}

	/* [API] 查詢單筆募資活動by ID */
	@ResponseBody
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjDTOById(projectID);
	}

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

	/* [API] 新增募資活動（含方案） */
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
			String baseNameProj = null;
			String pathStringProj = null;
	
			// plan 變數初始化
			String planTitle = null;
			String planUnitPrice = null;
			String planTotalAmount = null;
			String planBuyAmount = null;
			String planContent = null;
			MultipartFile planImage = null;
			String baseNamePlan = null;
			String pathStringPlan = null;

			
			// response定義
		    Map<String, Object> response = new HashMap<>();

	
		    // 上傳圖片 1.募資方案 
		    baseNameProj = UUID.randomUUID().toString();
		    System.out.println("圖片檔名為："+baseNameProj);

			try {
				pathStringProj = imageUtil.saveImage(ImageDirectory.CWDFUNDING, image, baseNameProj);
				System.out.println("圖片路徑："+pathStringProj);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}

			// 與Service互動
			try {
		        // 1. Validate input (basic validation examples)
		        if (title == null || title.isEmpty()) {
		            response.put("status", "error");
		            response.put("message", "Project title is required");
		            return ResponseEntity.badRequest().body(response);
		        }
		        
				projService.saveProj(title, categoryID, tagID, startDateStr, endDateStr, targetAmount, currentAmount,
						threshold, postponeDateStr, pathStringProj, description);
	
				/*
				 * 2. 存取fund plan (1) 取出所有陣列的元素
				 */
				for (int i = 0; i < planTitles.size(); i++) {
					planTitle = planTitles.get(i);
					planUnitPrice = planUnitPrices.get(i);
					planTotalAmount = planTotalAmounts.get(i);
					planBuyAmount = planBuyAmounts.get(i);
					planContent = planContents.get(i);
					planImage = planImages.get(i);
					
					// 上傳圖片 ＆ 給予圖片UUID並連同路徑(/images/cwdfunding/!@#$%%^&.___)存進資料庫
				    baseNamePlan = UUID.randomUUID().toString();
				    System.out.println("圖片檔名為："+baseNamePlan);
					try {
						pathStringPlan = imageUtil.saveImage(ImageDirectory.CWDFUNDING, planImage, baseNamePlan);
						System.out.println("方案圖片路徑："+pathStringPlan);
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
					
					System.out.println("projectID" + projectID);
					projService.savePlan(projectID, planTitle, planUnitPrice, planTotalAmount, planBuyAmount, pathStringPlan,
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
	
	/* [API] 編輯募資活動(含方案) */
	@ResponseBody
	@PutMapping("/api/fundproject/{projectID}")
	public ResponseEntity<Map<String, Object>> editProject(@PathVariable String projectID, @RequestParam String title,
			@RequestParam String categoryID, @RequestParam String tagID, @RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr, @RequestParam String targetAmount,
			@RequestParam String currentAmount, @RequestParam String threshold,
			@RequestParam("postponeDate") String postponeDateStr, @RequestParam MultipartFile image,
			@RequestParam("old-image") String oldImage,
			@RequestParam String description, @RequestParam List<String> planIDs, @RequestParam List<String> planTitles,
			@RequestParam List<String> planUnitPrices, @RequestParam List<String> planTotalAmounts,
			@RequestParam List<String> planBuyAmounts, @RequestParam List<MultipartFile> planImages, @RequestParam("old-planImage") List<String> oldPlanImages,
			@RequestParam List<String> planContents) {
			// project 變數初始化
			String baseNameProj = null;
			String pathStringProj = null;
	
			// plan 變數初始化
			String planID = null;
			String planTitle = null;
			String planUnitPrice = null;
			String planTotalAmount = null;
			String planBuyAmount = null;
			String planContent = null;
			MultipartFile planImage = null;
			String baseNamePlan = null;
			String pathStringPlan = null;

			
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
				 * 1. 存取fund project (1) 取得最新應填的projectID (2) 將image的檔名讀取出來，若使用者有上傳圖片則用此檔名，若無則用舊檔名
				 */
			    // 上傳圖片 1.募資方案 
			    
		        if(!image.getOriginalFilename().isEmpty()) {
				    baseNameProj = UUID.randomUUID().toString();
				    pathStringProj = imageUtil.saveImage(ImageDirectory.CWDFUNDING, image, baseNameProj);
				    System.out.println("new, baseNameProj:" + baseNameProj);
		        }else {
		        	pathStringProj = oldImage;
				    System.out.println("old, baseNameProj:" + baseNameProj);
		        }

				
				projService.editProj(projectID,title, categoryID, tagID, startDateStr, endDateStr, targetAmount, currentAmount,
						threshold, postponeDateStr, pathStringProj, description);
	
				/*
				 * 2. 存取fund plan (1) 取出所有陣列的元素
				 */
				for (int i = 0; i < planTitles.size(); i++) {
					planID = planIDs.get(i);
					planTitle = planTitles.get(i);
					planUnitPrice = planUnitPrices.get(i);
					planTotalAmount = planTotalAmounts.get(i);
					planBuyAmount = planBuyAmounts.get(i);
					planContent = planContents.get(i);
					planImage = planImages.get(i);
					
					// 使用者如有上傳新照片，則用此檔名；若無則用舊檔名
					if(! planImage.getOriginalFilename().isEmpty()) {
						baseNamePlan = UUID.randomUUID().toString();
						pathStringPlan = imageUtil.saveImage(ImageDirectory.CWDFUNDING, planImage, baseNamePlan);
					}else {
						pathStringPlan = oldPlanImages.get(i);
					}

					projService.editPlan(projectID, planID,planTitle, planUnitPrice, planTotalAmount, planBuyAmount, pathStringPlan,
							planContent);
				}
				response.put("status", "success");
				response.put("message", "Project edited successfully!");
				return ResponseEntity.ok(response);
			}catch (Exception e) {
				response.put("status", "error");
				response.put("message", "Something wrong!");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
	}

}
