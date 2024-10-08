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
@RequestMapping("/admin/post")
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
	 @GetMapping
	    public String showPostsPage(Model model) {
	        return "post/postList"; 
	    }
	 //更新貼文頁面
	 @GetMapping("/{postID}/edit")
	 public String showUpdateForm() {
		 return "/post/editPost"; 
	 }
	 
	//新增貼文頁面 *未完成*
	@GetMapping("/create")
	    public String showInsertForm() {
	        return "/post/addPost";  
	    }
	//留言頁面
	 @GetMapping("/comments/{postID}")
	    public String showCommentsPage(@PathVariable Integer postID,Model model) {
		 	model.addAttribute("postID", postID);
	        return "post/postComment"; // 返回你的 Thymeleaf 模板，確保模板的路徑正確
	    }
	//////////////////////////////////////////////////////////////////////////////////////////

	

 	
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
