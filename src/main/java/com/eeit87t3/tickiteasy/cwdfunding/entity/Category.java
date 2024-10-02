package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

	@Id @Column( name = "categoryID")
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Integer categoryID;
	
	@Column( name = "categoryName")
	private String categoryName;

	@Column( name = "categoryString")
	private String categoryString;
	
	@Column( name = "categoryStatus")
	private Integer categoryStatus;
	
	/*
	 * Category沒有和Fundproject有關的欄位
	 * [ mappedBy = "category" ] : 
	 * 		告訴spring容器Category類別會被FundProj類別參考，參考欄位為FundProj的"category"屬性
	 */
	@OneToMany(mappedBy = "category")
	private List<FundProj> fundProj = new ArrayList<>();

	
	public Integer getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryString() {
		return categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName= categoryName;
	}

	public Integer getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Integer categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public List<FundProj> getFundProj() {
		return fundProj;
	}

	public void setFundProj(List<FundProj> fundProj) {
		this.fundProj = fundProj;
	}

}
