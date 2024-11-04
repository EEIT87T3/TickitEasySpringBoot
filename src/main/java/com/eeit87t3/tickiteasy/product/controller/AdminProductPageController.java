package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductPhotoEntity;
import com.eeit87t3.tickiteasy.product.service.ProductPhotoService;
import com.eeit87t3.tickiteasy.product.service.ProductService;

/**
 * @author Liang123456123
 */
@Controller
@RequestMapping("/admin/product")
public class AdminProductPageController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	private ProductPhotoService productPhotoService;
	
	 // 商品圖表分析頁面
    @GetMapping("/analytics")
    public String memberAnalytics() {
        return "product/productAnalytics"; // 返回商品分析頁面
    }
	
	
	// 查詢所有商品頁面
    @GetMapping
    public String findAllProducts() {
        return "product/findAllProducts";
    }
    
    // 顯示新增商品頁面
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("categoryList", productService.getProductCategories());
        model.addAttribute("tagList", productService.getProductTags());
        return "product/createProduct";
    }
    
    // 修改商品頁面
    @GetMapping("/{productID}/edit")
    public String editProduct(@PathVariable("productID") int productID, Model model) {
        ProductEntity product = productService.findProductById(productID);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", productService.getProductCategories());
            model.addAttribute("tags", productService.getProductTags());
            
            // 獲取並添加商品詳細圖片
            List<ProductPhotoEntity> productPhotos = productPhotoService.getProductPhotos(product);
            model.addAttribute("productPhotos", productPhotos);
            
            return "product/editProduct";
        } else {
            return "redirect:/admin/product";
        }
    }
	
}
