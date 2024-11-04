package com.eeit87t3.tickiteasy.order.entity;

import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

/**
 * @author tony475767
 */
public class ClientSideOrderDetails {
	private Integer price;
	private Integer quantity;
	private ProductEntity productById;
	private TicketTypesEntity ticketTypeById;
	
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
	public ProductEntity getProductById() {
		return productById;
	}
	public void setProductById(ProductEntity productById) {
		this.productById = productById;
	}
	public TicketTypesEntity getTicketTypeById() {
		return ticketTypeById;
	}
	public void setTicketTypeById(TicketTypesEntity ticketTypeById) {
		this.ticketTypeById = ticketTypeById;
	}
}
