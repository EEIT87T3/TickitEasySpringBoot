package com.eeit87t3.tickiteasy.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.order.entity.ProdOrders;

public interface ProdOrdersService {
	public ProdOrders saveOrder(ProdOrders prodOrders); //INSERT 新增 依照ProdOrders
	public void deleteOrderById(Integer prodOrderID); //DELETE 刪除  依照prodOrderID
	public ProdOrders updateOrder(ProdOrders prodOrders); //UPDATE 修改 依照ProdOrders
	public ProdOrders selectOrderById(Integer prodOrderID); //SELECT 單筆搜尋 依照prodOrderID
	public List<ProdOrders> selectAllOrder(); //SELECTALL 多筆搜尋 空參 (搜尋該會員全部訂單 SELECTALL)
	public ProdOrders findLatestProdOrder(); //找最新一筆訂單ID
	
	public Page<ProdOrders> findByDate(String number,Integer id,Integer records);
	public Page<ProdOrders> findByProdOrdersId(Integer number, Integer id,Integer records);
	public Page<ProdOrders> findByMemberId(Integer number, Integer id,Integer records);
//	public ProdOrdersBean selectByShippingId(int shippingID);//SELECT 單筆搜尋 2.依照shippingID
//	
//	public List<ProdOrdersBean> selectAllByMemberId(int memberIDParameter); //SELECTALL 多筆搜尋 2.依照memberID
//	public List<ProdOrdersBean> selectAllByOrderDate(Date orderDate); //SELECTALL 多筆搜尋 3.依照orderDate
//	public List<ProdOrdersBean> selectAllByStatus(String status);//SELECTALL 多筆搜尋 4.依照status
//	public List<ProdOrdersBean> selectAllByShippingStatus(String shippingStatus);//SELECTALL 多筆搜尋 5.依照shippingStatus
}
