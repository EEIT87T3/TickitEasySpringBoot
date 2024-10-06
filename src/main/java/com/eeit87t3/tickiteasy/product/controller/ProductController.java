package com.eeit87t3.tickiteasy.product.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping
    public String getAllProducts(@RequestParam(required = false) String productName, 
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 Model model) {
        Page<ProductEntity> productPage = productService.findAllProductsPageSortedByIdAndName(productName, page, size);
        
        model.addAttribute("product", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("productName", productName);
        
        return "product/GetAllProducts";
    }

	@DeleteMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") int productID) throws IOException {
        productService.deleteProductById(productID);
        return "redirect:/admin/product";
    }
    
    @GetMapping("/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        model.addAttribute("categories", productService.getProductCategories());
        model.addAttribute("tags", productService.getProductTags());
        return "product/AddProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductEntity product, 
            @RequestParam MultipartFile imageFile,
            @RequestParam Integer categoryId, 
            @RequestParam Integer tagId) throws IOException {
			productService.createProduct(product, imageFile, categoryId, tagId);
			return "redirect:/admin/product";
			}
    
    // 查詢單筆
       @GetMapping("/{id}")
       public String findProductById(@PathVariable("id") int productID, Model model) {
           ProductEntity product = productService.findProductById(productID);
           if (product != null) {
               model.addAttribute("product", product);
               model.addAttribute("categories", productService.getProductCategories());
               model.addAttribute("tags", productService.getProductTags());
               return "product/FindProduct";  // 查看單個產品的頁面
           } else {
               return "redirect:/admin/product";
           }
       }
    
 // 修改（顯示表單）
    @GetMapping("/{id}/edit")
    public String getProductForEdit(@PathVariable("id") int productID, Model model) {
        ProductEntity product = productService.findProductById(productID);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("categories", productService.getProductCategories());
            model.addAttribute("tags", productService.getProductTags());
            return "product/UpdateProduct";
        } else {
            return "redirect:/admin/product";
        }
    }

    // 修改（處理表單提交）
    @PutMapping("/{id}/edit")
    public String editProductById(@PathVariable("id") int productID,
                                @ModelAttribute ProductEntity product,
                                @RequestParam MultipartFile imageFile,
                                @RequestParam Integer categoryId, 
                                @RequestParam Integer tagId) throws IOException {
        productService.editProductById(productID, product, imageFile, categoryId, tagId);
        return "redirect:/admin/product";
    }

 
    
}
