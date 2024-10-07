package com.eeit87t3.tickiteasy.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
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
    
    public List<CategoryEntity> getProductCategories() {
        return categoryService.findProductCategoryList();
    }

    public List<TagEntity> getProductTags() {
        return tagService.findProductTagList();
    }

    
    @Transactional
    public ProductEntity createProduct(ProductEntity product, MultipartFile imageFile, Integer categoryId, Integer tagId) throws IOException {
        // 設置分類和標籤
        CategoryEntity category = categoryService.findProductCategoryById(categoryId);
        TagEntity tag = tagService.findProductTagById(tagId);
        product.setProductCategory(category);
        product.setProductTag(tag);
        
        ProductEntity savedProduct = productRepo.save(product);
        
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, "product_" + savedProduct.getProductID());
            savedProduct.setProductPic(imagePath);
            savedProduct = productRepo.save(savedProduct);
        }
        
        return savedProduct;
    }
    
    @Transactional
    public ProductEntity editProductById(Integer productID, ProductEntity newProduct, MultipartFile imageFile, Integer categoryId, Integer tagId) throws IOException {
        Optional<ProductEntity> optional = productRepo.findById(productID);

        if(optional.isPresent()) {
            ProductEntity product = optional.get();

            // 更新圖片
            if (imageFile != null && !imageFile.isEmpty()) {
                if (product.getProductPic() != null) {
                    imageUtil.deleteImage(product.getProductPic());
                }
                String imagePath = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, "product_" + productID);
                product.setProductPic(imagePath);
            } else if (newProduct.getProductPic() != null) {
                product.setProductPic(newProduct.getProductPic());
            }

            // 更新分類和標籤
            CategoryEntity category = categoryService.findProductCategoryById(categoryId);
            TagEntity tag = tagService.findProductTagById(tagId);
            product.setProductCategory(category);
            product.setProductTag(tag);

            // 更新其他屬性
            product.setProductName(newProduct.getProductName());
            product.setProductDesc(newProduct.getProductDesc());
            product.setPrice(newProduct.getPrice());
            product.setStock(newProduct.getStock());
            product.setStatus(newProduct.getStatus());
            product.setProdTotalReviews(newProduct.getProdTotalReviews());
            product.setProdTotalScore(newProduct.getProdTotalScore());

            return productRepo.save(product);
        }

        return null;
    }
    
    @Transactional
    public void deleteProductById(Integer productID) throws IOException {
        Optional<ProductEntity> optional = productRepo.findById(productID);
        if (optional.isPresent()) {
            ProductEntity product = optional.get();
            if (product.getProductPic() != null) {
                imageUtil.deleteImage(product.getProductPic());
            }
            productRepo.deleteById(productID);
        }
    }
    
    @Transactional
    public List<ProductEntity> findProductByName(String productName) {
        return productRepo.findByProductNameContaining(productName);
    }
    
    @Transactional
	public ProductEntity findProductById(Integer productID) {
		Optional<ProductEntity> optional = productRepo.findById(productID);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	public List<ProductEntity> findAllProducts(){
		return productRepo.findAll();
	}
	
	public Page<ProductEntity> findAllProductsPageSortedByIdAndName(String productName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return productRepo.findAllOrderByProductIdAndName(productName, pageable);
    }
	
//	public Page<ProductEntity> findAllProductsPageSortedByTag(int pageNumber, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//        return productRepo.findAllOrderByProductTag(pageable);
//    }
	
//	public Page<ProductEntity> findAllProductsPageSortedByTagAndName(String productName, int pageNumber, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//        return productRepo.findAllOrderByProductTagAndName(productName, pageable);
//    }
	
}
