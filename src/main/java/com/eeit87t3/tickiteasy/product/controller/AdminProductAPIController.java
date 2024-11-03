package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.product.dto.ProductDTO;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;

/**
 * @author Liang123456123
 */
@RestController
@RequestMapping("/admin/api/product")
public class AdminProductAPIController {
	
	@Autowired
	ProductService productService;
	
	// 获取各标签的商品库存数，用于圆饼图
    @GetMapping("/tag-stock")
    public ResponseEntity<Map<String, Integer>> getProductTagStock() {
        Map<String, Integer> tagStockData = productService.getProductTagStock();
        return ResponseEntity.ok(tagStockData);
    }
    
	
	// 按照商品名稱進行模糊查詢的分頁
    @GetMapping
    public ResponseEntity<Page<ProductEntity>> findAllProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getProductsByName(name, page, size));
    }
    
    // 修改商品狀態
    @PutMapping("/{productID}/status")
    @ResponseBody
    public ResponseEntity<?> editProductStatusByID(@PathVariable("productID") int productID,
                                                   @RequestBody ProductEntity updatedProduct) {
        try {
            ProductEntity editProductStatusById = productService.editProductStatusById(productID, updatedProduct);
            return ResponseEntity.ok(editProductStatusById);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error processing request: " + e.getMessage());
        }
    }
    
    // 修改商品
    @PutMapping("/{productID}")
    @ResponseBody
    public ResponseEntity<?> editProductByID(@PathVariable("productID") int productID,
    		@ModelAttribute ProductDTO productDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile){
    	try {
            ProductEntity editProductById = productService.editProductById(productID, productDTO, imageFile);
            return ResponseEntity.status(HttpStatus.OK).body(editProductById);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("商品名稱重複");
        }
    	
    }
    
    // 刪除商品
    @DeleteMapping("/{productID}")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable("productID") int productID) throws IOException {
        try {
            productService.deleteProductById(productID);
            return ResponseEntity.ok().body("商品已成功刪除");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除商品時發生錯誤");
        }
    }
    
    // 新增商品
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            ProductEntity createdProduct = productService.createProduct(productDTO, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("商品名稱重複");
        }
    }
 

}
