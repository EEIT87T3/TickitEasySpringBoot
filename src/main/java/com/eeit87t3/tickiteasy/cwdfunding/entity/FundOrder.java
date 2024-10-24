package com.eeit87t3.tickiteasy.cwdfunding.entity;

import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fundingOrder")
public class FundOrder {

	@Id @Column(name = "orderID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderID;
	
	@Column(name = "memberID")
	private Integer memberID; 
	
	@Column(name = "tickitID")
	private String tickitID;
	
	@Column(name = "bonus")
	private Integer bonus;
	
	@Column(name = "totalAmount")
	private Integer totalAmount;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 檢查進來的時間，並做格式化
	@Column(name = "orderDate")
	private Timestamp orderDate;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "projectID", referencedColumnName = "projectID")
	private FundProj fundProj;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "planID", referencedColumnName = "planID")	
	private FundPlan fundPlan;

	public Integer getOrderID() {
		return orderID;
	}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}

	public Integer getMemberID() {
		return memberID;
	}

	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}

	public String getTickitID() {
		return tickitID;
	}

	public void setTickitID(String tickitID) {
		this.tickitID = tickitID;
	}

	public Integer getBonus() {
		return bonus;
	}

	public void setBonus(Integer bonus) {
		this.bonus = bonus;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public FundProj getFundProj() {
		return fundProj;
	}

	public void setFundProj(FundProj fundProj) {
		this.fundProj = fundProj;
	}

	public FundPlan getFundPlan() {
		return fundPlan;
	}

	public void setFundPlan(FundPlan fundPlan) {
		this.fundPlan = fundPlan;
	}
}
