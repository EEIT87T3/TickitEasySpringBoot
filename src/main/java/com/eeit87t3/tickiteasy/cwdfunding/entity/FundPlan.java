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
	private String title;
	
	@Column(name="planContent")
	private String content;
	
	@Column(name="unitPrice")
	private String unitPrice;
	
	@Column(name = "totalAmount")
	private String totalAmount;
	
	@Column(name = "buyAmount")
	private String buyAmount;
	
	@Column(name = "image")
	private String image;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(String buyAmount) {
		this.buyAmount = buyAmount;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public FundProj getFundProj() {
		return fundProj;
	}

	public void setFundProj(FundProj fundProj) {
		this.fundProj = fundProj;
	}

}
