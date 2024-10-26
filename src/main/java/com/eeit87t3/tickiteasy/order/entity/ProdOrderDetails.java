package com.eeit87t3.tickiteasy.order.entity;

import java.sql.Date;

import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "prodOrderDetails")
public class ProdOrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prodOrderDetailID")
    private Integer prodOrderDetailId;
    
    @Column(name = "productID")
    private Integer productId;
    
    @Column(name = "price")
    private Integer price;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "reviewTime")
    private Date reviewTime;
    
    @Column(name = "score")
    private Integer score;

    @Column(name = "ticketTypeID")
    private Integer ticketTypeId;
    
    @Column(name = "ticketPrice")
    private Integer ticketPrice;
    
    @Column(name = "ticketQuantity")
    private Integer ticketQuantity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodOrderID")
    @JsonBackReference
    private ProdOrders prodOrder;
    

    // 構造函數
    public ProdOrderDetails() {
    }


	public Integer getProdOrderDetailId() {
		return prodOrderDetailId;
	}


	public void setProdOrderDetailId(Integer prodOrderDetailId) {
		this.prodOrderDetailId = prodOrderDetailId;
	}

	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Date getReviewTime() {
		return reviewTime;
	}


	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}


	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	public ProdOrders getProdOrder() {
		return prodOrder;
	}


	public void setProdOrder(ProdOrders prodOrder) {
		this.prodOrder = prodOrder;
	}


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public Integer getTicketTypeId() {
		return ticketTypeId;
	}


	public void setTicketTypeId(Integer ticketTypeId) {
		this.ticketTypeId = ticketTypeId;
	}


	public Integer getTicketPrice() {
		return ticketPrice;
	}


	public void setTicketPrice(Integer ticketPrice) {
		this.ticketPrice = ticketPrice;
	}


	public Integer getTicketQuantity() {
		return ticketQuantity;
	}


	public void setTicketQuantity(Integer ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}



}