package com.eeit87t3.tickiteasy.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {
	
		// 按照商品名稱進行模糊查詢的後台分頁
		Page<ProductEntity> findByProductNameContaining(String productName, Pageable pageable);
		
		// 前台首頁
		@Query("SELECT p FROM ProductEntity p " +
			       "WHERE (:categoryId IS NULL OR p.productCategory.categoryId = :categoryId) " +
			       "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
			       "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
			       "AND (p.status = 1 OR p.status = 2) " +
			       "AND (:productName IS NULL OR p.productName LIKE %:productName%)")
			Page<ProductEntity> findProductsByFilter(
			    @Param("categoryId") Integer categoryId,
			    @Param("minPrice") Integer minPrice,
			    @Param("maxPrice") Integer maxPrice,
			    @Param("productName") String productName,
			    Pageable pageable
			);
		
}
