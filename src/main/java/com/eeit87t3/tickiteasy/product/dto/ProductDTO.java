package com.eeit87t3.tickiteasy.product.dto;

import java.util.List;

public class ProductDTO {

	    private Integer productID;  // 用於編輯，新建時為null
	    private String productName;
	    private String productDesc;
	    private Integer price;
	    private Integer stock;
	    private Integer categoryId;
	    private Integer tagId;
	    private String productPic;  // 包含圖片路徑
	    private List<String> detailPhotos;  // 詳細圖片路徑列表
	    
		public Integer getProductID() {
			return productID;
		}
		public void setProductID(Integer productID) {
			this.productID = productID;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
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
		public Integer getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Integer categoryId) {
			this.categoryId = categoryId;
		}
		public Integer getTagId() {
			return tagId;
		}
		public void setTagId(Integer tagId) {
			this.tagId = tagId;
		}
		public String getProductPic() {
			return productPic;
		}
		public void setProductPic(String productPic) {
			this.productPic = productPic;
		}
		public List<String> getDetailPhotos() {
			return detailPhotos;
		}
		public void setDetailPhotos(List<String> detailPhotos) {
			this.detailPhotos = detailPhotos;
		}
	    
}
