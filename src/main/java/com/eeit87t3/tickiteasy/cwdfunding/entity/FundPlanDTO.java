package com.eeit87t3.tickiteasy.cwdfunding.entity;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author TingXD (chen19990627)
 */
public class FundPlanDTO {

	private Integer planID;
	private String planTitle;
	private String planContent;
	private String planUnitPrice;
	private String planTotalAmount;
	private String planBuyAmount;
	private String planImage;
	private MultipartFile planImageFile;
	private Integer projectID;
	
	public Integer getPlanID() {
		return planID;
	}
	public void setPlanID(Integer planID) {
		this.planID = planID;
	}
	public String getPlanTitle() {
		return planTitle;
	}
	public void setPlanTitle(String planTitle) {
		this.planTitle = planTitle;
	}
	public String getPlanContent() {
		return planContent;
	}
	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}
	public String getPlanUnitPrice() {
		return planUnitPrice;
	}
	public void setPlanUnitPrice(String planUnitPrice) {
		this.planUnitPrice = planUnitPrice;
	}
	public String getPlanTotalAmount() {
		return planTotalAmount;
	}
	public void setPlanTotalAmount(String planTotalAmount) {
		this.planTotalAmount = planTotalAmount;
	}
	public String getPlanBuyAmount() {
		return planBuyAmount;
	}
	public void setPlanBuyAmount(String planBuyAmount) {
		this.planBuyAmount = planBuyAmount;
	}
	public String getPlanImage() {
		return planImage;
	}
	public void setPlanImage(String planImage) {
		this.planImage = planImage;
	}
	public MultipartFile getPlanImageFile() {
		return planImageFile;
	}
	public void setPlanImageFile(MultipartFile planImageFile) {
		this.planImageFile = planImageFile;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	
}
