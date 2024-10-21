package com.eeit87t3.tickiteasy.product.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productID")
	private Integer productID;
	
	@ManyToOne
    @JoinColumn(name = "productCategory", referencedColumnName = "categoryId", nullable = false)
	private CategoryEntity productCategory;
	
	@ManyToOne
	@JoinColumn(name = "productTag", referencedColumnName = "tagId")
	private TagEntity productTag;
	
	@Column(name = "productName", unique = true, nullable = false)
	private String productName;
	
	@Column(name = "productPic")
	private String productPic;
	
	@Column(name = "productDesc")
	private String productDesc;
	
	@Column(name = "price", nullable = false)
	private Integer price;
	
	@Column(name = "stock", nullable = false)
	private Integer stock;
	
	@Column(name = "status", nullable = false)
	private Integer status =0;
	
	@Column(name = "prodTotalReviews", nullable = false)
	private Integer prodTotalReviews = 0;
	
	@Column(name = "prodTotalScore", nullable = false)
	private Integer prodTotalScore = 0;
	
	@Column(name = "createdDate")
	@CreationTimestamp
	private LocalDateTime createdDate;

	public ProductEntity() {
	}

	public ProductEntity(Integer productID, CategoryEntity productCategory, TagEntity productTag, String productName,
			String productPic, String productDesc, Integer price, Integer stock, Integer status,
			Integer prodTotalReviews, Integer prodTotalScore, LocalDateTime createdDate) {
		this.productID = productID;
		this.productCategory = productCategory;
		this.productTag = productTag;
		this.productName = productName;
		this.productPic = productPic;
		this.productDesc = productDesc;
		this.price = price;
		this.stock = stock;
		this.status = status;
		this.prodTotalReviews = prodTotalReviews;
		this.prodTotalScore = prodTotalScore;
		this.createdDate = createdDate;
	}

	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public CategoryEntity getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(CategoryEntity productCategory) {
		this.productCategory = productCategory;
	}

	public TagEntity getProductTag() {
		return productTag;
	}

	public void setProductTag(TagEntity productTag) {
		this.productTag = productTag;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductPic() {
		return productPic;
	}

	public void setProductPic(String productPic) {
		this.productPic = productPic;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getProdTotalReviews() {
		return prodTotalReviews;
	}

	public void setProdTotalReviews(Integer prodTotalReviews) {
		this.prodTotalReviews = prodTotalReviews;
	}

	public Integer getProdTotalScore() {
		return prodTotalScore;
	}

	public void setProdTotalScore(Integer prodTotalScore) {
		this.prodTotalScore = prodTotalScore;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	
	

}
