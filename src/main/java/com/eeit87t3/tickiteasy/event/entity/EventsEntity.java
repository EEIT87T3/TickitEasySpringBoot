package com.eeit87t3.tickiteasy.event.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * @author Chuan(chuan13)
 */
@Entity @Table(name = "events")
public class EventsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private Integer eventID;

    @Column(name = "status", nullable = false)
    private Short status = 0;

    @Column(name = "eventName", nullable = false, unique = true, length = 60)
    private String eventName;

    @Column(name = "eventPic", length = 255)
    private String eventPic;

    @ManyToOne
    @JoinColumn(name = "eventCategory", nullable = false)
    private CategoryEntity eventCategory;

    @ManyToOne
    @JoinColumn(name = "eventTag")
    private TagEntity eventTag;

    @Column(name = "eventDesc", columnDefinition = "NVARCHAR(MAX)")
    private String eventDesc;

    @Column(name = "place", length = 60)
    private String place;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "eventStartTime", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime eventStartTime;

    @Column(name = "eventEndTime", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime eventEndTime;

    @Column(name = "startEntryTime", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime startEntryTime;

    @Column(name = "endEntryTime", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime endEntryTime;

    @Column(name = "earliestStartSaleTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime earliestStartSaleTime;

    @Column(name = "quantityTotalAvailable", nullable = false)
    private Integer quantityTotalAvailable;

    @Column(name = "quantityTotalPurchased", nullable = false)
    private Integer quantityTotalPurchased = 0;

    @Column(name = "totalReviews", nullable = false)
    private Integer totalReviews = 0;
    
    @Column(name = "totalScore", nullable = false)
    private Integer totalScore = 0;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    @JsonIgnore
    private List<TicketTypesEntity> ticketTypes = new ArrayList<TicketTypesEntity>();
    
    
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
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDateTime getEventStartTime() {
		return eventStartTime;
	}
	public void setEventStartTime(LocalDateTime eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	public LocalDateTime getEventEndTime() {
		return eventEndTime;
	}
	public void setEventEndTime(LocalDateTime eventEndTime) {
		this.eventEndTime = eventEndTime;
	}
	public LocalDateTime getStartEntryTime() {
		return startEntryTime;
	}
	public void setStartEntryTime(LocalDateTime startEntryTime) {
		this.startEntryTime = startEntryTime;
	}
	public LocalDateTime getEndEntryTime() {
		return endEntryTime;
	}
	public void setEndEntryTime(LocalDateTime endEntryTime) {
		this.endEntryTime = endEntryTime;
	}
	public LocalDateTime getEarliestStartSaleTime() {
		return earliestStartSaleTime;
	}
	public void setEarliestStartSaleTime(LocalDateTime earliestStartSaleTime) {
		this.earliestStartSaleTime = earliestStartSaleTime;
	}
	public Integer getQuantityTotalAvailable() {
		return quantityTotalAvailable;
	}
	public void setQuantityTotalAvailable(Integer quantityTotalAvailable) {
		this.quantityTotalAvailable = quantityTotalAvailable;
	}
	public Integer getQuantityTotalPurchased() {
		return quantityTotalPurchased;
	}
	public void setQuantityTotalPurchased(Integer quantityTotalPurchased) {
		this.quantityTotalPurchased = quantityTotalPurchased;
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
	public List<TicketTypesEntity> getTicketTypes() {
		return ticketTypes;
	}
	public void setTicketTypes(List<TicketTypesEntity> ticketTypes) {
		this.ticketTypes = ticketTypes;
	}

	@Override
	public String toString() {
		return "EventsEntity [eventID=" + eventID + ", status=" + status + ", eventName=" + eventName + ", eventPic="
				+ eventPic + ", eventDesc=" + eventDesc
				+ ", place=" + place + ", address=" + address + ", eventStartTime=" + eventStartTime + ", eventEndTime="
				+ eventEndTime + ", startEntryTime=" + startEntryTime + ", endEntryTime=" + endEntryTime
				+ ", earliestStartSaleTime=" + earliestStartSaleTime + ", quantityTotalAvailable="
				+ quantityTotalAvailable + ", quantityTotalPurchased=" + quantityTotalPurchased + ", totalReviews="
				+ totalReviews + ", totalScore=" + totalScore + "]";
	}
}
