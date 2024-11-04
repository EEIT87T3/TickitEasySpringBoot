package com.eeit87t3.tickiteasy.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.order.entity.ProdOrderDetails;

/**
 * @author tony475767
 */
public interface ProdOrderDetailsRepository extends JpaRepository<ProdOrderDetails, Integer>{
	
	@Query(value = "SELECT * FROM ProdOrderDetails WHERE prodOrderID = :prodOrderID",nativeQuery = true)
	List<ProdOrderDetails> findAllByIdA(@Param("prodOrderID") Integer id);

}
