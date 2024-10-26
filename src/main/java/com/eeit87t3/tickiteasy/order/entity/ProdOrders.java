package com.eeit87t3.tickiteasy.order.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity 
@Table(name = "prodOrders")
public class ProdOrders {
	@Id
	@Column(name = "prodOrderId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer prodOrderID; 
	
    @ManyToOne
	@JoinColumn(name = "memberID", referencedColumnName = "memberID")
    private Member member; 
	
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Taipei")
	@Column(name = "orderDate")
	private Timestamp orderDate;
	
	@Column(name = "payments")
	private String payments;
	
	@Column(name = "paymentInfo")
	private String paymentInfo;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "totalAmount")
	private int totalAmount;
	
	@Column(name = "shippingStatus")
	private String shippingStatus;
	
	@Column(name = "shippingID")
	private int shippingID;
	
	@Column(name = "recipientName")
	private String recipientName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
    @OneToMany(mappedBy = "prodOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
	private List<ProdOrderDetails> prodOrderDetailsBean;

	public ProdOrders(Integer prodOrderID,Member member, Timestamp orderDate, String payments, String paymentInfo, String status,
			int totalAmount, String shippingStatus, int shippingID, String recipientName, String address,
			String phone) {
		super();
		this.prodOrderID = prodOrderID;
		this.member = member;
		this.orderDate = orderDate;
		this.payments = payments;
		this.paymentInfo = paymentInfo;
		this.status = status;
		this.totalAmount = totalAmount;
		this.shippingStatus = shippingStatus;
		this.shippingID = shippingID;
		this.recipientName = recipientName;
		this.address = address;
		this.phone = phone;
	}
	public ProdOrders() {
		
	}
	public Integer getProdOrderID() {
		return prodOrderID;
	}
	public void setProdOrderID(Integer prodOrderID) {
		this.prodOrderID = prodOrderID;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Timestamp  getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public String getPayments() {
		return payments;
	}
	public void setPayments(String payments) {
		this.payments = payments;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(String paymenInfo) {
		this.paymentInfo = paymenInfo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}
	public int getShippingID() {
		return shippingID;
	}
	public void setShippingID(int shippingID) {
		this.shippingID = shippingID;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<ProdOrderDetails> getProdOrderDetailsBean() {
		return prodOrderDetailsBean;
	}
	public void setProdOrderDetailsBean(List<ProdOrderDetails> prodOrderDetailsBean) {
		this.prodOrderDetailsBean = prodOrderDetailsBean;
	}


}
