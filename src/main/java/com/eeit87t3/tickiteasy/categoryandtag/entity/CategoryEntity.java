package com.eeit87t3.tickiteasy.categoryandtag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Chuan(chuan13)
 */
@Entity @Table(name = "category")
public class CategoryEntity {
	
	@Id @Column(name = "categoryId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;
	
	@Column(name = "categoryString")
	private String categoryString;
	
	@Column(name = "categoryName")
	private String categoryName;
	
	@Column(name = "categoryStatus")
	private Short categoryStatus;

	public CategoryEntity() {
	}

	public CategoryEntity(Integer categoryId, String categoryString, String categoryName, Short categoryStatus) {
		this.categoryId = categoryId;
		this.categoryString = categoryString;
		this.categoryName = categoryName;
		this.categoryStatus = categoryStatus;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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
		this.categoryName = categoryName;
	}

	public Short getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Short categoryStatus) {
		this.categoryStatus = categoryStatus;
	}
}
