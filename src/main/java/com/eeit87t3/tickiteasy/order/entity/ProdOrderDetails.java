package com.eeit87t3.tickiteasy.order.entity;

import java.sql.Date;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodOrderID")
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


	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
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


	@Override
	public String toString() {
		return "ProdOrderDetails [prodOrderDetailId=" + prodOrderDetailId + ", productId=" + productId + ", price="
				+ price + ", quantity=" + quantity + ", content=" + content + ", reviewTime=" + reviewTime + ", score="
				+ score + ", prodOrder=" + prodOrder + "]";
	}




    
}