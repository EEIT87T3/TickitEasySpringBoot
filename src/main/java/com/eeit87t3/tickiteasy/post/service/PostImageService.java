package com.eeit87t3.tickiteasy.post.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.PostImagesEntity;
import com.eeit87t3.tickiteasy.post.repository.PostImagesRepo;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostImageService {
	  	@Autowired
	    private PostImagesRepo postImageRepo;

	    @Autowired
	    private ImageUtil imageUtil;
	    
	    public void uploadImages(MultipartFile[] imageFiles, Integer postID) throws IOException {
	        for (MultipartFile imageFile : imageFiles) {
	            String baseName = UUID.randomUUID().toString();
	            String pathString = imageUtil.saveImage(ImageDirectory.POST, imageFile, baseName);

	            PostImagesEntity postImagesPO = new PostImagesEntity();
	            postImagesPO.setImagePath(pathString);
	            postImagesPO.setPost(new PostEntity(postID)); // 設定外鍵關聯

	            postImageRepo.save(postImagesPO);
	        }
	    }
	    
	    public void deleteImageById(Integer imageID) throws IOException {
	        // 先從資料庫中查找圖片實體
	        PostImagesEntity postImage = postImageRepo.findById(imageID)
	                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found with ID: " + imageID));

	        // 獲取圖片路徑
	        String pathString = postImage.getImagePath();

	        // 使用 ImageUtil 刪除圖片
	        if (imageUtil.deleteImage(pathString)) {
	            // 如果刪除成功，則刪除資料庫中的記錄
	        	postImageRepo.deleteById(imageID);
	        } else {
	            throw new IOException("Failed to delete image from storage.");
	        }
	    }
}
