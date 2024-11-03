package com.eeit87t3.tickiteasy.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

/**
 * @author Liang123456123
 */
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
		
		 // 查詢相同標籤的推薦商品（排除當前商品且只顯示上架商品）
	    @Query("SELECT p FROM ProductEntity p " +
	           "WHERE p.productTag.tagId = :tagId " +
	           "AND p.productID != :currentProductId " +
	           "AND p.status = 1 " +
	           "ORDER BY p.createdDate DESC " +
	           "LIMIT 3")  // 直接限制返回3筆推薦商品
	    List<ProductEntity> findRecommendedProductsByTag(
	        @Param("tagId") Integer tagId,
	        @Param("currentProductId") Integer currentProductId
	    );
		
		
}
