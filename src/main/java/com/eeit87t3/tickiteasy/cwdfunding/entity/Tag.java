package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {

	@Id @Column( name = "tagID")
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Integer tagID;
	
	@Column( name = "tagName")
	private String tagName;

	@Column( name = "tagString")
	private String tagString;
	
	@Column( name = "tagStatus")
	private Integer tagStatus;
	
	/*
	 * Tag沒有和Fundproject有關的欄位
	 * [ mappedBy = "tag" ] : 
	 * 		告訴spring容器Tag類別會被FundProj類別參考，參考欄位為FundProj類別的"tag"屬性
	 */
	@OneToMany(mappedBy = "tag")
	private List<FundProj> fundProj = new ArrayList<>();

	public Integer getTagID() {
		return tagID;
	}

	public void setTagID(Integer tagID) {
		this.tagID = tagID;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagString() {
		return tagString;
	}

	public void setTagString(String tagString) {
		this.tagString = tagString;
	}

	public Integer getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(Integer tagStatus) {
		this.tagStatus = tagStatus;
	}

	public List<FundProj> getFundProj() {
		return fundProj;
	}

	public void setFundProj(List<FundProj> fundProj) {
		this.fundProj = fundProj;
	}
	
	
}
