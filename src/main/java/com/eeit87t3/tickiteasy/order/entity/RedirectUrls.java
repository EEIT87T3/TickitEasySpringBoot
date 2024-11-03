package com.eeit87t3.tickiteasy.order.entity;

/**
 * @author tony475767
 */
public class RedirectUrls {
	private String appPackageName;
	private String confirmUrl;
	private String confirmUrlType;
	private String cancelUrl;
	public String getAppPackageName() {
		return appPackageName;
	}
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}
	public String getConfirmUrl() {
		return confirmUrl;
	}
	public void setConfirmUrl(String confirmUrl) {
		this.confirmUrl = confirmUrl;
	}
	public String getConfirmUrlType() {
		return confirmUrlType;
	}
	public void setConfirmUrlType(String confirmUrlType) {
		this.confirmUrlType = confirmUrlType;
	}
	public String getCancelUrl() {
		return cancelUrl;
	}
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}
}
