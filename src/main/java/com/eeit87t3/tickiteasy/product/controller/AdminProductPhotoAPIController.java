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

@RestController
@RequestMapping("/admin/api/productPhoto")
public class AdminProductPhotoAPIController {
	
	@Autowired
	ProductPhotoService productPhotoService;
	
	 @Autowired
	    private ProductService productService;


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
