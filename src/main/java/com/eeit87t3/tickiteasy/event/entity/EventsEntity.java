package com.eeit87t3.tickiteasy.event.entity;

import java.time.LocalDateTime;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * @author Chuan(chuan13)
 */
@Entity @Table(name = "events")
public class EventsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private Integer eventID;

    @Column(name = "status")
    private Short status;

    @Column(name = "eventName")
    private String eventName;

    @Column(name = "eventPic")
    private String eventPic;

    @ManyToOne
    @JoinColumn(name = "eventCategory")
    private CategoryEntity eventCategory;

    @ManyToOne
    @JoinColumn(name = "eventTag")
    private TagEntity eventTag;

    @Column(name = "eventDesc")
    private String eventDesc;

    @Column(name = "earliestSessionTime")
    private LocalDateTime earliestSessionTime;

    @Column(name = "latestSessionTime")
    private LocalDateTime latestSessionTime;

    @Column(name = "earliestStartSaleTime")
    private LocalDateTime earliestStartSaleTime;

    @Column(name = "totalReviews")
    private Integer totalReviews;

    @Column(name = "totalScore")
    private Integer totalScore;

    @PrePersist
    public void onCreate() {
//		測試
	}

	public EventsEntity() {
		super();
	}

	public Integer getEventID() {
		return eventID;
	}

	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventPic() {
		return eventPic;
	}

	public void setEventPic(String eventPic) {
		this.eventPic = eventPic;
	}

	public CategoryEntity getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(CategoryEntity eventCategory) {
		this.eventCategory = eventCategory;
	}

	public TagEntity getEventTag() {
		return eventTag;
	}

	public void setEventTag(TagEntity eventTag) {
		this.eventTag = eventTag;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public LocalDateTime getEarliestSessionTime() {
		return earliestSessionTime;
	}

	public void setEarliestSessionTime(LocalDateTime earliestSessionTime) {
		this.earliestSessionTime = earliestSessionTime;
	}

	public LocalDateTime getLatestSessionTime() {
		return latestSessionTime;
	}

	public void setLatestSessionTime(LocalDateTime latestSessionTime) {
		this.latestSessionTime = latestSessionTime;
	}

	public LocalDateTime getEarliestStartSaleTime() {
		return earliestStartSaleTime;
	}

	public void setEarliestStartSaleTime(LocalDateTime earliestStartSaleTime) {
		this.earliestStartSaleTime = earliestStartSaleTime;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Integer totalReviews) {
		this.totalReviews = totalReviews;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
}
