package com.eeit87t3.tickiteasy.categoryandtag.entity;

import java.util.List;

import com.eeit87t3.tickiteasy.cwdfunding.entity.FundProj;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
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

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eventTag")  // mappedBy 寫對方的外鍵 Java property 名稱
	private List<EventsEntity> events;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "postTag")  
	private List<PostEntity> posts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "fundTag")
	private List<FundProj> fundProj;
	
	public TagEntity() {
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
	public List<EventsEntity> getEvents() {
		return events;
	}
	public void setEvents(List<EventsEntity> events) {
		this.events = events;
	}
}