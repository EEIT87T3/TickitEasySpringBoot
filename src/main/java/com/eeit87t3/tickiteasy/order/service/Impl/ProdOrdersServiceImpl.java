package com.eeit87t3.tickiteasy.order.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.order.entity.ProdOrders;
import com.eeit87t3.tickiteasy.order.repository.ProdOrdersRepository;
import com.eeit87t3.tickiteasy.order.service.ProdOrdersService;


@Service
@Transactional
public class ProdOrdersServiceImpl implements ProdOrdersService{
	
	@Autowired
	private ProdOrdersRepository por;

	@Override
	public ProdOrders saveOrder(ProdOrders prodOrders) {
		return por.save(prodOrders);
	}

	@Override
	public void deleteOrderById(Integer prodOrderId) {
		por.deleteById(prodOrderId);
	}

	@Override
	public ProdOrders updateOrder(ProdOrders prodOrders) {
		
		Optional<ProdOrders> byId = por.findById(prodOrders.getProdOrderID());
		
		if(byId.isPresent()) {
			ProdOrders prodOrders2 = byId.get();
			
			prodOrders2.setMemberID(prodOrders.getMemberID());
			prodOrders2.setOrderDate(prodOrders.getOrderDate());
			prodOrders2.setPayments(prodOrders.getPayments());
			prodOrders2.setPaymenInfo(prodOrders.getPaymenInfo());
			prodOrders2.setStatus(prodOrders.getStatus());
			prodOrders2.setTotalAmount(prodOrders.getTotalAmount());
			prodOrders2.setShippingStatus(prodOrders.getShippingStatus());
			prodOrders2.setShippingID(prodOrders.getShippingID());
			prodOrders2.setRecipientName(prodOrders.getRecipientName());
			prodOrders2.setAddress(prodOrders.getAddress());
			prodOrders2.setPhone(prodOrders.getPhone());
			
			
			return por.save(prodOrders2);
		}
		
		return null;
	}

	@Override
	public ProdOrders selectOrderById(Integer id) {
		
		Optional<ProdOrders> byId = por.findById(id);
		
		if(byId.isPresent()) {
			return byId.get();
		}
		
		return null;
	}

	@Override
	public List<ProdOrders> selectAllOrder() {
		return por.findAll();
	}
	
	public ProdOrders findLatestProdOrder() {
		return por.findLatestProdOrder();
	}
	
	public Page<ProdOrders> findAllPage(Integer id,Integer records){
        Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
        return por.findAll(pageable);
    }
	
	public Page<ProdOrders> findAllPage(Integer id){
        Pageable pageable = PageRequest.of(id-1,5, Sort.Direction.ASC,"prodOrderID");
        return por.findAll(pageable);
    }

	@Override
	public Page<ProdOrders> findByDate(String number,Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByDate(number,pageable);
	}

	@Override
	public Page<ProdOrders> findByProdOrdersId(Integer number, Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByProdOrdersId(number, pageable);
	}

	@Override
	public Page<ProdOrders> findByMemberId(Integer number, Integer id,Integer records) {
		Pageable pageable = PageRequest.of(id-1,records, Sort.Direction.ASC,"prodOrderID");
		return por.findByMemberId(number, pageable);
	}
	
	
}
