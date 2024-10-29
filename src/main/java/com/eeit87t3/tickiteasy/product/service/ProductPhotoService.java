package com.eeit87t3.tickiteasy.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.product.entity.ProductEntity;
import com.eeit87t3.tickiteasy.product.entity.ProductPhotoEntity;
import com.eeit87t3.tickiteasy.product.repository.ProductPhotoRepo;

@Service
public class ProductPhotoService {

	@Autowired
	private ProductPhotoRepo productPhotoRepo;
	
	@Autowired
    private ImageUtil imageUtil;
	
	// 新增圖片
    public ProductPhotoEntity createProductPhoto(MultipartFile imageFile, ProductEntity product) throws IOException {
        String baseName = UUID.randomUUID().toString();
        String fileName = imageUtil.saveImage(ImageDirectory.PRODUCT, imageFile, baseName);
        
        ProductPhotoEntity productPhoto = new ProductPhotoEntity();
        productPhoto.setFileName(fileName);
        productPhoto.setProduct(product);
        
        return productPhotoRepo.save(productPhoto);
    }

    // 修改圖片
    public ProductPhotoEntity editProductPhoto(Integer photoID, MultipartFile newImageFile) throws IOException {
        Optional<ProductPhotoEntity> optionalPhoto = productPhotoRepo.findById(photoID);
        if (optionalPhoto.isPresent()) {
            ProductPhotoEntity productPhoto = optionalPhoto.get();
            
            // 刪除舊圖片
            imageUtil.deleteImage(productPhoto.getFileName());
            
            // 儲存新圖片
            String baseName = UUID.randomUUID().toString();
            String newFileName = imageUtil.saveImage(ImageDirectory.PRODUCT, newImageFile, baseName);
            
            productPhoto.setFileName(newFileName);
            return productPhotoRepo.save(productPhoto);
        }
        return null; // 或者拋出異常
    }
	
    
    // 刪除圖片
    public void deleteProductPhoto(Integer photoID) throws IOException {
        Optional<ProductPhotoEntity> optionalPhoto = productPhotoRepo.findById(photoID);
        if (optionalPhoto.isPresent()) {
            ProductPhotoEntity productPhoto = optionalPhoto.get();
            
            // 從檔案系統中刪除圖片
            imageUtil.deleteImage(productPhoto.getFileName());
            
            // 從資料庫中刪除記錄
            productPhotoRepo.delete(productPhoto);
        }
    }
    
    // 查詢圖片
    public List<ProductPhotoEntity> getProductPhotos(ProductEntity product) {
        return productPhotoRepo.findByProduct(product);
    }
	
}
