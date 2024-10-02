package com.eeit87t3.tickiteasy.cwdfunding.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProjDTO;
import com.eeit87t3.tickiteasy.cwdfunding.repository.FundProjRepository;

@Service
public class FundProjService {

	private FundProjRepository fundProjRepo;
	
	public FundProjService(FundProjRepository fundProjsRepository) {
		this.fundProjRepo = fundProjsRepository;
	}
	
	/* 新增 */
	public FundProj saveProj(FundProj newProj) {
		return fundProjRepo.save(newProj);
			
	}
	

	/* 查詢全部（id升冪）：分頁 */
	public Page<FundProjDTO> findFundProjByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 10,Sort.Direction.ASC,"projectID");
		Page<FundProj> fundProjPage = fundProjRepo.findAll(pgb);
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
			dto.setThreshold(fundProj.getThreshold());
			dto.setPostponeDate(fundProj.getPostponeDate().toLocalDateTime());
			dto.setCategoryString(fundProj.getCategory().getCategoryString());
			dto.setCategoryName(fundProj.getCategory().getCategoryName());
			dto.setTagString(fundProj.getTag().getTagString());
			dto.setTagName(fundProj.getTag().getTagName());
			return dto;
		});
		return dtoPage;
	}
	/* 查詢單筆 */
	public FundProjDTO findFundProjById(Integer projectID) {
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
			dto.setThreshold(fundProj.getThreshold());
			dto.setPostponeDate(fundProj.getPostponeDate().toLocalDateTime());
			dto.setCategoryString(fundProj.getCategory().getCategoryString());
			dto.setCategoryName(fundProj.getCategory().getCategoryName());
			dto.setTagString(fundProj.getTag().getTagString());
			dto.setTagName(fundProj.getTag().getTagName());
			return dto;
		});
		return dto;
	}
	
	/* 刪除 */
	public boolean deleteFundProjById(Integer id) {
		FundProjDTO exist = new FundProjDTO();
		exist = findFundProjById(id);
		// 使用getProjectID判斷專案是否存在
		if (exist.getProjectID() != null) {
			fundProjRepo.deleteById(id);
			System.out.println(id+" project exists");
			return true;
		}else {
			System.out.println("project not found");
			return false;
		}
	}
	
	/* 更新 (尚未寫完)*/
	public FundProj updateFundProjById(Integer id, FundProj newProj) {
		Optional<FundProj> optional = fundProjRepo.findById(id);
		
		if(optional.isPresent()) {
			FundProj oldProj = optional.get();
			oldProj.setTitle(newProj.getTitle());
			oldProj.setDescription(newProj.getDescription());
		}
		return null;
	}
}
