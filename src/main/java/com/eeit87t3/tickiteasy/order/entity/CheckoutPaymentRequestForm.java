package com.eeit87t3.tickiteasy.order.entity;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutPaymentRequestForm {
	private BigDecimal amount;
	private String currency;
	private String orderId;
	private List packages;
	private RedirectUrls redirectUrls;
	
	public RedirectUrls getRedirectUrls() {
		return redirectUrls;
	}
	public void setRedirectUrls(RedirectUrls redirectUrls) {
		this.redirectUrls = redirectUrls;
	}
	public List getPackages() {
		return packages;
	}
	public void setPackages(List packages) {
		this.packages = packages;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
	
}
