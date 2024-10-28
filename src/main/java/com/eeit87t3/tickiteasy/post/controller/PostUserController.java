package com.eeit87t3.tickiteasy.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/post")
public class PostUserController {
		//測試頁面 
		@GetMapping("/test")
		public String testPage() {
			return "post/test";  
		}
		//分類貼文列表 
		@GetMapping("/PostList")
		public String userThemePostList() {
			return "post/userThemePostList";  
		}
		//分類首頁
		@GetMapping("/HomePage")
		public String userHomePage() {
			return "post/userPostHome";  
		}
		//單筆貼文
		@GetMapping("/{postID}")
		public String userPostPage() {
			return "post/userPost";  
		}
		//新增貼文
		@GetMapping("/usercreate")
		public String userAddPost() {
			return "post/userAddPost";  
		}
		//編輯貼文
		@GetMapping("/{postID}/useredit")
		public String userEditPost() {
			return "post/userEditPost";  
		}
}
