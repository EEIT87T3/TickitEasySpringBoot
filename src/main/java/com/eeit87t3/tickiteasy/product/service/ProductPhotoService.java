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
