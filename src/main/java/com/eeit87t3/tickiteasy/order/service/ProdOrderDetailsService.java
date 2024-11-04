package com.eeit87t3.tickiteasy.order.service;

import java.util.List;

import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;

/**
 * @author tony475767
 */
public interface ProdOrderDetailsService {
	
	List<ProdOrderDetails> findAllByIdA(Integer id);
}
