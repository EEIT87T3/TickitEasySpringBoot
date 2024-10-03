package com.eeit87t3.tickiteasy.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {
	
	List<ProductEntity> findByProductNameContaining(String productName);
}
