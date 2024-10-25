package com.eeit87t3.tickiteasy.post.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.dto.CreatePostDTO;
import com.eeit87t3.tickiteasy.post.dto.ShowCommentDTO;
import com.eeit87t3.tickiteasy.post.dto.ShowPostDTO;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.PostImagesEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.repository.PostImagesRepo;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;
import com.eeit87t3.tickiteasy.post.service.PostImageService;
import com.eeit87t3.tickiteasy.post.service.PostService;
import com.eeit87t3.tickiteasy.test.TestImagesEntity;

// 後臺路徑:/admin/api/post
// 前臺路徑:/user/post
//目前都是後台
@RestController
@RequestMapping("/admin/api/post")
public class PostJsonController {

	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;
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
	@Autowired
	private ImageUtil imageUtil;
	@Autowired
	private PostImagesRepo postImagesRepo;

	@Autowired
	private PostImageService postImageService;

	// 取得所有貼文
	// 改用DTO獲取必要資料
	@GetMapping("GET/")
	@ResponseBody
	public List<ShowPostDTO> getAllPosts() {
		return postService.getAllPosts();
	}


	@GetMapping("/posts/category/{categoryId}")
	public ResponseEntity<Page<ShowPostDTO>> getPostsByCategory(
			@PathVariable Integer categoryId,
			@RequestParam(required = false) Integer tagId,
			@RequestParam(required = false) String keyword, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "postID") String sortBy, 
		    @RequestParam(defaultValue = "desc") String orderBy) {

		  Page<ShowPostDTO> posts = postService.getPostsByCategory(categoryId, tagId, keyword, page, size, sortBy, orderBy);
		return ResponseEntity.ok(posts);
	}

	// 根據id取得單一貼文
	@GetMapping("GET/{postID}")
	public ResponseEntity<PostEntity> getPostById(@PathVariable("postID") Integer postID) {
		try {
			PostEntity post = postService.findById(postID);
			return ResponseEntity.ok(post);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 更新單筆貼文
	@PutMapping("PUT/{postID}")
	public ResponseEntity<PostEntity> updatePost(
			@PathVariable("postID") Integer postID,
			@RequestParam("postTitle") String postTitle,
	        @RequestParam("postContent") String postContent,
	        @RequestParam(value = "postCategory.categoryId", required = false) Integer categoryId,
	        @RequestParam(value = "postTag.tagId", required = false) Integer tagId,
	        @RequestParam(value = "images", required = false) MultipartFile[] imageFiles) {

		try {
			 // 先獲取現有的貼文
	        PostEntity existingPost = postRepo.findById(postID)
	                .orElseThrow(() -> new RuntimeException("Post not found"));
	        
	        // 更新需要修改的屬性
	        existingPost.setPostTitle(postTitle);
	        existingPost.setPostContent(postContent);

	        // 更新分類
	        if (categoryId != null) {
	            CategoryEntity category = categoryRepo.findById(categoryId)
	                    .orElseThrow(() -> new RuntimeException("Category not found"));
	            existingPost.setPostCategory(category);
	        }

	        // 更新標籤
	        if (tagId != null) {
	            TagEntity tag = tagRepo.findById(tagId)
	                    .orElseThrow(() -> new RuntimeException("Tag not found"));
	            existingPost.setPostTag(tag);
	        }

	        PostEntity result = postService.update(postID, existingPost, imageFiles);
	        return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(null); // 發生錯誤時回傳 HTTP 400 狀態
		}
	}

	// 根據類別取得多筆貼文 *未完成*
	@GetMapping("/category")
	public ResponseEntity<List<PostEntity>> getPostsByCategory(@RequestParam("categoryID") Integer categoryID) {
		// 根據 categoryID 查詢 CategoryEntity
		Optional<CategoryEntity> optionalCategory = categoryRepo.findById(categoryID);

		if (optionalCategory.isPresent()) {
			// 如果類別存在，則查詢相關貼文
			List<PostEntity> posts = postService.findByCategory(optionalCategory.get());
			return ResponseEntity.ok(posts); // 回傳 200 OK 和結果
		}

		// 如果類別不存在，回傳 404 Not Found
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	// 根據貼文id取得多筆留言
	@GetMapping("/comments")
	// public List<ShowCommentDTO> getCommentsByPostId(@RequestParam("postID")
	// Integer postID) {
	// return commentService.getCommentsByPostId(postID);
	// }
	public ResponseEntity<List<CommentEntity>> findCommentByPostId(@RequestParam("postID") Integer postID) {

		Optional<PostEntity> optionalPost = postRepo.findById(postID);

		if (optionalPost.isPresent()) {
			// 如果貼文存在，則查詢貼文的留言
			List<CommentEntity> comments = commentService.findByPostId(postID);
			return ResponseEntity.ok(comments); // 回傳 200 OK 和結果
		}

		// 如果類別不存在，回傳 404 Not Found
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	// 新增單筆貼文(附圖)//這不是json
	@PostMapping("POST/")
	public ResponseEntity<?> createPost(
			@ModelAttribute CreatePostDTO createPostDTO,
			@RequestParam(value = "images", required = false) MultipartFile[] imageFiles) {

		try {
			// 創建貼文物件
			PostEntity newPost = new PostEntity();
			newPost.setPostTitle(createPostDTO.getPostTitle());
			newPost.setPostContent(createPostDTO.getPostContent());

			// 設置分類和標籤 (使用商品方法
			if (createPostDTO.getCategoryID() != null) {
				CategoryEntity category = categoryService.findProductCategoryById(createPostDTO.getCategoryID());
				newPost.setPostCategory(category);
			}
			if (createPostDTO.getTagID() != null) {
				TagEntity tag = tagService.findProductTagById(createPostDTO.getTagID());
				newPost.setPostTag(tag);
			}

			// 設置默認值或從當前用戶會話中獲取
			// newPost.setMemberID(getCurrentMemberId()); // 實現這個方法來獲取當前登錄用戶的ID
			newPost.setMemberID(1); // 實現這個方法來獲取當前登錄用戶的ID
			newPost.setStatus(1); // 假設 1 是默認狀態

			// 保存到資料庫之前，先保存 newPost 以獲得 postID
			PostEntity createdPost = postService.insert(newPost);
			Integer postID = createdPost.getPostID();
			
			// 處理圖片上傳
			if (imageFiles != null && imageFiles.length > 0) {
				postImageService.uploadImages(imageFiles, postID);
			} else {

			}

			// 構建回應物件
		    Map<String, Object> response = new HashMap<>();
		    response.put("message", "貼文新增成功！");
		    response.put("postID", postID);

		    return ResponseEntity.ok(response);  // 返回 JSON 物件，包含 postID
		    
//			return ResponseEntity.ok("貼文新增成功！");
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("圖片上傳失敗", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 取得類別列表
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryEntity>> getAllCategories() {
		List<CategoryEntity> categories = categoryService.findPostCategoryList();
		return ResponseEntity.ok(categories);
	}

	// 取得標籤列表
	@GetMapping("/tags")
	public ResponseEntity<List<TagEntity>> getAllTags() {
		List<TagEntity> tags = tagService.findPostTagList();
		return ResponseEntity.ok(tags);
	}

	// 刪除單筆貼文
	@DeleteMapping("DELETE/{postID}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer postID) {
	    Map<String, Object> response = new HashMap<>();
	    
	    // 檢查 postID 是否有效
	    if (postID == null || postID <= 0) {
	        response.put("success", false);
	        response.put("message", "貼文ID無效");
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    // 根據 ID 找到貼文
	    PostEntity post = postService.findById(postID);
	    
	    // 如果找不到貼文，返回錯誤訊息
	    if (post == null) {
	        response.put("success", false);
	        response.put("message", "找不到貼文");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	    
	    // 如果貼文存在，取得 categoryId
	    Integer categoryId = post.getPostCategory().getCategoryId();
	    
	    // 執行刪除操作
	    Boolean isDeleted = postService.delete(postID);

	    // 根據刪除結果返回相應的回應
	    if (isDeleted) {
	        response.put("success", true);
	        response.put("message", "貼文已成功刪除");
	        response.put("categoryId", categoryId);  // 返回類別ID
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("success", false);
	        response.put("message", "刪除失敗，請稍後再試");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



}
