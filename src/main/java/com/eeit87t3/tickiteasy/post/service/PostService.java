package com.eeit87t3.tickiteasy.post.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.dto.ShowPostDTO;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;
//import com.eeit87t3.tickiteasy.post.exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class PostService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private ImageUtil imageUtil;
	@Autowired
	private PostImageService postImageService;
	
	public PostEntity save(PostEntity post) {
        return postRepo.save(post);
    }
	//取得所有貼文
//	@Transactional(readOnly = true)
//	public List<PostEntity> findAll(){
//		return postRepo.findAll();
//	}
	public List<ShowPostDTO> getAllPosts() {
        List<PostEntity> posts = postRepo.findAllPostsWithDetails();
        
        return posts.stream().map(post -> {
        	ShowPostDTO dto = new ShowPostDTO();
            dto.setPostID(post.getPostID()); 
            dto.setNickname(post.getMember().getNickname());
            dto.setPostTitle(post.getPostTitle());
            dto.setPostContent(post.getPostContent());
            dto.setCategoryName(post.getPostCategory() != null ? post.getPostCategory().getCategoryName() : "N/A");
            dto.setTagName(post.getPostTag() != null ? post.getPostTag().getTagName() : "N/A");
            dto.setLikesCount(post.getLikesCount() );
            dto.setViewCount(post.getViewCount() );
            dto.setStatus(post.getStatus());
            dto.setPostTime(post.getPostTime() ); 
            dto.setEditTime(post.getEditTime() ); 
            
            return dto;
        }).collect(Collectors.toList());
    }
    public Page<ShowPostDTO> getPostsByCategory( Integer categoryId, Integer tagId, String keyword, int page, int size, String sortBy, String orderBy) {
    	Sort sort ;

        // 設定排序方向
        if (orderBy.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        
    	Pageable pageable = PageRequest.of(page, size, sort);
        Page<PostEntity> postPage = postRepo.findByPostCategory_CategoryId(categoryId, pageable);

        return postRepo.findByCategoryTagAndKeyword(categoryId, tagId, keyword, pageable)
                .map(this::convertToShowPostDTO);
    }	
    private ShowPostDTO convertToShowPostDTO(PostEntity post) {
        ShowPostDTO dto = new ShowPostDTO();
        dto.setPostID(post.getPostID());
        dto.setNickname(post.getMember().getNickname());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostContent(post.getPostContent());
        dto.setCategoryName(post.getPostCategory().getCategoryName());
        dto.setCategoryId(post.getPostCategory().getCategoryId());
        dto.setTagName(post.getPostTag() != null ? post.getPostTag().getTagName() : null);
        dto.setTagId(post.getPostTag() != null ? post.getPostTag().getTagId() : null);
//        dto.setPostImgUrl(post.getPostImgUrl());
        dto.setStatus(post.getStatus());
        dto.setPostTime(post.getPostTime()); // 或者轉換為你需要的格式
        dto.setEditTime(post.getEditTime()); // 或者轉換為你需要的格式
        dto.setLikesCount(post.getLikesCount());
        dto.setViewCount(post.getViewCount());
        return dto;
    }
    public List<PostEntity> findByMemberID(Integer memberID) {
        return postRepo.findByMemberID(memberID);
    }
//	public Page<PostEntity> getPostsByPage(int page, int size) {
//	    Pageable pageable = PageRequest.of(page, size); // page 是第幾頁, size 是每頁顯示的數量
//	    return postRepo.findAll(pageable);
//	}
//	
	//根據貼文ID取得單筆貼文
    @Transactional(readOnly = true)
    public PostEntity findById(Integer postId) {
    	Optional<PostEntity> optioanl = postRepo.findById(postId);
		
		if(optioanl.isPresent()) {
			return optioanl.get();
		}
		
		return null;
		
    }
    
    //根據Category取得多筆貼文
    @Transactional(readOnly = true)
    public  List<PostEntity> findByCategory(CategoryEntity postCategory) {
    	return postRepo.findByPostCategory(postCategory);
    }
//    //根據搜尋內容取得多筆貼文
//    @Transactional(readOnly = true)
//    public List<PostEntity> findByEnter(String enter){
//    	return postRepo.findByEnter(enter);
//    }
    
    //新增單筆貼文
    @Transactional
    public PostEntity insert(PostEntity post) {
    	return  postRepo.save(post);
    }
    //更新單筆貼文
    @Transactional
    public PostEntity update(
    		Integer postID, 
    		PostEntity updatedPost,
			@RequestParam(value = "images", required = false) MultipartFile[] imageFiles
			) {
    	try {
        // 首先查找該貼文是否存在
        PostEntity existingPost = postRepo.findById(postID)
                                          .orElseThrow(() -> new RuntimeException("Post not found"));

        // 更新貼文的屬性
        existingPost.setPostTitle(updatedPost.getPostTitle());
        existingPost.setPostContent(updatedPost.getPostContent());
        // 確保不修改 memberID
        Integer memberID = existingPost.getMemberID(); // 獲取當前的 memberID
        existingPost.setMemberID(memberID); // 保持原有的 memberID
        // 更新分類
        if (updatedPost.getPostCategory() != null) {
            CategoryEntity category = categoryRepo.findById(updatedPost.getPostCategory().getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            existingPost.setPostCategory(category);
        }

        // 更新標籤
        if (updatedPost.getPostTag() != null) {
            TagEntity tag = tagRepo.findById(updatedPost.getPostTag().getTagId())
                .orElseThrow(() -> new RuntimeException("Tag not found"));
            existingPost.setPostTag(tag);
        }
     // 處理圖片上傳
     	if (imageFiles != null && imageFiles.length > 0) {
     		postImageService.uploadImages(imageFiles, postID);
     	} else {

     	}
    
        // 儲存更新後的貼文
        return postRepo.save(existingPost); // 返回更新後的貼文實體
    	} catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload images", e); // 可以根據需要自定義異常處理
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e; // 重新拋出 RuntimeException，以便外層可以捕獲
        }
    }
    
    @Transactional
    public Boolean delete(Integer postID) {
        // 查詢貼文是否存在
        Optional<PostEntity> optional = postRepo.findById(postID);
        
        // 如果存在則刪除
        if (optional.isPresent()) {
            postRepo.deleteById(postID);
            return !postRepo.existsById(postID); // 返回是否成功刪除
        } 
        
        // 如果貼文不存在，返回 false
        return false;
    }
    
    // 更新 post 的 likesCount
    @Transactional
    public void updateLikesCount(Integer postId, Integer likesCount) {
        // 查詢該 post
        Optional<PostEntity> optionalPost = postRepo.findById(postId);
        if (optionalPost.isPresent()) {
            PostEntity post = optionalPost.get();
            post.setLikesCount(likesCount); 
            postRepo.save(post); 
        } else {
            throw new EntityNotFoundException("Post not found with ID: " + postId);
        }
    }

    
}
