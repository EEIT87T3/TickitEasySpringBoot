package com.eeit87t3.tickiteasy.post.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
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
	private CategoryRepo categoryRepo;
	
	 @GetMapping("/posts")
	    public String showPostsPage(Model model) {
	        return "post/postList"; // 返回你的 Thymeleaf 模板，確保模板的路徑正確
	    }
	 
	//新增貼文
	@GetMapping("/create")
	    public String showInsertForm() {
	        return "post/insertpost";  // 返回 insertpost.jsp 
	    }
	//取得所有貼文
// 	@GetMapping
// 	public String findAll(Model model) {
// 		List<PostEntity> posts = postService.findAll();
// 		model.addAttribute("posts",posts);
// 		
// 		return "post/PostList";	
//		
//	}
	@GetMapping
	public ResponseEntity<List<PostEntity>> getAllPosts() {
	    List<PostEntity> posts = postService.findAll(); // 獲取所有貼文
	    return ResponseEntity.ok(posts);
	}
 	//根據貼文ID取得單筆貼文
 	@GetMapping("/{id}")
 	public String findById(
 			@ModelAttribute PostEntity post,
 			@RequestParam("postID")Integer postID,
 			Model model) {
 		
 		PostEntity findPost = postService.findById(postID);
 	
 		model.addAttribute("post",findPost);
	    
 		List<CommentEntity> findComments = commentService.findById(postID);
 		
 		model.addAttribute("comment", findComments);
 		return "post/post";	
 		
 	}
 	//根據貼文ID取得單筆貼文-留言
 	@GetMapping("/{id}")
 	public String findCommentByPostId(
 			@ModelAttribute PostEntity post,
 			@RequestParam("postID")Integer postID,
 			Model model) {
 		
// 		PostEntity findPost = postService.findById(postID);
 		
// 		model.addAttribute("post",findPost);
 		
 		List<CommentEntity> findComments = commentService.findById(postID);
 		
 		model.addAttribute("comment", findComments);
 		return "post/post";	
 		
 	}
 	@PostMapping("/getUpdatePost")
 	public String getUpdatePost(@RequestParam("postID")Integer postID,Model model) {
 		

		PostEntity foundPost = postService.findById(postID); 
		model.addAttribute("post", foundPost);
 		
 		return "post/UpdatePost";	
 		
 	}
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
 	//方法2
 	@GetMapping("/category")
 	public ResponseEntity<List<PostEntity>> getEventsByCategory(@RequestParam("categoryID") Integer categoryID) {
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
 	//新增單筆貼文
 	@PostMapping("/insertPost")
 	public String insert(
 			@ModelAttribute PostEntity post,
 			Model model) {
 		postService.insert(post);
 		
		PostEntity insertpost = postService.findById(post.getPostID());
 		//不能直接redirect:/post/findById 因為抓不到postID
 		
 		model.addAttribute("post",insertpost);
 		//將更改過後的文章addAttribute
 		//"post"對應request.getAttribute("post");
 		
 		return "post/post";	
 		
 	}
 	//更新單筆貼文
 	@PostMapping("/updatePost")
 	public String update(
 			@ModelAttribute PostEntity post,
 			@RequestParam("postID") Integer postID,
 			Model model) {
 		
 		postService.update(postID,post);
 		
 		PostEntity updatepost = postService.findById(postID);
 		//不能直接redirect:/post/findById 因為抓不到postID
 		
 		model.addAttribute("post",updatepost);
 		//將更改過後的文章addAttribute
 		//"post"對應request.getAttribute("post");
 		
 		return "post/post";	
 		//post資料夾的post.jsp
 		
 	}
 	//刪除單筆貼文
 	@PostMapping("/deletePost")
 	public String delete(@RequestParam("postID") Integer postID) {
 		postService.delete(postID);
 		
 		return "redirect:/post/findAll";	
 	}
 	
}
