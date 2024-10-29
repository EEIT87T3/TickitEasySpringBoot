package com.eeit87t3.tickiteasy.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.product.dto.ProductDTO;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.repository.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
    private ImageUtil imageUtil;
	
	@Autowired
    private CategoryService categoryService;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private ProdEmailService prodEmailService;
    
    public List<CategoryEntity> getProductCategories() {
        return categoryService.findProductCategoryList();
    }

    public List<TagEntity> getProductTags() {
        return tagService.findProductTagList();
    }
    
 // 修改商品狀態
    @Transactional
    public ProductEntity editProductStatusById(Integer productID, ProductEntity updatedProduct) throws IOException {
    	Optional<ProductEntity> optionalProduct = productRepo.findById(productID);
        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            
            // 記錄商品原始狀態
            int oldStatus = product.getStatus();
            
            switch (updatedProduct.getStatus()) {
                case 0:
                    product.setStatus(0); // 下架
                    break;
                case 1:
                    product.setStatus(1); // 上架
                    break;
                case 2:
                    product.setStatus(2); // 補貨中
                    break;
                default:
                    throw new IllegalArgumentException("無效的狀態值。狀態必須是 0（下架）、1（上架）或 2（補貨中）");
            }
            
         // 儲存更新
            ProductEntity savedProduct = productRepo.save(product);
            
            // 如果狀態從2(補貨中)改為1(上架)，發送補貨通知
            if (oldStatus == 2 && updatedProduct.getStatus() == 1) {
            	prodEmailService.sendRestockNotification(product);
            }
            
            return savedProduct;
        } else {
            throw new RuntimeException("找不到ID為 " + productID + " 的商品");
        }
    }
    
 // 新增商品
    @Transactional
    public ProductEntity createProduct(ProductDTO productDTO, MultipartFile imageFile) throws IOException {
        ProductEntity product = new ProductEntity();
        // 設置基本属性
        product.setProductName(productDTO.getProductName());
        product.setProductDesc(productDTO.getProductDesc());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        // 設置分類和標籤
        CategoryEntity category = categoryService.findProductCategoryById(productDTO.getCategoryId());
        TagEntity tag = tagService.findProductTagById(productDTO.getTagId());
        product.setProductCategory(category);
        product.setProductTag(tag);
        
        ProductEntity savedProduct = productRepo.save(product);
        // 處理圖片
        if (imageFile != null && !imageFile.isEmpty()) {
            String baseName = UUID.randomUUID().toString();
            String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, baseName);
            savedProduct.setProductPic(imagePath);
            savedProduct = productRepo.save(savedProduct);
        }
        return savedProduct;
    }
    
 // 修改商品
    @Transactional
    public ProductEntity editProductById(Integer productID, ProductDTO productDTO, MultipartFile imageFile) throws IOException {
        Optional<ProductEntity> optional = productRepo.findById(productID);

        if (optional.isPresent()) {
            ProductEntity product = optional.get();

            // 更新基本属性
            product.setProductName(productDTO.getProductName());
            product.setProductDesc(productDTO.getProductDesc());
            product.setPrice(productDTO.getPrice());
            product.setStock(productDTO.getStock());

            // 更新分類和標籤
            CategoryEntity category = categoryService.findProductCategoryById(productDTO.getCategoryId());
            TagEntity tag = tagService.findProductTagById(productDTO.getTagId());
            product.setProductCategory(category);
            product.setProductTag(tag);

            // 處理圖片
            if (imageFile != null && !imageFile.isEmpty()) {
                if (product.getProductPic() != null) {
                    imageUtil.deleteImage(product.getProductPic());
                }
                String baseName = UUID.randomUUID().toString();
                String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, baseName);
                product.setProductPic(imagePath);
            } else if (productDTO.getProductPic() != null) {
                product.setProductPic(productDTO.getProductPic());
            }

            return productRepo.save(product);
        }

        throw new RuntimeException("Product not found");
    }
    
    //刪除商品
    @Transactional
    public void deleteProductById(Integer productID) throws IOException {
        Optional<ProductEntity> optional = productRepo.findById(productID);
        if (optional.isPresent()) {
            ProductEntity product = optional.get();
            if (product.getProductPic() != null) {
                try {
                    imageUtil.deleteImage(product.getProductPic());
                } catch (IOException e) {
                	System.out.println("刪除商品圖片時發生錯誤: " + e.getMessage());
                	e.printStackTrace(); 
                    // 即使圖片刪除失敗，我們仍然繼續刪除數據庫記錄
                }
            }
            try {
                productRepo.deleteById(productID);
            } catch (Exception e) {
            	System.out.println("從資料庫刪除商品時發生錯誤: " + e.getMessage()); //商品Id被訂單FK和商品圖片FK綁住
            	e.printStackTrace(); 
                throw new RuntimeException("刪除商品失敗", e);
            }
        } else {
        	System.out.println("嘗試刪除不存在的商品，ID: " + productID);
            throw new RuntimeException("商品不存在，ID: " + productID);
        }
    }
    
    //後台查詢單筆商品
    @Transactional
	public ProductEntity findProductById(Integer productID) {
		Optional<ProductEntity> optional = productRepo.findById(productID);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
    
    // 按照商品名稱進行模糊查詢的分頁
    public Page<ProductEntity> getProductsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findByProductNameContaining(name, pageable);
    }
    
   
    
   
}
