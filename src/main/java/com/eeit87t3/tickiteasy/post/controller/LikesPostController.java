package com.eeit87t3.tickiteasy.post.controller;

import com.eeit87t3.tickiteasy.post.dto.ToggleLikeDTO;
import com.eeit87t3.tickiteasy.post.entity.LikesEntity;
import com.eeit87t3.tickiteasy.post.service.LikesPostService;
import com.eeit87t3.tickiteasy.post.service.PostService;
import com.eeit87t3.tickiteasy.util.JWTUtil;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.eeit87t3.tickiteasy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/post/likes")
public class LikesPostController {
	@Autowired
	JWTUtil jwtUtil;
	
    @Autowired
    private LikesPostService likesPostService;

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private PostService postService;

    // 新增或移除喜歡
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ToggleLikeDTO request) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.getEmailFromToken(token);

            Member member = memberService.findByEmail(email);
            if (member == null) {
                response.put("success", false);
                response.put("message", "Invalid user");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Integer memberID = member.getMemberID();
            Integer postID = request.getPostID(); // 從請求中獲取 postID
            
            if (likesPostService.isLiked(memberID, postID)) {
                // 已經按讚，則取消按讚
                likesPostService.removeLike(memberID, postID);
                response.put("success", false);
                response.put("message", "已取消按讚！");
            } else {
                // 未按讚，則新增按讚
                LikesEntity like = likesPostService.addLike(memberID, postID); // 傳入 memberID 和 postID
                response.put("success", true);
                response.put("message", "已成功按讚！");
                response.put("likeID", like.getLikeID());
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "更新喜歡狀態失敗", "error", e.getMessage()));
        }
    }



    // 查詢該貼文是否按過喜歡
    @GetMapping("/checkLike")
    public ResponseEntity<Boolean> checkLike(
            @RequestParam("memberID") Integer memberID, 
            @RequestParam("postID") Integer postID) {

        boolean isLiked = likesPostService.getLikeByMemberAndPost(memberID, postID);

        return ResponseEntity.ok(isLiked);
    }
    
    // 3. 查詢該會員的所有喜歡（需要標題和內容）
    @GetMapping("/member/{memberID}")
    public ResponseEntity<List<Map<String, Object>>> getMemberLikes(
    		@PathVariable Integer memberID) {
        try {
            List<Map<String, Object>> likes = likesPostService.getLikesByMember(memberID);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 4. 查詢該貼文的所有喜歡（僅需要筆數）
    @GetMapping("/count/{postID}")
    public ResponseEntity<Map<String, Object>> getLikesCountByPost(
    		@PathVariable Integer postID) {
        try {
        	Integer  count = likesPostService.getLikesCountByPost(postID);
            return ResponseEntity.ok(Map.of("postID", postID, "likesCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // 更新 post 的 likesCount
    @PutMapping("/updateLikesCount/{postID}")
    public ResponseEntity<String> updateLikesCount(@PathVariable Integer postID, @RequestParam Integer likesCount) {
        postService.updateLikesCount(postID, likesCount);
        return ResponseEntity.ok("Likes count updated successfully.");
    }
}
