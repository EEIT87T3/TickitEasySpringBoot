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
public class PostController {
	
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
	
///////////////////////////////////////     頁面跳轉    //////////////////////////////////////
	//所有貼文頁面
	 @GetMapping("/posts")
	    public String showPostsPage(Model model) {
	        return "post/postList"; 
	    }
	 //更新貼文頁面
	 @GetMapping("/GetUpdate")
	 public String showUpdateForm() {
		 return "post/editPost";  // 返回 insertpost.html
	 }
	 
	//新增貼文頁面 *未完成*
	@GetMapping("/create")
	    public String showInsertForm() {
	        return "post/insertpost";  
	    }
	//留言頁面
	 @GetMapping("/comments/{postID}")
	    public String showCommentsPage(@PathVariable Integer postID,Model model) {
		 	model.addAttribute("postID", postID);
	        return "post/postComment"; // 返回你的 Thymeleaf 模板，確保模板的路徑正確
	    }
	//////////////////////////////////////////////////////////////////////////////////////////

	//取得更新貼文內容
	@GetMapping("/update")
	public ResponseEntity<PostEntity> editPost(@RequestParam("postID") Integer postID) {
	    PostEntity post = postService.findById(postID);
	    if (post == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    return ResponseEntity.ok(post);
	}
	//取得所有貼文
	@GetMapping
	public ResponseEntity<List<PostEntity>> getAllPosts() {
	    List<PostEntity> posts = postService.findAll(); // 獲取所有貼文
	    return ResponseEntity.ok(posts);
	}

	//根據id取得單一貼文
	@GetMapping("/{postID}")
	public ResponseEntity<PostEntity> getPostById(@PathVariable("postID") Integer postID) {
	    try {
	        PostEntity post = postService.findById(postID);
	        return ResponseEntity.ok(post);
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
 	@PostMapping("/create")
 	public ResponseEntity<String> createPost(@RequestParam("postTitle") String title, 
 	                                         @RequestParam("postContent") String content,
 	                                         @RequestParam("categoryID") Integer categoryID,
 	                                         @RequestParam("tagID") Integer tagID) {
 	    PostEntity post = new PostEntity();
 	    post.setPostTitle(title);
 	    post.setPostContent(content);
 	    
 	    
 	   Optional<CategoryEntity> categoryOpt = categoryRepo.findById(categoryID);
 	   if (!categoryOpt.isPresent()) {
 		   throw new IllegalArgumentException("該分類不存在");
 	   }
 	   CategoryEntity category = categoryOpt.get();
 	   
 	   Optional<TagEntity> tagOpt = tagRepo.findById(tagID);
 	   if (!tagOpt.isPresent()) {
 		   throw new IllegalArgumentException("該標籤不存在");
 	   }
 	  TagEntity tag = tagOpt.get();
 	    
 	     
 	    post.setPostCategory(category);
 	    post.setPostTag(tag);
 	    
 	    postService.insert(post);
 	    
 	    return ResponseEntity.ok("Post created successfully");
 	}
 	
 	//更新單筆貼文
 	 @PutMapping("/{postID}")
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
 	//刪除單筆貼文
 	@DeleteMapping("/delete")
 	public String delete(@RequestParam("postID") Integer postID) {
 		postService.delete(postID);
 		
 		return "post/postComment";	
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
 	
	//更新貼文
//	@GetMapping("/update/{postID}")
//	    public String showUpdateForm(@PathVariable Integer postID,Model model) {
//		model.addAttribute("postID", postID);
//   
//		return "post/editPost";  
//	    }
// 	//根據貼文ID取得單筆貼文-留言
// 	@GetMapping("/comment")
// 	public String findCommentByPostId(
// 			@ModelAttribute PostEntity post,
// 			@RequestParam("postID")Integer postID,
// 			Model model) {
// 		
// 		
// 		List<CommentEntity> findComments = commentService.findById(postID);
// 		
// 		model.addAttribute("comment", findComments);
// 		return "post/post";	
// 		
// 	}

 	
// 	//根據搜尋內容取得多筆貼文
// 	@GetMapping("/findByEnter")
// 	public String findByEnter(
// 			@RequestParam("enter") String enter,
// 			Model model) {
// 		
// 		List<PostEntity> posts = postService.findByEnter(enter);
// 		model.addAttribute("posts",posts);
// 		
// 		return "post/PostList";	
// 		
// 	}
 	//根據貼文主題取得多筆貼文
// 	@GetMapping("/findByTheme")
// 	public String findByTheme(
// 			@RequestParam("category") CategoryEntity category,
// 			Model model) {
// 		
// 		List<PostEntity> posts = postService.findByCategory(category);
// 		model.addAttribute("posts",posts);
// 		if(category == null) {
// 			return "redirect:/post/findAll";
// 		}
// 		return "post/PostList";	
// 		
// 	}
 	//根據貼文ID取得單筆貼文
// 	@GetMapping("/{postID}")
// 	public String findById(
// 			@ModelAttribute PostEntity post,
// 			@RequestParam("postID")Integer postID,
// 			Model model) {
// 		
// 		PostEntity findPost = postService.findById(postID);
// 	
// 		model.addAttribute("post",findPost);
//	    
// 		List<CommentEntity> findComments = commentService.findById(postID);
// 		
// 		model.addAttribute("comment", findComments);
// 		return "post/post";	
// 		
// 	}
	//取得所有貼文
// 	@GetMapping
// 	public String findAll(Model model) {
// 		List<PostEntity> posts = postService.findAll();
// 		model.addAttribute("posts",posts);
// 		
// 		return "post/PostList";	
//		
//	}
}
