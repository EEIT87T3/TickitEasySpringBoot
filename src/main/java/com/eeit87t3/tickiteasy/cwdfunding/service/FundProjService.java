package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlan;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundPlanDTO;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundPlanRepository;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjRepository;
import com.eeit87t3.tickiteasy.image.ImageUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FundProjService {

	@Autowired
	private FundProjRepository fundProjRepo;

	@Autowired
	private FundPlanRepository fundPlanRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private TagRepo tagRepo;
	
	@Autowired
	private ImageUtil imageUtil;
	
	public FundProjService(FundProjRepository fundProjsRepository) {
		this.fundProjRepo = fundProjsRepository;
	}
	
	/* 新增募資活動 */
	public FundProj saveProj(FundProjDTO fundProjDTO) {
		FundProj proj = new FundProj();
		
		System.out.println("新增service開始");
		//先用categoryID找出對應到的category實體，之後再塞進proj 
		Optional<CategoryEntity> coptional = categoryRepo.findById(Integer.valueOf(fundProjDTO.getCategoryId()));
		CategoryEntity category = coptional.get();
		//先用tagID找出對應到的tag實體，之後再塞進proj 
		Optional<TagEntity> toptional = tagRepo.findById(Integer.valueOf(fundProjDTO.getTagId()));
		TagEntity tag = toptional.get();
		
        // 將請求中的日期先格式化再轉換成LocalDateTime
        // 定義datetime-local格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // fundProjDTO.getStartDate().format(formatter): 將startDate格式化成formatter格式的字串
        // LocalDateTime.parse(＿＿＿, formatter): 將＿＿轉型成LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(fundProjDTO.getStartDate().format(formatter),formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(fundProjDTO.getEndDate().format(formatter),formatter);
        
        // 設定FundProjBean屬性，日期轉成.sql.TimeStamp
        proj.setTitle(fundProjDTO.getTitle());
        proj.setDescription(fundProjDTO.getDescription());
        proj.setImage(fundProjDTO.getImage());
        proj.setStartDate(Timestamp.valueOf(startDateTime));
        proj.setEndDate(Timestamp.valueOf(endDateTime));
        proj.setTargetAmount(fundProjDTO.getTargetAmount());
        proj.setCurrentAmount(fundProjDTO.getCurrentAmount());
        proj.setFundCategory(category);
        proj.setFundTag(tag);
        System.out.println(proj.getTitle());
		System.out.println("新增service結束");

		return fundProjRepo.save(proj);
			
	}
	
	

	/* 查詢全部（id升冪、以category搜索）：分頁 */
	public Page<FundProjDTO> findFundProjByPage(Integer pageNumber, Integer size, Integer categoryID){
		Pageable pgb = PageRequest.of(pageNumber-1, size,Sort.Direction.ASC,"projectID");
		Page<FundProj> fundProjPage = null;
		
		if ( categoryID != null) {
			fundProjPage = fundProjRepo.findProjectByCategory(categoryID, pgb);
		}else {
			fundProjPage = fundProjRepo.findAll(pgb);
		}
		//Page內建map()方法，可將A實體轉換成B實體
		Page<FundProjDTO> dtoPage= fundProjPage.map(fundProj ->{
			FundProjDTO dto = new FundProjDTO();
			dto.setProjectID(fundProj.getProjectID());
			dto.setTitle(fundProj.getTitle());
			dto.setDescription(fundProj.getDescription());
			dto.setImage(fundProj.getImage());
			dto.setStartDate(fundProj.getStartDate().toLocalDateTime());
			dto.setEndDate(fundProj.getEndDate().toLocalDateTime());
			dto.setTargetAmount(fundProj.getTargetAmount());
			dto.setCurrentAmount(fundProj.getCurrentAmount());
			dto.setThreshold(fundProj.getThreshold() != null ? fundProj.getThreshold() : "N/A");
	        // 處理 postponeDate 可能為 null 的情況
	        if (fundProj.getPostponeDate() != null) {
	            dto.setPostponeDate(fundProj.getPostponeDate().toLocalDateTime());
	        }
	        dto.setCategoryString(fundProj.getFundCategory().getCategoryString());
			dto.setCategoryName(fundProj.getFundCategory().getCategoryName());
			dto.setTagString(fundProj.getFundTag().getTagString());
			dto.setTagName(fundProj.getFundTag().getTagName());
			return dto;
		});
		return dtoPage;
	}
	
	/* 查詢單筆fund proj DTO */
	public FundProjDTO findFundProjDTOById(Integer projectID) {
		Optional<FundProj> optional = fundProjRepo.findById(projectID);
		FundProjDTO dto = new FundProjDTO();
		optional.map(fundProj -> {
			dto.setProjectID(fundProj.getProjectID());
			dto.setTitle(fundProj.getTitle());
			dto.setDescription(fundProj.getDescription());
			dto.setImage(fundProj.getImage());
			dto.setStartDate(fundProj.getStartDate().toLocalDateTime());
			dto.setEndDate(fundProj.getEndDate().toLocalDateTime());
			dto.setTargetAmount(fundProj.getTargetAmount());
			dto.setCurrentAmount(fundProj.getCurrentAmount());
			dto.setThreshold(fundProj.getThreshold() != null ? fundProj.getThreshold() : "N/A");
	        // 處理 postponeDate 可能為 null 的情況
	        if (fundProj.getPostponeDate() != null) {
	            dto.setPostponeDate(fundProj.getPostponeDate().toLocalDateTime());
	        }
			dto.setCategoryId(fundProj.getFundCategory().getCategoryId());
			dto.setCategoryString(fundProj.getFundCategory().getCategoryString());
			dto.setCategoryName(fundProj.getFundCategory().getCategoryName());
			dto.setTagId(fundProj.getFundTag().getTagId());
			dto.setTagString(fundProj.getFundTag().getTagString());
			dto.setTagName(fundProj.getFundTag().getTagName());
	        // 將 FundPlan 轉換為 FundPlanDTO 列表
	        List<FundPlanDTO> planDTOList = fundProj.getFundPlan().stream().map(plan -> {
	            FundPlanDTO planDTO = new FundPlanDTO();
	            planDTO.setPlanID(plan.getPlanID());
	            planDTO.setPlanTitle(plan.getPlanTitle());
	            planDTO.setPlanContent(plan.getPlanContent());
	            planDTO.setPlanUnitPrice(plan.getPlanUnitPrice());
	            planDTO.setPlanTotalAmount(plan.getPlanTotalAmount());
	            planDTO.setPlanBuyAmount(plan.getPlanBuyAmount());
	            planDTO.setPlanImage(plan.getPlanImage());
	            planDTO.setProjectID(fundProj.getProjectID()); // 關聯的 projectID
	            return planDTO;
	        }).collect(Collectors.toList());
	        
	        dto.setFundplanList(planDTOList);
			return dto;
		});
		return dto;
	}
	
	/* 查詢單筆fund proj */
	public FundProj findFundProjById(Integer projectID) {
		Optional<FundProj> optional = fundProjRepo.findById(projectID);
		FundProj fundProj = optional.get();
		return fundProj;
	}
	
	
	/* 查詢project最新單筆的projectID */ 
	public String findTopProject() {
		return String.valueOf(fundProjRepo.findTopProjectById().getProjectID());
	}
	

	
	/* 刪除 
	 * 1. 刪除FundProject的同時會一併刪除該對應的plan
	 * 	  因為在FundProj entity中有[cascade = CascadeType.ALL]
	 * 2. 使用ImageUtil將圖片從系統檔案夾刪除 
	 * */
	public boolean deleteFundProjById(Integer id) {
		// 1. 以projectID 找出fundProj和fundPlan兩資料表中是否存在對應物件
		FundProjDTO existProj = new FundProjDTO();
		existProj = findFundProjDTOById(id);
		List<FundPlan> existPlans = fundPlanRepo.findByProjectID(id);
		
		// 2. 將project和plan圖片欄位取出
		String imagePath = existProj.getImage();
		List<String> imagePathPlans = new ArrayList<>();
		
		for (FundPlan existPlan : existPlans) {
			imagePathPlans.add(existPlan.getPlanImage());
		}
		
		// 3. 執行刪除(包含從檔案夾中刪除圖片)
			fundProjRepo.deleteById(id);
			//try{}catch{}：刪除/images/cwdfunding/內圖片
			try {
				imageUtil.deleteImage(imagePath);
				for (String imagePathPlan : imagePathPlans) {
					imageUtil.deleteImage(imagePathPlan);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

	}
	
	/* 編輯募資活動 (尚未寫完)*/
	public FundProj editProj(
	        String projectID,
	        String title,
	        String categoryID,
	        String tagID,
	        String startDateStr,
	        String endDateStr,
	        String targetAmount,
	        String currentAmount,
	        String threshold,
	        String postponeDateStr,
	        String filename,
	        String description
	) {
	    // 找到專案
	    Optional<FundProj> optional = fundProjRepo.findById(Integer.valueOf(projectID));
	    if (!optional.isPresent()) {
	        throw new EntityNotFoundException("Project not found with ID: " + projectID);
	    }
	    
	    FundProj proj = optional.get();
	    // 更新 category 和 tag
	    Optional<CategoryEntity> coptional = categoryRepo.findById(Integer.valueOf(categoryID));
	    proj.setFundCategory(coptional.orElse(null));
	    
	    Optional<TagEntity> toptional = tagRepo.findById(Integer.valueOf(tagID));
	    proj.setFundTag(toptional.orElse(null));

//	    proj.setTitle(title);
	    proj.setDescription(description);
	    proj.setImage(filename);

	    // 解析日期
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	    LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, formatter);
	    LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, formatter);
	    LocalDateTime postponeDateTime = LocalDateTime.parse(postponeDateStr, formatter);

	    // 設定新的日期
	    proj.setStartDate(Timestamp.valueOf(startDateTime));
	    proj.setEndDate(Timestamp.valueOf(endDateTime));
	    proj.setTargetAmount(targetAmount);
	    proj.setCurrentAmount(currentAmount);
	    proj.setThreshold(threshold);
	    proj.setPostponeDate(Timestamp.valueOf(postponeDateTime));


	    // 儲存更新的專案
	    return fundProjRepo.save(proj);
	}

	/* 編輯募資活動的方案 */
	public FundPlan editPlan(
			String projectID,
			String planID,
			String title,
			String unitPrice,
			String totalAmount,
			String buyAmount,
			String image,
			String content
			) {
		FundPlan newFundPlan = new FundPlan();
		
	    // 找到專案
	    Optional<FundPlan> optional = fundPlanRepo.findById(Integer.valueOf(planID));
	    if (!optional.isPresent()) {
	        throw new EntityNotFoundException("Project not found with ID: " + planID);
	    }
	    
	    newFundPlan = optional.get();
		
		//先用projectID找出對應到的FundProj實體，之後再塞進plan
		Optional<FundProj> poptional = fundProjRepo.findById(Integer.valueOf(projectID));
		FundProj fundProj = poptional.get();
		
        // 設定FundPlan屬性
		newFundPlan.setFundProj(fundProj);
		newFundPlan.setPlanTitle(title);
		newFundPlan.setPlanUnitPrice(unitPrice);
		newFundPlan.setPlanTotalAmount(totalAmount);
		newFundPlan.setPlanBuyAmount(buyAmount);
		newFundPlan.setPlanImage(image);
		newFundPlan.setPlanContent(content);
		
		return fundPlanRepo.save(newFundPlan);
	}
}
