package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity @Table(name = "fundingProj")
public class FundProj{

	
	@Id @Column(name = "projectID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectID;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "image")
	private String image;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss EE", timezone = "UTC+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 檢查進來的時間，並做格式化
	@Column(name = "startDate")
	private Timestamp startDate;
	
	@Column(name = "endDate")
	private Timestamp endDate;
	
	@Column(name = "targetAmount")
	private String targetAmount;
	
	@Column(name = "currentAmount")
	private String currentAmount;
	
	@Column(name = "threshold")
	private String threshold;
	
	@Column(name = "postponeDate")
	private Timestamp postponeDate;
	
	@Column(name = "status")
	private Integer status;
	
	/*
	 * [JoinColumn]告訴spring容器FundProj類別的"categoryID"屬性，會參考到CategoryEntity的類別"categoryId"屬性
	 * [JsonIgnore]和其他類別存在雙向關聯性，避免循環依賴的annotation
	 */
	@ManyToOne
	@JoinColumn( name = "categoryID", referencedColumnName = "categoryId")
	private CategoryEntity fundCategory;
	
	/*
	 * [JoinColumn]告訴spring容器FundProj類別的"tagID"屬性，會參考到TagEntity的類別"tagId"屬性
	 * [JsonIgnore]和其他類別存在雙向關聯性，避免循環依賴的annotation
	 */
	@ManyToOne
	@JoinColumn( name = "tagID", referencedColumnName = "tagId")
	private TagEntity fundTag;
	
	/*
	 * Fundproj沒有和FundProjPhoto有關的欄位
	 * [ mappedBy = "fundProj" ] : 
	 * 		告訴spring容器Fundproj資料表會被FundProjPhoto資料表參考，參考欄位為fundProjPhotos的"fundProj"屬性
	 */
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fundProj")
	private List<FundProjPhoto> fundProjPhotos = new ArrayList<>();
	
	/*
	 * Fundproj沒有和FundPlan有關的欄位
	 * [ mappedBy = "fundProj" ] : 
	 * 		告訴spring容器Fundproj類別會被FundPlan類別參考，參考欄位為fundPlan的"fundProj"屬性
	 */
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fundProj")
	private List<FundPlan> fundPlan = new ArrayList<>();
	
	public FundProj() {
		
	}
	

	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getTargetAmount() {
		return targetAmount;
	}
	public void setTargetAmount(String targetAmount) {
		this.targetAmount = targetAmount;
	}
	public String getCurrentAmount() {
		return currentAmount;
	}
	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public Timestamp getPostponeDate() {
		return postponeDate;
	}
	public void setPostponeDate(Timestamp postponeDate) {
		this.postponeDate = postponeDate;
	}



	public CategoryEntity getFundCategory() {
		return fundCategory;
	}


	public void setFundCategory(CategoryEntity fundCategory) {
		this.fundCategory = fundCategory;
	}


	public TagEntity getFundTag() {
		return fundTag;
	}


	public void setFundTag(TagEntity fundTag) {
		this.fundTag = fundTag;
	}


	public List<FundProjPhoto> getFundProjPhotos() {
		return fundProjPhotos;
	}


	public void setFundProjPhotos(List<FundProjPhoto> fundProjPhotos) {
		this.fundProjPhotos = fundProjPhotos;
	}


	public List<FundPlan> getFundPlan() {
		return fundPlan;
	}


	public void setFundPlan(List<FundPlan> fundPlan) {
		this.fundPlan = fundPlan;
	}


	// 返回格式化後的日期
    public String getFormattedStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(startDate);
    }

    public String getFormattedEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(endDate);
    }
    
    public String getFormattedPostponeDate() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(postponeDate);
    }
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	
}