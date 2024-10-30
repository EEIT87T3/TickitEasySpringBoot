package com.eeit87t3.tickiteasy.post.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportDTO {

    private Integer reportID;

    private Integer postID;
    
    private Integer reportedMemberID;
    
    private Integer accusedMemberID;

    private String reason;

    private String savedContent;

    private String savedTitle;

    private Integer savedCategoryID;

    private Integer savedTagID;
    
    private Integer reportStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Timestamp savedEditTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Timestamp reportTime;

    // Constructors
    public ReportDTO() {
    }

    public ReportDTO(Integer reportID, Integer postID, Integer reportedMemberID, Integer accusedMemberID, String reason, 
                     String savedContent, String savedTitle, Integer savedCategoryID,
                     Integer savedTagID, Timestamp savedEditTime, Timestamp reportTime) {
        this.reportID = reportID;
        this.postID = postID;
        this.reportedMemberID = reportedMemberID;
        this.accusedMemberID = accusedMemberID;
        this.reason = reason;
        this.savedContent = savedContent;
        this.savedTitle = savedTitle;
        this.savedCategoryID = savedCategoryID;
        this.savedTagID = savedTagID;
        this.savedEditTime = savedEditTime;
        this.reportTime = reportTime;
    }

    // Getters and Setters
    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

   
    public Integer getReportedMemberID() {
		return reportedMemberID;
	}

	public Integer getAccusedMemberID() {
		return accusedMemberID;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportedMemberID(Integer reportedMemberID) {
		this.reportedMemberID = reportedMemberID;
	}

	public void setAccusedMemberID(Integer accusedMemberID) {
		this.accusedMemberID = accusedMemberID;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSavedContent() {
        return savedContent;
    }

    public void setSavedContent(String savedContent) {
        this.savedContent = savedContent;
    }

    public String getSavedTitle() {
        return savedTitle;
    }

    public void setSavedTitle(String savedTitle) {
        this.savedTitle = savedTitle;
    }

    public Integer getSavedCategoryID() {
        return savedCategoryID;
    }

    public void setSavedCategoryID(Integer savedCategoryID) {
        this.savedCategoryID = savedCategoryID;
    }

    public Integer getSavedTagID() {
        return savedTagID;
    }

    public void setSavedTagID(Integer savedTagID) {
        this.savedTagID = savedTagID;
    }

    public Timestamp getSavedEditTime() {
        return savedEditTime;
    }

    public void setSavedEditTime(Timestamp savedEditTime) {
        this.savedEditTime = savedEditTime;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }
}
