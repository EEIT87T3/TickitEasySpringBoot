package com.eeit87t3.tickiteasy.post.service;

import com.eeit87t3.tickiteasy.post.dto.ToggleLikeDTO;
import com.eeit87t3.tickiteasy.post.entity.LikesEntity;
import com.eeit87t3.tickiteasy.post.repository.LikesPostRepo;

import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.eeit87t3.tickiteasy.post.repository.LikesPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LikesPostService {

    @Autowired
    private LikesPostRepo likesPostRepo;
    @PersistenceContext
    private EntityManager entityManager;
    
    public LikesEntity toggleLike(Integer memberID, ToggleLikeDTO request) {
        Integer postID = request.getPostID();
        boolean isLiked = request.getIsLiked();
        
        System.out.println("memberID: " + memberID + ", postID: " + postID + ", isLiked: " + isLiked);
        if (isLiked) {
            // 新增喜歡
            LikesEntity newLike = new LikesEntity();
            newLike.setMemberID(memberID);
            newLike.setPostID(postID);
            // 這裡可以設置其他字段，例如 createdTime
            
            return likesPostRepo.save(newLike);
        } else {
            // 移除喜歡
            // 假設您有一個方法可以根據 memberID 和 postID 找到 LikesEntity
            LikesEntity existingLike = likesPostRepo.findByMemberIDAndPostID(memberID, postID);
            if (existingLike != null) {
            	likesPostRepo.delete(existingLike);
            }
            return null;  // 可以根據需要返回其他訊息
        }
    }
    public boolean isLiked(Integer memberID, Integer postID) {
        return likesPostRepo.existsByMemberIDAndPostID(memberID, postID);
    }
    // 新增喜歡
    public LikesEntity addLike(Integer memberID, Integer postID) {
        LikesEntity like = new LikesEntity();
        like.setMemberID(memberID);
        like.setPostID(postID);
        return likesPostRepo.save(like);
    }

    // 移除喜歡
    public void removeLike(Integer memberID, Integer postID) {
        LikesEntity like = likesPostRepo.findByMemberIDAndPostID(memberID, postID);
        if (like != null) {
            likesPostRepo.delete(like);
        }
    }
    // 新增移除該貼文的所有喜歡的方法
    public void removeAllLikesByPostId(Integer postID) {
        likesPostRepo.deleteByPostId(postID);
    }
    // 查詢是否按過喜歡
    public boolean getLikeByMemberAndPost(Integer memberID, Integer postID) {
        String sql = "SELECT COUNT(*) FROM postLikes WHERE memberID = :memberID AND postID = :postID"; 

        // 使用 EntityManager 來創建查詢
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("memberID", memberID);
        query.setParameter("postID", postID);
        
        // 獲取計數結果
        Long count = ((Number) query.getSingleResult()).longValue();

        // 如果計數大於0，表示會員已按過喜歡，返回 true，否則返回 false
        return count > 0;
    }

    // 查詢該會員的所有喜歡（需要標題和內容）
    public List<Map<String, Object>> getLikesByMember(Integer memberID) {
        return likesPostRepo.findLikesByMember(memberID);
    }

    // 查詢該貼文的所有喜歡（僅需要筆數）
    public Integer getLikesCountByPost(Integer postID) {
        return likesPostRepo.countByPostID(postID);
    }
}
