package com.eeit87t3.tickiteasy.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Liang123456123
 */
@Controller
@RequestMapping("/admin/product/productPhoto")
public class AdminProdcutPhotoPageController {
	
	//修改商品副圖頁面
	@GetMapping("/{productID}")
    public String editProductPhoto(@PathVariable Integer productID, Model model) {
        model.addAttribute("productID", productID);
        return "product/editProductPhoto"; 
    }

}
