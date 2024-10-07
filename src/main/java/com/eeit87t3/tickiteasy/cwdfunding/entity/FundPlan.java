package com.eeit87t3.tickiteasy.cwdfunding.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "fundingPlan")
public class FundPlan {
	
	@Id @Column(name="planID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planID;
	
	@Column(name="title")
	private String planTitle;
	
	@Column(name="planContent")
	private String planContent;
	
	@Column(name="unitPrice")
	private String planUnitPrice;
	
	@Column(name = "totalAmount")
	private String planTotalAmount;
	
	@Column(name = "buyAmount")
	private String planBuyAmount;
	
	@Column(name = "image")
	private String planImage;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn( name="projectID", referencedColumnName = "projectID")
	private FundProj fundProj;

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

	public FundProj getFundProj() {
		return fundProj;
	}

	public void setFundProj(FundProj fundProj) {
		this.fundProj = fundProj;
	}

	
}