package com.eeit87t3.tickiteasy.post.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.repository.MemberRepository;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.post.dto.CreatePostDTO;
import com.eeit87t3.tickiteasy.post.dto.ReportDTO;
import com.eeit87t3.tickiteasy.post.dto.ShowCommentDTO;
import com.eeit87t3.tickiteasy.post.dto.ShowPostDTO;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.entity.PostImagesEntity;
import com.eeit87t3.tickiteasy.post.entity.ReportEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.repository.PostImagesRepo;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;
import com.eeit87t3.tickiteasy.post.service.LikesPostService;
import com.eeit87t3.tickiteasy.post.service.PostImageService;
import com.eeit87t3.tickiteasy.post.service.PostService;
import com.eeit87t3.tickiteasy.post.service.ReportService;
import com.eeit87t3.tickiteasy.test.TestImagesEntity;
import com.eeit87t3.tickiteasy.util.JWTUtil;

import jakarta.servlet.http.HttpSession;

// 後臺路徑:/admin/api/post
// 前臺路徑:/user/post
//目前都是後台
@RestController
@RequestMapping("/admin/api/post")
public class PostJsonController {

	@Autowired 
	JWTUtil jwtUtil;
	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private LikesPostService  likesPostService;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ImageUtil imageUtil;
	@Autowired
	private PostImagesRepo postImagesRepo;

