package com.eeit87t3.tickiteasy.event.dto;

import java.util.List;

import com.eeit87t3.tickiteasy.event.entity.EventsEntity;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;

public class EventWithTicketTypesDTO {

	private EventsEntity event;
	private List<TicketTypesEntity> ticketTypes;
	
	public EventWithTicketTypesDTO(EventsEntity eventsEntity) {
		this.event = eventsEntity;
		this.ticketTypes = eventsEntity.getTicketTypes();
	}

	public EventsEntity getEvent() {
		return event;
	}
	public List<TicketTypesEntity> getTicketTypes() {
		return ticketTypes;
	}
}
