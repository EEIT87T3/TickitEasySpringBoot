package com.eeit87t3.tickiteasy.post.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.dto.CreatePostDTO;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.PostImagesEntity;
import com.eeit87t3.tickiteasy.post.repository.PostImagesRepo;
import com.eeit87t3.tickiteasy.post.service.PostImageService;

@RestController
@RequestMapping("/admin/api/post/images")
public class PostImagesController {

    @Autowired
    private PostImagesRepo postImagesRepo;
    @Autowired
    private PostImageService postImageService;

    @Autowired
    private ImageUtil imageUtil;

    // 新增圖片
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImages(
            @RequestParam(value = "images") MultipartFile[] imageFiles,
            @RequestParam Integer postID) {

        try {
        	  postImageService.uploadImages(imageFiles, postID);
        	  return ResponseEntity.ok("圖片上傳成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("圖片上傳失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 刪除圖片
    @DeleteMapping("/delete/{imageID}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer imageID) {
    	  try {
        	  postImageService.deleteImageById(imageID);
        	  return ResponseEntity.ok("圖片刪除成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("圖片刪除失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

