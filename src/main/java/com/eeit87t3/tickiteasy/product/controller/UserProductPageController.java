package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeit87t3.tickiteasy.product.service.ProductService;

@Controller
@RequestMapping("/user")
public class UserProductPageController {

	 @Autowired
	    private ProductService productService;

	
	// 前台查詢所有商品頁面
    @GetMapping("/product")
    public String findAllProducts(Model model) {
    	 model.addAttribute("categoryList", productService.getProductCategories());
         model.addAttribute("tagList", productService.getProductTags());
        return "product/userFindAllProducts";
    }
	
    // 前台查詢單筆商品頁面
    @GetMapping("/product/{productID}")
    public String findProductDetailById(@PathVariable Integer productID, Model model) {
        model.addAttribute("productID", productID);
        return "product/userFindProductDetail";
    }
    
    // 前台查詢購物車頁面
    @GetMapping("/cart")
    public String findAllProductsCart(Model model) {
        return "product/userCart";
    }
    
    
}
