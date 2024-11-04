package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductPhotoEntity;
import com.eeit87t3.tickiteasy.product.service.ProductPhotoService;
import com.eeit87t3.tickiteasy.product.service.ProductService;

import java.io.IOException;
import java.util.List;

/**
 * @author Liang123456123
 */
@RestController
@RequestMapping("/admin/api/productPhoto")
public class AdminProductPhotoAPIController {
	
	@Autowired
	ProductPhotoService productPhotoService;
	
	 @Autowired
	    private ProductService productService;
	 
	 // 新增產品圖片
	    @PostMapping("/{productID}")
	    public ResponseEntity<ProductPhotoEntity> createProductPhoto(
	            @PathVariable Integer productID,
	            @RequestPart(value = "fileName", required = false) MultipartFile fileName)
	    {
	        try {
	            ProductEntity product = productService.findProductById(productID);
	            if (product == null) {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	            ProductPhotoEntity newPhoto = productPhotoService.createProductPhoto(fileName, product);
	            return new ResponseEntity<>(newPhoto, HttpStatus.CREATED);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	 // 更新產品圖片
	    @PutMapping("/{photoID}")
	    public ResponseEntity<ProductPhotoEntity> editProductPhoto(
	            @PathVariable Integer photoID,
	            @RequestPart(value = "fileName", required = false) MultipartFile fileName) {
	        try {
	            ProductPhotoEntity updatedPhoto = productPhotoService.editProductPhoto(photoID, fileName);
	            if (updatedPhoto == null) {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	            return new ResponseEntity<>(updatedPhoto, HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }


	    // 獲取特定產品的所有圖片
	    @GetMapping("/product/{productID}")
	    public ResponseEntity<List<ProductPhotoEntity>> findProductPhotos(@PathVariable Integer productID) {
	        ProductEntity product = productService.findProductById(productID);
	        if (product == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	        List<ProductPhotoEntity> photos = productPhotoService.getProductPhotos(product);
	        return new ResponseEntity<>(photos, HttpStatus.OK);
	    }


	    // 刪除產品圖片
	    @DeleteMapping("/{photoID}")
	    public ResponseEntity<Void> deleteProductPhoto(@PathVariable Integer photoID) {
	        try {
	            productPhotoService.deleteProductPhoto(photoID);
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } catch (IOException e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
