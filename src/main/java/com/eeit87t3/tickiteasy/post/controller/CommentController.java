package com.eeit87t3.tickiteasy.post.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;
import com.eeit87t3.tickiteasy.util.JWTUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin/api/post/comment")
public class CommentController {
	@Autowired
	JWTUtil jwtUtil;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CommentRepo commentRepo;

	// 新增單筆留言
	@PostMapping("POST/")
	public ResponseEntity<Map<String, Object>> addComment(
			@RequestHeader("Authorization") String authHeader,
			@RequestBody CommentEntity comment) {

	    Map<String, Object> response = new HashMap<>();
	    
	    if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
	        response.put("success", false);
	        response.put("message", "留言內容不得為空");
	        return ResponseEntity.badRequest().body(response);
	    }
		comment.setContent(comment.getContent());

		comment.setMemberID(comment.getMemberID()); // 實現這個方法來獲取當前登錄用戶的ID

		// CommentEntity savedComment = commentRepo.save(comment); // 保存實體到資料庫
		try {
			 // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");

	        // 從 Token 中獲取電子郵件
	        String email = jwtUtil.getEmailFromToken(token);

	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        if (member == null) {
	            response.put("success", false);
	            response.put("message", "Invalid user");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
			// 使用服務層插入留言
			 CommentEntity savedComment = commentService.insert(comment);
			 response.put("success", true);
		     response.put("message", "留言新增成功"); // 確保這裡返回了 message
		     response.put("data", savedComment); // 返回更新後的留言
			return ResponseEntity.ok(response); // 返回200和保存後的留言
		} catch (Exception e) {
		
			response.put("success", false);
	        response.put("message", "新增留言失敗，請檢查輸入或重試");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	
		}
	}

	// 更新單筆留言
	@PutMapping("PUT/{commentID}")
	public ResponseEntity<Map<String, Object>> updateComment(
	        @RequestHeader("Authorization") String authHeader,
	        @PathVariable("commentID") Integer commentID,
	        @RequestBody CommentEntity comment) {

	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 先取得原始留言
	        CommentEntity existingComment = commentRepo.findById(commentID)
	            .orElseThrow(() -> new RuntimeException("Comment not found"));

	        // 從 Authorization Header 中提取 Token
	        String token = authHeader.replace("Bearer ", "");

	        // 從 Token 中獲取電子郵件
	        String email = jwtUtil.getEmailFromToken(token);

	        // 根據電子郵件獲取會員資料
	        Member member = memberService.findByEmail(email);
	        if (member == null) {
	            response.put("success", false);
	            response.put("message", "Invalid user");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }

	        // 驗證當前會員是否為該貼文的發表者
	        if (!existingComment.getMemberID().equals(member.getMemberID())) {
	            response.put("success", false);
	            response.put("message", "您無權修改此留言");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	        }

	        // 更新留言內容
	        existingComment.setContent(comment.getContent()); // 假設留言內容的屬性為 content
	        existingComment.setEditTime(new Timestamp(System.currentTimeMillis())); // 設置為當前時間

	        CommentEntity updatedComment = commentService.update(commentID, existingComment);
	        response.put("success", true);
	        response.put("message", "留言更新成功");
	        response.put("data", updatedComment); // 返回更新後的留言
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "更新留言失敗，請檢查輸入或重試");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}



	// 刪除單筆留言
	@DeleteMapping("DELETE/{commentID}")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@PathVariable Integer commentID,
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
		            response.put("message", "您尚未登入或沒有權限刪除留言");
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
		try {
			
	        if (commentID == null || commentID <= 0) {
	            response.put("success", false);
	            response.put("message", "貼文ID無效");
	            return ResponseEntity.badRequest().body(response);
	        }

			Boolean isDeleted = commentService.delete(commentID);

			
			if (isDeleted) {
				response.put("success", true);
				response.put("message", "貼文已成功刪除");
				return ResponseEntity.ok(response);
			} else {
				response.put("success", false);
				response.put("message", "刪除失敗，請稍後再試");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} catch (Exception e) {
			// 處理 Token 無效或過期的情況
			  response.put("success", false);
		      response.put("message", "Invalid or expired token");
		      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			
		}
	}
}
