package com.eeit87t3.tickiteasy.cwdfunding.controller;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.service.FundProjService;

@Controller
@RequestMapping("/admin")
public class FundProjController {

	@Autowired
	private FundProjService projService;

	/* 列出所有募資活動頁面, api */
	@GetMapping("/fundproject")
	public String showProjPage(@RequestParam(value="p", defaultValue = "1") Integer pageNumber,Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		model.addAttribute("page", page);
		
		return "cwdfunding/showFundProj";
	}
	
	@ResponseBody
	@GetMapping("/api/fundproject")
	public Page<FundProjDTO> findByPageApi(@RequestParam Integer pageNumber,Model model) {
		Page<FundProjDTO> page = projService.findFundProjByPage(pageNumber);
		return page;
	}
	
	
	/* 刪除募資活動by ID, api */
	@ResponseBody
	@GetMapping("/api/fundproject/{projectID}")
	public FundProjDTO findByID(@PathVariable Integer projectID) {
		return projService.findFundProjById(projectID);
	}
	
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
	
	/* 新增募資活動, api */
	@GetMapping("/add-project")
	public String addProjPage(Model model) {
		return "cwdfunding/addFundProjnPlan";
	}
	
	@ResponseBody
	@PostMapping("/api/fundproject")
	public String addProject(
			@RequestParam String title,
			@RequestParam String categoryID,
			@RequestParam String tagID,
			@RequestParam("startDate") String startDateStr,
			@RequestParam("endDate") String endDateStr,
			@RequestParam String targetAmount,
			@RequestParam String currentAmount,
			@RequestParam String threshold,
			@RequestParam String postponeDateStr,
			@RequestParam MultipartFile image,
			@RequestParam String description,
			@RequestParam("planTitle") String planTitle,
			@RequestParam String projectID,
			@RequestParam("planContent") String planContent,
			@RequestParam("planUnitPrice") String planUnitPrice,
			@RequestParam("planTotalAmount") String planTotalAmount,
			@RequestParam("planBuyAmount") String planBuyAmount,
			@RequestParam("planImage") MultipartFile planImage
			) {
		// 處理圖片（存進本端、將檔名寫進資料庫）
        String filename = image.getOriginalFilename();
        String planFilename = planImage.getOriginalFilename();
        
//        projService.saveProj(title, categoryID, tagID, startDateStr,
//        		endDateStr, targetAmount, currentAmount, threshold, 
//        		postponeDateStr, filename, description);
        
        projService.savePlan(projectID, planTitle, planUnitPrice, planTotalAmount, planBuyAmount, planFilename, planContent);
        
        return "Project added successfully!";
	}
 
	
	

}
