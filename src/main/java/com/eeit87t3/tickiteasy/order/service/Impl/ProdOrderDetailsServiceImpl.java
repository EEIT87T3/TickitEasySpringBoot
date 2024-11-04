package com.eeit87t3.tickiteasy.order.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;
import com.eeit87t3.tickiteasy.order.repository.ProdOrderDetailsRepository;
import com.eeit87t3.tickiteasy.order.service.ProdOrderDetailsService;

/**
 * @author tony475767
 */
@Service
public class ProdOrderDetailsServiceImpl implements ProdOrderDetailsService{

	@Autowired
	private ProdOrderDetailsRepository podr;
	
	@Override
	public List<ProdOrderDetails> findAllByIdA(Integer id) {
		return podr.findAllByIdA(id);
	}

}
