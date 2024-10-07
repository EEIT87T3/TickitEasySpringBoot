package com.eeit87t3.tickiteasy.post.controller;

import java.util.List;
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

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;
import com.eeit87t3.tickiteasy.post.service.PostService;

// 後臺路徑:/admin/api/post
// 前臺路徑:/user/post
//目前都是後台
@Controller
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
 	@GetMapping("/comment")
 	public ResponseEntity<List<CommentEntity>> findCommentByPostId(@RequestParam("postID") Integer postID) {
 
 		Optional<PostEntity> optionalPost = postRepo.findById(postID);
 		
 		if (optionalPost.isPresent()) {
 			// 如果貼文存在，則查詢貼文的留言
 			List<CommentEntity> comments = commentService.findById(postID);
 			return ResponseEntity.ok(comments);  // 回傳 200 OK 和結果
 		}
 		
 		// 如果類別不存在，回傳 404 Not Found
 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
 	}
 	

 	//新增單筆貼文
 	@PostMapping("POST/")
 	public ResponseEntity<String> createPost(@RequestParam("postTitle") String title, 
 	                                         @RequestParam("postContent") String content,
 	                                         @RequestParam("categoryID") Integer categoryID,
 	                                         @RequestParam("memberID") Integer memberID,
 	                                         @RequestParam("status") Integer status,
 	                                         @RequestParam("tagID") Integer tagID) {
 		System.out.println(categoryID);
 		System.out.println(tagID);
 	    PostEntity post = new PostEntity();
 	    post.setPostTitle(title);
 	    post.setPostContent(content);
 	    post.setMemberID(memberID);
 	    post.setStatus(status);
 	    
 	    TagEntity tag= tagService.findProductTagById(tagID);
 	    CategoryEntity category= categoryService.findProductCategoryById(categoryID);
 	    post.setPostCategory(category);
 	    post.setPostTag(tag);
 	   System.out.println(category!=null);
		System.out.println(tag!=null);
// 	   Optional<CategoryEntity> categoryOpt = categoryRepo.findById(categoryID);
// 	   if (!categoryOpt.isPresent()) {
// 		   throw new IllegalArgumentException("該分類不存在");
// 	   }
// 	   CategoryEntity category = categoryOpt.get();
// 	   
// 	   Optional<TagEntity> tagOpt = tagRepo.findById(tagID);
// 	   if (!tagOpt.isPresent()) {
// 		   throw new IllegalArgumentException("該標籤不存在");
// 	   }
// 	  TagEntity tag = tagOpt.get();
 	    
 	     
// 	    post.setPostCategory(category);
// 	    post.setPostTag(tag);
 	    
 	    postService.insert(post);
 	    
 	    return ResponseEntity.ok("Post created successfully");
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
 	public ResponseEntity<?> delete(@PathVariable Integer postID) {
 	    // 檢查 postID 是否為 null 或小於等於零
 	    if (postID == null || postID <= 0) {
 	        return ResponseEntity.badRequest().body("貼文ID無效");
 	    }

 	    // 調用服務層的刪除方法
 	    Boolean isDeleted = postService.delete(postID);

 	    // 根據刪除結果返回響應
 	    if (isDeleted) {
 	        return ResponseEntity.ok("貼文已成功刪除");
 	    } else {
 	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
 	                             .body("刪除失敗，請稍後再試");
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
