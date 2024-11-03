package com.eeit87t3.tickiteasy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.event.dto.TicketTypesDTO;
import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;
import com.eeit87t3.tickiteasy.event.service.AdminTicketTypesService;

/**
 * @author Chuan (chuan13)
 */
@RestController
@RequestMapping("/admin/api/event/{eventID}/tickettype")
public class AdminTicketTypesJsonController {
	
	@Autowired
	private AdminTicketTypesService ticketTypesService;

	@PostMapping
	public ResponseEntity<?> create(@PathVariable Integer eventID,
			@ModelAttribute TicketTypesDTO createTicketTypesDTO) {
		createTicketTypesDTO.setEventID(eventID);
		String validateCreateInput = ticketTypesService.validateCreateInput(createTicketTypesDTO);
		if ("輸入正確！".equals(validateCreateInput)) {
			return new ResponseEntity<TicketTypesEntity>(ticketTypesService.create(createTicketTypesDTO), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>(validateCreateInput, HttpStatus.BAD_REQUEST);
		}
	};
	
	@PutMapping("/{ticketTypeID}")
	public ResponseEntity<?> edit(@PathVariable Integer eventID,
			@PathVariable Integer ticketTypeID,
			@ModelAttribute TicketTypesDTO editTicketTypesDTO) {
		String validateEditInput = ticketTypesService.validateEditInput(editTicketTypesDTO);
		if ("輸入正確！".equals(validateEditInput)) {
			return new ResponseEntity<TicketTypesEntity>(ticketTypesService.edit(editTicketTypesDTO), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(validateEditInput, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{ticketTypeID}")
	public ResponseEntity<?> delete(@PathVariable Integer ticketTypeID) {
		Boolean result = ticketTypesService.delete(ticketTypeID);
		if (result) {
			return new ResponseEntity<String>("刪除成功！", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("刪除失敗。", HttpStatus.BAD_REQUEST);
		}
	}
}