	@Autowired
	private PostImageService postImageService;
//	private ResponseEntity<Map<String, Object>> validateAuthorization(String authHeader) {
//	    Map<String, Object> response = new HashMap<>();
//	    
//	    // 檢查授權標頭
//	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//	        response.put("success", false);
//	        response.put("message", "缺少授權標頭或格式錯誤");
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//	    }
//	    
//	    // 從 Authorization Header 中提取 Token
//	    String token = authHeader.replace("Bearer ", "");
//	    String email = jwtUtil.getEmailFromToken(token);
//	    
//	    // 根據電子郵件獲取會員資料
//	    Member member = memberService.findByEmail(email);
//	    
//	    if (member == null) {
//	        response.put("success", false);
//	        response.put("message", "無效的使用者");
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//	    }
//	    
//	    return null; // 表示授權成功
//	}

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
	// 根據MemberID取得單一貼文
	@GetMapping("GET/member/{MemberID}")
	public ResponseEntity<List<PostEntity>> getPostsByMemberID(@PathVariable("MemberID") Integer MemberID) {
		try {
			List<PostEntity> posts = postService.findByMemberID(MemberID);
			return ResponseEntity.ok(posts);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 更新單筆貼文
	@PutMapping("PUT/{postID}")
	public ResponseEntity<Map<String, Object>> updatePost(
	        @RequestHeader("Authorization") String authHeader,
	        @PathVariable("postID") Integer postID,
	        @RequestParam("postTitle") String postTitle,
	        @RequestParam("postContent") String postContent,
	        @RequestParam(value = "postCategory.categoryId", required = false) Integer categoryId,
	        @RequestParam(value = "postTag.tagId", required = false) Integer tagId,
	        @RequestParam(value = "images", required = false) MultipartFile[] imageFiles) {

	    Map<String, Object> response = new HashMap<>();
	    // 檢查授權標頭
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        response.put("success", false);
	        response.put("message", "缺少授權標頭或格式錯誤");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    try {
	        // 先獲取現有的貼文
	        PostEntity existingPost = postRepo.findById(postID)
	                .orElseThrow(() -> new RuntimeException("Post not found"));

	        // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");
	        String email = jwtUtil.getEmailFromToken(token);

	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        
	        if (member == null) {
	            response.put("success", false);
	            response.put("message", "無效的使用者");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }

	        // 驗證當前會員是否為該貼文的發表者
	        if (!existingPost.getMember().getMemberID().equals(member.getMemberID())) {
	            response.put("success", false);
	            response.put("message", "您無權修改此貼文");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }

	        // 更新需要修改的屬性
	        existingPost.setPostTitle(postTitle);
	        existingPost.setPostContent(postContent);
	        existingPost.setEditTime(new Timestamp(System.currentTimeMillis())); // 設置為當前時間
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

	        // 更新貼文並回傳結果
	        PostEntity result = postService.update(postID, existingPost, imageFiles);
	        
	        response.put("success", true);
	        response.put("message", "貼文已成功更新");
	        response.put("data", result); // 回傳更新後的貼文資料
	        return ResponseEntity.ok(response);

	    } catch (RuntimeException e) {
	        response.put("success", false);
	        response.put("message", "發生錯誤：" + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "未知錯誤：" + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
//	@GetMapping("/comments")
//	public ResponseEntity<List<CommentEntity>> findCommentByPostId(@RequestParam("postID") Integer postID) {
//
//		Optional<PostEntity> optionalPost = postRepo.findById(postID);
//
//		if (optionalPost.isPresent()) {
//			// 如果貼文存在，則查詢貼文的留言
//			List<CommentEntity> comments = commentService.findByPostId(postID);
//			return ResponseEntity.ok(comments); // 回傳 200 OK 和結果
//		}
//
//		// 如果類別不存在，回傳 404 Not Found
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//	}
	@GetMapping("/comments")
	public ResponseEntity<List<ShowCommentDTO>> findCommentByPostId(@RequestParam("postID") Integer postID) {
	    Optional<PostEntity> optionalPost = postRepo.findById(postID);

	    if (optionalPost.isPresent()) {
	        List<ShowCommentDTO> comments = commentService.findByPostIdwithMember(postID);
	        return ResponseEntity.ok(comments); // 回傳 200 OK 和結果
	    }

	    // 如果類別不存在，回傳 404 Not Found
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}


	// 新增單筆貼文(附圖)//這不是json
	@PostMapping("POST/")
	public ResponseEntity<Map<String, Object>> createPost(
	        @RequestHeader("Authorization") String authHeader,
	        @ModelAttribute CreatePostDTO createPostDTO,
	        @RequestParam(value = "images", required = false) MultipartFile[] imageFiles) {

	    Map<String, Object> response = new HashMap<>();
	    
	    // 檢查授權標頭
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        response.put("success", false);
	        response.put("message", "缺少授權標頭或格式錯誤");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    try {
	        // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");
	        
	        // 從 Token 中獲取電子郵件
	        String email = jwtUtil.getEmailFromToken(token);
	        
	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        
	        if (member == null) {
	            response.put("success", false);
	            response.put("message", "無效的使用者");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        
	        // 創建貼文物件
	        PostEntity newPost = new PostEntity();
	        newPost.setPostTitle(createPostDTO.getPostTitle());
	        newPost.setPostContent(createPostDTO.getPostContent());

	        // 設置分類和標籤
	        if (createPostDTO.getCategoryID() != null) {
	            CategoryEntity category = categoryService.findProductCategoryById(createPostDTO.getCategoryID());
	            newPost.setPostCategory(category);
	        }
	        if (createPostDTO.getTagID() != null) {
	            TagEntity tag = tagService.findProductTagById(createPostDTO.getTagID());
	            newPost.setPostTag(tag);
	        }

	        // 設置會員 ID 和狀態
	        newPost.setMemberID(member.getMemberID());
	        newPost.setStatus(1); // 假設 1 為默認狀態

	        // 保存貼文並取得 postID
	        PostEntity createdPost = postService.insert(newPost);
	        Integer postID = createdPost.getPostID();
	        
	        // 處理圖片上傳
	        if (imageFiles != null && imageFiles.length > 0) {
	            postImageService.uploadImages(imageFiles, postID);
	        }

	        // 回應成功訊息和 postID
	        response.put("success", true);
	        response.put("message", "貼文新增成功！");
	        response.put("postID", postID);
	        
	        return ResponseEntity.ok(response);
	        
	    } catch (IOException e) {
	        response.put("success", false);
	        response.put("message", "圖片上傳失敗");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "發生錯誤：" + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
	public ResponseEntity<Map<String, Object>> delete(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
		    @PathVariable Integer postID,
	        HttpSession session) {

	    Map<String, Object> response = new HashMap<>();
	    // 檢查 session 是否存在 admin
	    Admin admin = (Admin) session.getAttribute("admin");
	    
	    // 判斷是否為後台執行
	    if (admin != null) {
	        // 無需進行授權檢查，執行刪除操作
	    } else {
	        // 進行授權檢查
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            response.put("success", false);
	            response.put("message", "您尚未登入或沒有權限刪除貼文");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }

	        // 提取 Token
	        String token = authHeader.replace("Bearer ", "");
	        String email = jwtUtil.getEmailFromToken(token);
	        Member member = memberService.findByEmail(email);
	        
	        // 驗證會員是否存在
	        if (member == null) {
	            response.put("success", false);
	            response.put("message", "會員不存在");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    }
	    // 檢查貼文ID是否有效
	    if (postID == null || postID <= 0) {
	        response.put("success", false);
	        response.put("message", "貼文ID無效");
	        return ResponseEntity.badRequest().body(response);
	    }
	    
	    // 查找貼文
	    PostEntity post = postService.findById(postID);
	    if (post == null) {
	        response.put("success", false);
	        response.put("message", "找不到貼文");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	    
	    // 取得 categoryId
	    Integer categoryId = (post.getPostCategory() != null) ? post.getPostCategory().getCategoryId() : null;

	    // 刪除貼文和相關留言和喜歡
	    try {
	        List<CommentEntity> comments = commentService.findByPostId(postID);
	        commentService.deleteAll(comments);
	        
	        likesPostService.removeAllLikesByPostId(postID);
	        
//	        // 將所有相關檢舉的貼文設置為 null
//	        List<ReportEntity> reports = reportService.findByPost(post);
//	        for (ReportEntity report : reports) {
//	            report.setPost(null); // 移除與貼文的關聯
//	            reportService.save(report);
//	        }

	        Boolean isDeleted = postService.delete(postID);

	        if (isDeleted) {
	            response.put("success", true);
	            response.put("message", "貼文已成功刪除");
	            response.put("categoryId", categoryId);
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("success", false);
	            response.put("message", "刪除失敗，請稍後再試");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "刪除過程中發生錯誤");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	//讀取當前登入會員
	@GetMapping("/member")
	public ResponseEntity<Integer> getCurrentMember(
			@RequestHeader(value = "Authorization") String authHeader) {
		 // 檢查是否有提供授權標頭
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }

	    try {
	        // 取得 token 並解析 email
	        String token = authHeader.replace("Bearer ", "");
	        String email = jwtUtil.getEmailFromToken(token);

	        // 根據 email 查詢會員
	        Member member = memberService.findByEmail(email);
	        
	        if (member == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // 返回會員 ID
	        return ResponseEntity.ok(member.getMemberID());
	    } catch (Exception e) {
	        // 記錄錯誤，並返回500錯誤
	        e.printStackTrace(); // 這樣可以幫助除錯
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	// 更新 viewCount 
	@PutMapping("/view/{postID}")
	public ResponseEntity<Map<String, Object>> incrementViewCount(@PathVariable Integer postID) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // 查找 post 並更新 viewCount
	        PostEntity post = postService.findById(postID);
	        if (post == null) {
	            response.put("success", false);
	            response.put("message", "Post not found");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	      
	        post.setViewCount(post.getViewCount() + 1);
	        postService.save(post); // 保存更新後的 post
	        
	        response.put("success", true);
	        response.put("message", "View count updated");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Error updating view count");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}
	//檢舉
	@PostMapping("/report/{postID}")
	public ResponseEntity<Map<String, Object>> reportPost(
	        @PathVariable Integer postID,
	        @RequestHeader(value = "Authorization") String authHeader,
	        @RequestBody ReportDTO reportRequest){ 

	    Map<String, Object> response = new HashMap<>();
	    // 進行授權檢查
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("success", false);
            response.put("message", "您尚未登入或沒有權限刪除貼文");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 提取 Token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmailFromToken(token);
        Member member = memberService.findByEmail(email);
        
        // 驗證會員是否存在
        if (member == null) {
            response.put("success", false);
            response.put("message", "會員不存在");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
	    try {
	        // 獲取當前時間
	        Timestamp reportTime = new Timestamp(System.currentTimeMillis());

	        // 獲取文章快照
	        PostEntity post = postService.findById(postID);
	        if (post == null) {
	            response.put("success", false);
	            response.put("message", "找不到該文章");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	        // 設置被檢舉者 (發佈文章的會員)
	        Member accusedMember = post.getMember(); // 文章的會員即為被檢舉者

	        // 獲取檢舉者的會員資料
	        Member reportedMember = memberRepository.findById(reportRequest.getReportedMemberID()).orElse(null);
	        if (reportedMember == null) {
	            response.put("success", false);
	            response.put("message", "找不到檢舉者");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	        
	        // 建立檢舉紀錄
	        ReportEntity report = new ReportEntity();
	        
	        report.setReason(reportRequest.getReason());
	        report.setPost(post);
	        report.setReportedMember(reportedMember); // 設定檢舉者
	        report.setAccusedMember(accusedMember);   // 設定被檢舉者
	        report.setReportTime(reportTime);
	        report.setReportStatus(1);//預設1未處理
	        
	        // 設定文章快照的相關資料
	        report.setSavedTitle(post.getPostTitle()); 
	        report.setSavedContent(post.getPostContent()); 
	        
	        // 從 CategoryEntity 和 TagEntity 中獲取 ID
	        if (post.getPostCategory() != null) {
	            report.setSavedCategoryID(post.getPostCategory().getCategoryId()); // 假設有 getCategoryID()
	        }
	        
	        if (post.getPostTag() != null) {
	            report.setSavedTagID(post.getPostTag().getTagId()); // 假設有 getTagID()
	        }
	        report.setSavedEditTime(post.getEditTime()); 

	        // 儲存檢舉紀錄
	        reportService.save(report);

	        response.put("success", true);
	        response.put("message", "檢舉成功");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "檢舉失敗，請稍後重試");
	        response.put("error", e.getMessage());  // 添加詳細的錯誤訊息
	        e.printStackTrace();  // 輸出至控制台查看完整 stack trace
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }

	}
	@GetMapping("/reports")
	public ResponseEntity<List<ReportEntity>> getAllReports() {
	    List<ReportEntity> reports = reportService.findAllReports(); // 你的服務方法來查詢所有檢舉
	    return ResponseEntity.ok(reports);
	}
	
	//變更處理狀態
	@PutMapping("/report/{reportID}/status")
	public ResponseEntity<Map<String, Object>> updateReportStatus(
	        @PathVariable Integer reportID,
	        @RequestBody Map<String, Integer> requestBody) {

	    Map<String, Object> response = new HashMap<>();

	    Integer newStatus = requestBody.get("reportStatus");
	    if (newStatus == null || (newStatus != 1 && newStatus != 0)) {
	        response.put("success", false);
	        response.put("message", "狀態必須為 1 或 0");
	        return ResponseEntity.badRequest().body(response);
	    }

	    ReportEntity report = reportService.findById(reportID);
	    if (report == null) {
	        response.put("success", false);
	        response.put("message", "找不到該檢舉紀錄");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }

	    report.setReportStatus(newStatus);
	    reportService.save(report);

	    response.put("success", true);
	    response.put("message", "檢舉狀態已更新");
	    return ResponseEntity.ok(response);
	}





}
