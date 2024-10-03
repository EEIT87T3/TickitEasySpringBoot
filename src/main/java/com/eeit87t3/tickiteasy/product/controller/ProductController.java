package com.eeit87t3.tickiteasy.product.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/getAllProducts")
    public String getAllProducts(@RequestParam(required = false) String productName, Model model) {
        List<ProductEntity> products;
        if (productName != null && !productName.isEmpty()) {
            products = productService.findProductByName(productName);
        } else {
            products = productService.findAllProducts();
        }
        model.addAttribute("product", products);
        return "product/GetAllProducts";
    }
	
//    
//    @GetMapping("/getAllProducts")
//    public String getAllProducts(Model model) {
//    	List<ProductEntity> allProducts = productService.findAllProducts();
//    	model.addAttribute("product", allProducts);
//    	return "product/GetAllProducts";
//    }
//    
    @PostMapping("deleteProductById")
    public String deleteProductById(int productID) throws IOException  {
        productService.deleteProductById(productID);
        return "redirect:/getAllProducts";
    }
    
//    @GetMapping("/findProducts")
//    public String searchProducts(@RequestParam String productName, Model model) {
//        List<ProductEntity> products = productService.findProductByName(productName);
//        model.addAttribute("products", products);
//        return "redirect:/getAllProducts";
//    }
    
    @GetMapping("add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        model.addAttribute("categories", productService.getProductCategories());
        model.addAttribute("tags", productService.getProductTags());
        return "product/AddProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductEntity product, 
                              MultipartFile imageFile,
                              Integer categoryId, 
                              Integer tagId) throws IOException {
        productService.addProduct(product, imageFile, categoryId, tagId);
        return "redirect:/getAllProducts";
    }
    

    @GetMapping("getProductForUpdate")
    public String getProductForUpdate(int productID, Model model) {
        ProductEntity product = productService.findProductById(productID);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", productService.getProductCategories());
            model.addAttribute("tags", productService.getProductTags());
            return "product/UpdateProduct";
        } else {
            // 處理產品不存在的情況
            return "redirect:/getAllProducts";
        }
    }
    
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductEntity product,
				    						  int productID,
				    						  MultipartFile imageFile,
				                              Integer categoryId, 
				                              Integer tagId
    		) throws IOException {
        productService.updateProductById(productID, product, imageFile, categoryId, tagId);
        return "redirect:/getAllProducts";
    }
    
}
