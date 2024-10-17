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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;
import com.eeit87t3.tickiteasy.post.service.CommentService;

@RestController
@RequestMapping("/admin/api/post/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService ;
	@Autowired
	private CommentRepo commentRepo;
	
	
	//新增單筆留言
	@PostMapping("POST/")
	public ResponseEntity<CommentEntity> addComment(@RequestBody CommentEntity comment) {
		 if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
		        return ResponseEntity.badRequest().body(null); // 返回400錯誤
		    }
	 	comment.setContent(comment.getContent());
	
	 	comment.setMemberID(1); // 實現這個方法來獲取當前登錄用戶的ID
	
//	    CommentEntity savedComment = commentRepo.save(comment); // 保存實體到資料庫
	 	try {
	        // 使用服務層插入留言
	        CommentEntity savedComment = commentService.insert(comment); 
	        return ResponseEntity.ok(savedComment); // 返回200和保存後的留言
	    } catch (Exception e) {
	        // 錯誤處理
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(null); // 返回500錯誤
	    }
	}
	//更新單筆貼文
		@PutMapping("PUT/{commentID}")
		public ResponseEntity<CommentEntity> updateComment(
				@PathVariable("commentID") Integer commentID,
				@RequestBody CommentEntity comment) {
			
			try {
				CommentEntity updatedComment = commentService.update(commentID, comment);
				return ResponseEntity.ok(updatedComment); // 返回更新後的 JSON 數據
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(null); // 發生錯誤時回傳 HTTP 400 狀態
			}
		}
	//刪除單筆留言
 	@DeleteMapping("DELETE/{commentID}")
 	public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer commentID) {
 	    if (commentID == null || commentID <= 0) {
 	        Map<String, Object> response = new HashMap<>();
 	        response.put("success", false);
 	        response.put("message", "貼文ID無效");
 	        return ResponseEntity.badRequest().body(response);
 	    }

 	    Boolean isDeleted = commentService.delete(commentID);

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
}
