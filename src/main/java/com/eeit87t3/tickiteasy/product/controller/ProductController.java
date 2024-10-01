package com.eeit87t3.tickiteasy.product.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
    
    @GetMapping("/getAllProducts")
    public String getAllProducts(Model model) {
    	List<ProductEntity> allProducts = productService.findAllProducts();
    	model.addAttribute("product", allProducts);
    	return "product/GetAllProducts";
    }
    
    @PostMapping("deleteProductById")
    public String deleteProductById(@RequestParam("productID")int productID) throws IOException  {
        productService.deleteProductById(productID);
        return "redirect:/getAllProducts";
    }
    
    
    
}
