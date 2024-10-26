package com.eeit87t3.tickiteasy.event.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.eeit87t3.tickiteasy.event.dto.EventWithTicketTypesDTO;
import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;

@Service
public class UserEventsService {

	@Autowired
	private EventsProcessingService eventsProcessingService;
	
	
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		eventsSearchingDTO.setStatuses(Arrays.asList((short) 1, (short) 2));  // 前台只能查詢到已上架的活動
		eventsSearchingDTO.setPageSize(12);  // 前台的單頁筆數固定為 12
		return eventsProcessingService.findBySpecification(eventsSearchingDTO);
	}
	
	public EventWithTicketTypesDTO findById(Integer eventID) {
		return new EventWithTicketTypesDTO(eventsProcessingService.findById(eventID));
	}
	
}
