package com.eeit87t3.tickiteasy.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.product.dto.ProductDTO;
import com.eeit87t3.tickiteasy.product.entity.ProductCartItemEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductPhotoEntity;
import com.eeit87t3.tickiteasy.product.repository.ProdFavoritesRepo;
import com.eeit87t3.tickiteasy.product.repository.ProductPhotoRepo;
import com.eeit87t3.tickiteasy.product.repository.ProductRepo;

@Service
public class UserProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ProductPhotoRepo productPhotoRepo;
	
	@Autowired
	private ProdFavoritesRepo prodFavoritesRepo;
	
	
	
	 //前台首頁查詢商品頁面
    public Page<ProductEntity> findProductsByFilter(Integer categoryId, Integer minPrice, Integer maxPrice,
            String productName, int sort, int page, int size) {
		// 創建排序對象
		Sort sortObject;
		switch (sort) {
		case 1:
		sortObject = Sort.by(Sort.Direction.ASC, "price");
		break;
		case 2:
		sortObject = Sort.by(Sort.Direction.DESC, "price");
		break;
		case 3:
		sortObject = Sort.by(Sort.Direction.DESC, "createdDate");
		break;
		default:
		// 默認按照創建日期由舊到新排序
		sortObject = Sort.by(Sort.Direction.ASC, "createdDate");
		}
		// 創建分頁請求，包含排序信息
		Pageable pageable = PageRequest.of(page, size, sortObject);
		return productRepo.findProductsByFilter(categoryId, minPrice, maxPrice, productName, pageable);
		}
    
    //前台查詢單筆商品
    @Transactional
    public ProductDTO findProductDetailById(Integer productID) {
        Optional<ProductEntity> optional = productRepo.findById(productID);

        if(optional.isPresent()) {
            ProductEntity entity = optional.get();
            ProductDTO dto = convertToDTO(entity);
            
            // 獲取並設置詳細圖片
            List<ProductPhotoEntity> photos = productPhotoRepo.findByProduct(entity);
            List<String> detailPhotoUrls = photos.stream()
                .map(ProductPhotoEntity::getFileName)
                .collect(Collectors.toList());
            dto.setDetailPhotos(detailPhotoUrls);
            
            return dto;
        }

        return null;
    }

    private ProductDTO convertToDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setProductID(entity.getProductID());
        dto.setProductName(entity.getProductName());
        dto.setProductDesc(entity.getProductDesc());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCategoryId(entity.getProductCategory().getCategoryId());
        dto.setTagId(entity.getProductTag() != null ? entity.getProductTag().getTagId() : null);
        dto.setProductPic(entity.getProductPic());
        // 注意：詳細圖片在這裡不設置，而是在 findProductById 方法中設置
        
        dto.setStatus(entity.getStatus());
        return dto;
    }
    
    // 查詢推薦商品
    @Transactional
    public List<ProductDTO> findRecommendedProducts(Integer productID) {
        // 先取得當前商品以獲取其tagId
        Optional<ProductEntity> currentProduct = productRepo.findById(productID);
        
        if (currentProduct.isPresent() && currentProduct.get().getProductTag() != null) {
            Integer tagId = currentProduct.get().getProductTag().getTagId();
            
            // 查詢推薦商品
            List<ProductEntity> recommendedProducts = productRepo
                .findRecommendedProductsByTag(tagId, productID);
            
            // 轉換為DTO
            return recommendedProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
    
	
    //加入購物車
    public ProductCartItemEntity addToCart(Integer productID, Integer quantity) {
        Optional<ProductEntity> productOptional = productRepo.findById(productID);
        
        if (productOptional.isPresent()) {
            ProductEntity product = productOptional.get();
            
            ProductCartItemEntity cartItem = new ProductCartItemEntity();
            cartItem.setProductID(product.getProductID());
            cartItem.setProductName(product.getProductName());
            cartItem.setProductPic(product.getProductPic());
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(quantity);
            
            return cartItem;
        } else {
            throw new RuntimeException("商品不存在");
        }
    }
    
    ////修改購物車商品數量
    public ProductCartItemEntity updateCartItemQuantity(ProductCartItemEntity cartItem, Integer newQuantity) {
        cartItem.setQuantity(newQuantity);
        return cartItem;
    }
    
    

}
