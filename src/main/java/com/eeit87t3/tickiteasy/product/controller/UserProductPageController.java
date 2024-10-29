package com.eeit87t3.tickiteasy.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.service.ProductService;
import com.eeit87t3.tickiteasy.product.service.UserProdFavoritesService;
import com.eeit87t3.tickiteasy.util.JWTUtil;

@Controller
@RequestMapping
public class UserProductPageController {
	
    
    @Autowired
    private JWTUtil jwtUtil;
    
    @Autowired
    private MemberService memberService;

	 @Autowired
	    private ProductService productService;
	 
	 @Autowired
	    private UserProdFavoritesService favoritesService;
	 
//	  前台查詢會員商品收藏頁面
	    @GetMapping("/user/product/favorite")
	    public String getMemberFavorites(Model model) {
	        return "product/userProdFavorites";
	    }
	 
//	 @GetMapping("/user/product/favorite")
//	    public String getMemberFavorites(Model model, @RequestHeader("Authorization") String authHeader) {
//	        // 獲取會員身份信息
//	        String token = authHeader.replace("Bearer ", "");
//	        String email = jwtUtil.getEmailFromToken(token);
//	        Member member = memberService.findByEmail(email);
//	        
//	        // 如果會員存在，查詢收藏商品
//	        if (member != null) {
//	            List<ProductEntity> favorites = favoritesService.getMemberFavorites(member);
//	            model.addAttribute("favorites", favorites); // 傳遞收藏的商品列表給模板
//	        }
//
//	        return "product/userProdFavorites";  // 返回 Thymeleaf 模板頁面
//	    }
	
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
    @GetMapping("/user/cart")
    public String findAllProductsCart(Model model) {
        return "product/userCart";
    }
    

    
    
}
