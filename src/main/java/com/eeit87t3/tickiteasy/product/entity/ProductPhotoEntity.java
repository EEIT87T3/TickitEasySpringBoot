package com.eeit87t3.tickiteasy.product.entity;

/**
 * @author Liang123456123
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="productPhoto")
public class ProductPhotoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photoID")
	private Integer photoID;
	
	@Column(name = "fileName",nullable = false)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name = "productID",referencedColumnName = "productID", nullable = false)
	private ProductEntity product;
	
	public ProductPhotoEntity() {
	}

	public ProductPhotoEntity(String fileName, ProductEntity product) {
		this.fileName = fileName;
		this.product = product;
	}

	public ProductPhotoEntity(Integer photoID, String fileName, ProductEntity product) {
		this.photoID = photoID;
		this.fileName = fileName;
		this.product = product;
	}

	public Integer getPhotoID() {
		return photoID;
	}

	public void setPhotoID(Integer photoID) {
		this.photoID = photoID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

}
