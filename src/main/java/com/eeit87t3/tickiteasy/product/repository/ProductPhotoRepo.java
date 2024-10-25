package com.eeit87t3.tickiteasy.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductPhotoEntity;

public interface ProductPhotoRepo extends JpaRepository<ProductPhotoEntity, Integer> {
	 List<ProductPhotoEntity> findByProduct(ProductEntity product);
}
