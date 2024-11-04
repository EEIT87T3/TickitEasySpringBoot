package com.eeit87t3.tickiteasy.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.event.entity.TicketTypesEntity;

/**
 * @author Chuan (chuan13)
 */
public interface TicketTypesRepo extends JpaRepository<TicketTypesEntity, Integer> {

}
