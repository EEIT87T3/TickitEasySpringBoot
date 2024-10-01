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
@Entity @Table(name = "tag")
public class TagEntity {
	
	@Id @Column(name = "tagId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tagId;
	
	@Column(name = "tagString")
	private String tagString;
	
	@Column(name = "tagName")
	private String tagName;
	
	@Column(name = "tagStatus")
	private Short tagStatus;

	public TagEntity() {
	}

	public TagEntity(Integer tagId, String tagString, String tagName, Short tagStatus) {
		super();
		this.tagId = tagId;
		this.tagString = tagString;
		this.tagName = tagName;
		this.tagStatus = tagStatus;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagString() {
		return tagString;
	}

	public void setTagString(String tagString) {
		this.tagString = tagString;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Short getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(Short tagStatus) {
		this.tagStatus = tagStatus;
	}

}