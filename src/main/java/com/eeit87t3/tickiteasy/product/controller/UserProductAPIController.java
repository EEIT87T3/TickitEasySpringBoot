package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.product.entity.ProductCartItemEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;
import com.eeit87t3.tickiteasy.product.service.UserProductService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eeit87t3.tickiteasy.product.dto.ProductDTO;

@RestController
@RequestMapping("/user/api/product")
public class UserProductAPIController {
	
	@Autowired
	private UserProductService userProductService;
	

	
	//前台首頁查詢商品頁面
	@GetMapping
    public ResponseEntity<Page<ProductEntity>> findAllProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "0") int sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductEntity> products = userProductService.findProductsByFilter(
                categoryId, minPrice, maxPrice, productName, sort, page, size);

        return ResponseEntity.ok(products);
    }
	
	//前台查詢單筆商品頁面
    @GetMapping("/{productID}")
    public ResponseEntity<?> findProductDetailById(@PathVariable Integer productID) {
        try {
            ProductDTO productDTO = userProductService.findProductDetailById(productID);
            if (productDTO != null) {
                return ResponseEntity.ok(productDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with id " + productID + " not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while fetching the product: " + e.getMessage());
        }
    }
    
    // 取得推薦商品
    @GetMapping("/{productID}/recommend")
    public ResponseEntity<?> findRecommendedProducts(@PathVariable Integer productID) {
        try {
            List<ProductDTO> recommendedProducts = userProductService.findRecommendedProducts(productID);
            if (!recommendedProducts.isEmpty()) {
                return ResponseEntity.ok(recommendedProducts);
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching recommended products: " + e.getMessage());
        }
    }
    
    
    //新增商品到購物車
    @PostMapping
    public ResponseEntity<ProductCartItemEntity> addToCart(@RequestParam Integer productID, @RequestParam Integer quantity) {
        try {
        	ProductCartItemEntity cartItem = userProductService.addToCart(productID, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //修改購物車商品數量
    @PutMapping
    public ResponseEntity<ProductCartItemEntity> updateCartItem(@RequestBody ProductCartItemEntity cartItem, @RequestParam Integer newQuantity) {
    	ProductCartItemEntity updatedItem = userProductService.updateCartItemQuantity(cartItem, newQuantity);
        return ResponseEntity.ok(updatedItem);
    }
    
    

}
