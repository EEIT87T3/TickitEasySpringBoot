package com.eeit87t3.tickiteasy.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import com.eeit87t3.tickiteasy.order.entity.ProdOrders;

public interface ProdOrdersService {
	public ProdOrders saveOrder(ProdOrders prodOrders); //INSERT 新增 依照ProdOrders
	public void deleteOrderById(Integer prodOrderID); //DELETE 刪除  依照prodOrderID
	public ProdOrders updateOrder(ProdOrders prodOrders); //UPDATE 修改 依照ProdOrders
	public ProdOrders selectOrderById(Integer prodOrderID); //SELECT 單筆搜尋 依照prodOrderID
	public List<ProdOrders> selectAllOrder(); //SELECTALL 多筆搜尋 空參 (搜尋該會員全部訂單 SELECTALL)
	public ProdOrders findLatestProdOrder(); //找最新一筆訂單ID
	public List<ProdOrders> findOrdersByMemberId(Integer number); //SELECT 搜尋會員訂單 依照memberID
	
	public Page<ProdOrders> findByDate(String number,Integer id,Integer records);
	public Page<ProdOrders> findByProdOrdersId(Integer number, Integer id,Integer records);
	public Page<ProdOrders> findByMemberId(Integer number, Integer id,Integer records);
	
	public String ECPay(List<Map<String,Object>> ticketTypesCartToCheckoutJson,List<Map<String,Object>> checkoutItems,String totalAmount,String memberEmail);
	public String LinePay(List<Map<String,Object>> ticketTypesCartToCheckoutJson,List<Map<String,Object>> checkoutItems,String totalAmount,String memberEmail) throws Exception;
}
