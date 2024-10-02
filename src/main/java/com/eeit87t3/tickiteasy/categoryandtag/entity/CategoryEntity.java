package com.eeit87t3.tickiteasy.categoryandtag.entity;

import java.util.List;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eventCategory")  // mappedBy 寫對方的外鍵 Java property 名稱
	private List<EventsEntity> events;
	
	public CategoryEntity() {
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
	public List<EventsEntity> getEvents() {
		return events;
	}
	public void setEvents(List<EventsEntity> events) {
		this.events = events;
	}
}
