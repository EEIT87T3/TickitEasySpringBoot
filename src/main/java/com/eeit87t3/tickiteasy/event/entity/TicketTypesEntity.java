package com.eeit87t3.tickiteasy.event.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Chuan (chuan13)
 */
@Entity @Table(name = "ticketTypes")
public class TicketTypesEntity {
	
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketTypeID")
    private Integer ticketTypeID;

    @Column(name = "status", nullable = false)
    private Short status = 0;

    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private EventsEntity event;

    @Column(name = "ticketTypeNo")
    private Short ticketTypeNo;
    
    @Column(name = "typeName", nullable = false, length = 60)
    private String typeName;

    @Column(name = "typeDesc", length = 60)
    private String typeDesc;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantityAvailable")
    private Integer quantityAvailable;

    @Column(name = "quantityPurchased", nullable = false)
    private Integer quantityPurchased = 0;

    @Column(name = "startSaleTime", nullable = false)
    private LocalDateTime startSaleTime;
    
    @Column(name = "endSaleTime", nullable = false)
    private LocalDateTime endSaleTime;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketType")
//    @JsonIgnore
//    private List<TicketOrderDetails> ticketOrderDetails = new ArrayList<>();
    
	public TicketTypesEntity() {
		super();
	}
	
	public Integer getTicketTypeID() {
		return ticketTypeID;
	}
	public void setTicketTypeID(Integer ticketTypeID) {
		this.ticketTypeID = ticketTypeID;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public EventsEntity getEvent() {
		return event;
	}
	public void setEvent(EventsEntity event) {
		this.event = event;
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
	public Integer getQuantityPurchased() {
		return quantityPurchased;
	}
	public void setQuantityPurchased(Integer quantityPurchased) {
		this.quantityPurchased = quantityPurchased;
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
//	public List<TicketOrderDetails> getTicketOrderDetails() {
//		return ticketOrderDetails;
//	}
//	public void setTicketOrderDetails(List<TicketOrderDetails> ticketOrderDetails) {
//		this.ticketOrderDetails = ticketOrderDetails;
//	}

	@Override
	public String toString() {
		return "TicketTypesEntity [ticketTypeID=" + ticketTypeID + ", status=" + status + ", ticketTypeNo=" + ticketTypeNo + ", typeName=" + typeName + ", typeDesc=" + typeDesc + ", price="
				+ price + ", quantityAvailable=" + quantityAvailable + ", quantityPurchased=" + quantityPurchased
				+ ", startSaleTime=" + startSaleTime + ", endSaleTime=" + endSaleTime + "]";
	}
}
