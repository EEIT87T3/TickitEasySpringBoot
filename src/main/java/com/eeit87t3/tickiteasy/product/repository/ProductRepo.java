package com.eeit87t3.tickiteasy.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {
	
	List<ProductEntity> findByProductNameContaining(String productName);

//	@Query("SELECT p FROM ProductEntity p LEFT JOIN p.productTag t ORDER BY t.tagName")
//    Page<ProductEntity> findAllOrderByProductTag(Pageable pageable);

//	@Query("SELECT p FROM ProductEntity p LEFT JOIN p.productTag t WHERE (:productName is null OR p.productName LIKE %:productName%) ORDER BY t.tagName")
//    Page<ProductEntity> findAllOrderByProductTagAndName(@Param("productName") String productName, Pageable pageable);
	
	@Query("SELECT p FROM ProductEntity p WHERE (:productName is null OR p.productName LIKE %:productName%) ORDER BY p.productID")
    Page<ProductEntity> findAllOrderByProductIdAndName(@Param("productName") String productName, Pageable pageable);
	
}
