package com.eeit87t3.tickiteasy.order.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tony475767
 */
public class ProductPackageForm {
	private String id;
	private String name;
	private BigDecimal amount;
	private List Products;
	
	public List getProducts() {
		return Products;
	}
	public void setProducts(List productForm) {
		Products = productForm;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
