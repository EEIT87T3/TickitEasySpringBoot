package com.eeit87t3.tickiteasy.event.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.event.dto.EventsSearchingDTO;
import com.eeit87t3.tickiteasy.event.entity.EventsEntity;

@Service
public class UserEventsService {

	@Autowired
	private EventsProcessingService eventsProcessingService;
	
	
	public Page<EventsEntity> findBySpecification(EventsSearchingDTO eventsSearchingDTO) {
		eventsSearchingDTO.setStatuses(Arrays.asList((short) 1, (short) 2));  // 前台只能查詢到已上架的活動
		return eventsProcessingService.findBySpecification(eventsSearchingDTO);
	}
	
}
