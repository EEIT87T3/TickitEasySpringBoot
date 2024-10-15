package com.eeit87t3.tickiteasy.post.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.post.dto.CreatePostDTO;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;
import com.eeit87t3.tickiteasy.post.service.PostService;

// 後臺路徑:/admin/api/post
// 前臺路徑:/user/post
//目前都是後台
@RestController
@RequestMapping("/admin/api/post")
public class PostJsonController {
	
	@Autowired
	private PostService postService ;
	@Autowired
	private CommentService commentService ;
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private CommentRepo commentRepo;
	

	//取得所有貼文
	@GetMapping("GET/")
	@ResponseBody
	public ResponseEntity<List<PostEntity>> getAllPosts() {
	    List<PostEntity> posts = postService.findAll(); // 獲取所有貼文
	    return ResponseEntity.ok(posts);
	}

	//根據id取得單一貼文
	@GetMapping("GET/{postID}")
	public ResponseEntity<PostEntity> getPostById(@PathVariable("postID") Integer postID) {
	    try {
	        PostEntity post = postService.findById(postID);
	        return ResponseEntity.ok(post);
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}

	//更新單筆貼文
	@PutMapping("PUT/{postID}")
	public ResponseEntity<PostEntity> updatePost(
			@PathVariable("postID") Integer postID,
			@RequestBody PostEntity post) {
		
		try {
			PostEntity updatedPost = postService.update(postID, post);
			return ResponseEntity.ok(updatedPost); // 返回更新後的 JSON 數據
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null); // 發生錯誤時回傳 HTTP 400 狀態
		}
	}
 	//根據類別取得多筆貼文 *未完成*
 	@GetMapping("/category")
 	public ResponseEntity<List<PostEntity>> getPostsByCategory(@RequestParam("categoryID") Integer categoryID) {
 	    // 根據 categoryID 查詢 CategoryEntity
 	    Optional<CategoryEntity> optionalCategory = categoryRepo.findById(categoryID);
 	    
 	    if (optionalCategory.isPresent()) {
 	        // 如果類別存在，則查詢相關貼文
 	        List<PostEntity> posts = postService.findByCategory(optionalCategory.get());
 	        return ResponseEntity.ok(posts);  // 回傳 200 OK 和結果
 	    }
 	    
 	    // 如果類別不存在，回傳 404 Not Found
 	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
 	}
 	
 	//根據貼文id取得多筆留言
 	@GetMapping("/comments")
 	public ResponseEntity<List<CommentEntity>> findCommentByPostId(@RequestParam("postID") Integer postID) {
 
 		Optional<PostEntity> optionalPost = postRepo.findById(postID);
 		
 		if (optionalPost.isPresent()) {
 			// 如果貼文存在，則查詢貼文的留言
 			List<CommentEntity> comments = commentService.findByPostId(postID);
 			return ResponseEntity.ok(comments);  // 回傳 200 OK 和結果
 		}
 		
 		// 如果類別不存在，回傳 404 Not Found
 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
 	}
 	

 	//新增單筆貼文
 	@PostMapping("POST/")
 	public ResponseEntity<PostEntity> createPost(@RequestBody CreatePostDTO createPostDTO) {
 	    // 驗證必要字段
 	    if (createPostDTO.getPostTitle() == null || createPostDTO.getPostContent() == null) {
 	        return ResponseEntity.badRequest().body(null);
 	    }

 	    PostEntity post = new PostEntity();
 	    post.setPostTitle(createPostDTO.getPostTitle());
 	    post.setPostContent(createPostDTO.getPostContent());
 	    post.setPostImgUrl(createPostDTO.getPostImgUrl());
 	    
 	    // 設置分類和標籤
 	    if (createPostDTO.getCategoryID() != null) {
 	        CategoryEntity category = categoryService.findProductCategoryById(createPostDTO.getCategoryID());
 	        post.setPostCategory(category);
 	    }
 	    if (createPostDTO.getTagID() != null) {
 	        TagEntity tag = tagService.findProductTagById(createPostDTO.getTagID());
 	        post.setPostTag(tag);
 	    }

 	    // 設置默認值或從當前用戶會話中獲取
// 	    post.setMemberID(getCurrentMemberId()); // 實現這個方法來獲取當前登錄用戶的ID
 	    post.setMemberID(1); // 實現這個方法來獲取當前登錄用戶的ID
 	    post.setStatus(1); // 假設 1 是默認狀態

 	    PostEntity createdPost = postService.insert(post);
 	    return ResponseEntity.ok(createdPost);
 	}
 	
 
 	//取得類別列表
 	@GetMapping("/categories")
 	public ResponseEntity<List<CategoryEntity>> getAllCategories() {
 	    List<CategoryEntity> categories = categoryService.findPostCategoryList();
 	    return ResponseEntity.ok(categories);
 	}
 	//取得標籤列表
 	@GetMapping("/tags")
 	public ResponseEntity<List<TagEntity>> getAllTags() {
 	    List<TagEntity> tags = tagService.findPostTagList();
 	    return ResponseEntity.ok(tags);
 	}
 	
 	//刪除單筆貼文
 	@DeleteMapping("DELETE/{postID}")
 	public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer postID) {
 	    if (postID == null || postID <= 0) {
 	        Map<String, Object> response = new HashMap<>();
 	        response.put("success", false);
 	        response.put("message", "貼文ID無效");
 	        return ResponseEntity.badRequest().body(response);
 	    }

 	    Boolean isDeleted = postService.delete(postID);

 	    Map<String, Object> response = new HashMap<>();
 	    if (isDeleted) {
 	        response.put("success", true);
 	        response.put("message", "貼文已成功刪除");
 	        return ResponseEntity.ok(response);
 	    } else {
 	        response.put("success", false);
 	        response.put("message", "刪除失敗，請稍後再試");
 	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
 	    }
 	}


// 	@DeleteMapping("DELETE/{postID}")
// 	public String delete(@RequestParam("postID") Integer postID) {
// 		postService.delete(postID);
// 		
// 		return "post/postComment";	
// 	}
// 	

}
