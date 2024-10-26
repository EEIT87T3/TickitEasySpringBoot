package com.eeit87t3.tickiteasy.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.order.entity.ProdOrders;

public interface ProdOrdersRepository extends JpaRepository<ProdOrders, Integer> {
	
	
	@Query(value = "SELECT TOP 1 * FROM ProdOrders ORDER BY prodOrderID DESC", nativeQuery = true)
	public ProdOrders findLatestProdOrder();
	
	@Query(value = "SELECT * FROM ProdOrders WHERE orderDate LIKE CONCAT('%', :number, '%')", nativeQuery = true)
	public Page<ProdOrders> findByDate(@Param("number") String number, Pageable pageable);

	@Query(value = "SELECT * FROM ProdOrders WHERE prodOrderId LIKE CONCAT('%', :number, '%')", nativeQuery = true)
	public Page<ProdOrders> findByProdOrdersId(@Param("number") Integer number, Pageable pageable);
	
	@Query(value = "SELECT * FROM ProdOrders WHERE memberId LIKE CONCAT('%', :number, '%')", nativeQuery = true)
	public Page<ProdOrders> findByMemberId(@Param("number") Integer number, Pageable pageable);
	
	//查詢訂單狀態 已付款
	@Query(value = "SELECT * FROM ProdOrders WHERE status = '已付款'", nativeQuery = true)
	public List<ProdOrders> findByStatusPaid();
	
	//查詢訂單狀態 已付款
	@Query(value = "SELECT * FROM ProdOrders WHERE status = '未付款'", nativeQuery = true)
	public List<ProdOrders> findByStatusNotPaid();
	
	//透過訂單號碼查詢
	@Query(value = "SELECT * FROM ProdOrders WHERE paymentInfo = :paymentInfo", nativeQuery = true)
	public ProdOrders findBypaymentInfo(String paymentInfo);
	
	//透過訂單號碼查詢
	@Query(value = "SELECT * FROM ProdOrders WHERE memberID = :memberID", nativeQuery = true)
	public List<ProdOrders> findOrdersByMemberId(@Param("memberID") Integer memberId);
}
