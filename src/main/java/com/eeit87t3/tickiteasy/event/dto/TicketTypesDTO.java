package com.eeit87t3.tickiteasy.event.dto;

import java.time.LocalDateTime;

/**
 * 「新增票種」或「編輯票種」時使用的 DTO。
 * 
 * @author Chuan (chuan13)
 */
public class TicketTypesDTO {

	private Integer ticketTypeID;
	private Integer eventID;
	private Short ticketTypeNo;
	private String typeName;
	private String typeDesc;
	private Integer price;
	private Integer quantityAvailable;
	private LocalDateTime startSaleTime;
	private LocalDateTime endSaleTime;
	
	public Integer getTicketTypeID() {
		return ticketTypeID;
	}
	public void setTicketTypeID(Integer ticketTypeID) {
		this.ticketTypeID = ticketTypeID;
	}
	public Integer getEventID() {
		return eventID;
	}
	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
	public Short getTicketTypeNo() {
		return ticketTypeNo;
	}
	public void setTicketTypeNo(Short ticketTypeNo) {
		this.ticketTypeNo = ticketTypeNo;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuantityAvailable() {
		return quantityAvailable;
	}
	public void setQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	public LocalDateTime getStartSaleTime() {
		return startSaleTime;
	}
	public void setStartSaleTime(LocalDateTime startSaleTime) {
		this.startSaleTime = startSaleTime;
	}
	public LocalDateTime getEndSaleTime() {
		return endSaleTime;
	}
	public void setEndSaleTime(LocalDateTime endSaleTime) {
		this.endSaleTime = endSaleTime;
	}
	
	@Override
	public String toString() {
		return "TicketTypesDTO [ticketTypeID=" + ticketTypeID + ", eventID=" + eventID + ", ticketTypeNo="
				+ ticketTypeNo + ", typeName=" + typeName + ", typeDesc=" + typeDesc + ", price=" + price
				+ ", quantityAvailable=" + quantityAvailable + ", startSaleTime=" + startSaleTime + ", endSaleTime="
				+ endSaleTime + "]";
	}
}
