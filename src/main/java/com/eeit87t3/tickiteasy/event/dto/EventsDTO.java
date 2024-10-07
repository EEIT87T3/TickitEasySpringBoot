package com.eeit87t3.tickiteasy.event.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * 「新增活動」或「編輯活動」時使用的 DTO。
 * 
 * @author Chuan(chuan13)
 */
public class EventsDTO {

    private Integer eventID;
    private String statusString;
    private String eventName;
    private MultipartFile eventPicFile;
    private String categoryString;
    private String tagString;
    private String eventDesc;
    
	public Integer getEventID() {
		return eventID;
	}
	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
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
	
	@Override
	public String toString() {
		return "EventsDTO [eventID=" + eventID + ", statusString=" + statusString + ", eventName=" + eventName
				+ ", eventPicFile=" + eventPicFile + ", categoryString=" + categoryString + ", tagString=" + tagString
				+ ", eventDesc=" + eventDesc + "]";
	}
}
