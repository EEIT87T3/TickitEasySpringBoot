package com.eeit87t3.tickiteasy.post.repository;

import com.eeit87t3.tickiteasy.post.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface LikesPostRepo extends JpaRepository<LikesEntity, Integer> {

    LikesEntity findByMemberIDAndPostID(Integer memberID, Integer postID);

    @Query("SELECT new map(p.postID as postID, p.postTitle as title, p.postContent as content, l.likeCreatedTime as Date) " +
    	       "FROM LikesEntity l JOIN PostEntity p ON l.postID = p.postID " +
    	       "WHERE l.memberID = :memberID")
    List<Map<String, Object>> findLikesByMember(Integer memberID);
    
    // 根據 postID 刪除所有相關的喜歡
    @Modifying
    @Transactional
    @Query("DELETE FROM LikesEntity l WHERE l.postID = :postID")
    void deleteByPostId(@Param("postID") Integer postID);
    
    boolean existsByMemberIDAndPostID(Integer memberID, Integer postID);
    Integer countByPostID(Integer postID);
}
