package com.eeit87t3.tickiteasy.post.entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "postReport")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportID;
    
    @ManyToOne
    @JoinColumn(name = "postID")
    private PostEntity post; 
    
    @ManyToOne
    @JoinColumn(name = "reportedMemberID", referencedColumnName = "memberID")
    private Member reportedMember; // 檢舉者的會員資料

    @ManyToOne
    @JoinColumn(name = "accusedMemberID", referencedColumnName = "memberID")
    private Member accusedMember; // 被檢舉者的會員資料
   
//    @Column(name = "postID")
//	private Integer postID;
//	
//	@Column(name = "memberID")
//	private Integer memberID;
	
	@Column(name = "reason")
    private String reason; 
	
	@Lob
	@Column(name = "savedContent")
	private String savedContent; 
	
	@Column(name = "savedTitle")
	private String savedTitle; 
	
	@Column(name = "savedCategoryID")
	private Integer savedCategoryID; 

	@Column(name = "savedTagID")
	private Integer savedTagID;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "savedEditTime")
	private Timestamp savedEditTime;
	
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "reportTime")
    private Timestamp reportTime;
    
    @Column(name = "reportStatus")
    private Integer reportStatus;
    
    @Column(name = "isDeleted")
    private boolean isDeleted = false; // 用來標記是否已刪除

	public Integer getReportID() {
		return reportID;
	}

	public PostEntity getPost() {
		return post;
	}

	public Member getReportedMember() {
		return reportedMember;
	}

	public Member getAccusedMember() {
		return accusedMember;
	}

	public String getReason() {
		return reason;
	}

	public String getSavedContent() {
		return savedContent;
	}

	public String getSavedTitle() {
		return savedTitle;
	}

	public Integer getSavedCategoryID() {
		return savedCategoryID;
	}

	public Integer getSavedTagID() {
		return savedTagID;
	}

	public Timestamp getSavedEditTime() {
		return savedEditTime;
	}

	public Timestamp getReportTime() {
		return reportTime;
	}

	public Integer getReportStatus() {
		return reportStatus;
	}

	public void setReportID(Integer reportID) {
		this.reportID = reportID;
	}

	public void setPost(PostEntity post) {
		this.post = post;
	}

	public void setReportedMember(Member reportedMember) {
		this.reportedMember = reportedMember;
	}

	public void setAccusedMember(Member accusedMember) {
		this.accusedMember = accusedMember;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setSavedContent(String savedContent) {
		this.savedContent = savedContent;
	}

	public void setSavedTitle(String savedTitle) {
		this.savedTitle = savedTitle;
	}

	public void setSavedCategoryID(Integer savedCategoryID) {
		this.savedCategoryID = savedCategoryID;
	}

	public void setSavedTagID(Integer savedTagID) {
		this.savedTagID = savedTagID;
	}

	public void setSavedEditTime(Timestamp savedEditTime) {
		this.savedEditTime = savedEditTime;
	}

	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}

	public void setReportStatus(Integer reportStatus) {
		this.reportStatus = reportStatus;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	
    
    
   
   
}

