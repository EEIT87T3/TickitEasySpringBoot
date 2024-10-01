package com.eeit87t3.tickiteasy.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {

}
