package com.eeit87t3.tickiteasy.event.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 「新增活動」或「編輯活動」時使用的 DTO。
 * 
 * @author Chuan (chuan13)
 */
public class EventsDTO {

    private Integer eventID;
    private String eventName;
    private MultipartFile eventPicFile;
    private String categoryString;
    private String tagString;
    private String eventDesc;
    private String place;
    private String address;
    private Integer quantityTotalAvailable;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime eventStartTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime eventEndTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime startEntryTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC+8")
    private LocalDateTime endEntryTime;
    
    
	public Integer getEventID() {
		return eventID;
	}
	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public MultipartFile getEventPicFile() {
		return eventPicFile;
	}
	public void setEventPicFile(MultipartFile eventPicFile) {
		this.eventPicFile = eventPicFile;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public String getTagString() {
		return tagString;
	}
	public void setTagString(String tagString) {
		this.tagString = tagString;
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
	public Integer getQuantityTotalAvailable() {
		return quantityTotalAvailable;
	}
	public void setQuantityTotalAvailable(Integer quantityTotalAvailable) {
		this.quantityTotalAvailable = quantityTotalAvailable;
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
	
	@Override
	public String toString() {
		return "EventsDTO [eventID=" + eventID + ", eventName=" + eventName + ", eventPicFile=" + eventPicFile
				+ ", categoryString=" + categoryString + ", tagString=" + tagString + ", eventDesc=" + eventDesc
				+ ", place=" + place + ", address=" + address + ", quantityTotalAvailable=" + quantityTotalAvailable
				+ ", eventStartTime=" + eventStartTime + ", eventEndTime=" + eventEndTime + ", startEntryTime="
				+ startEntryTime + ", endEntryTime=" + endEntryTime + "]";
	}
}
