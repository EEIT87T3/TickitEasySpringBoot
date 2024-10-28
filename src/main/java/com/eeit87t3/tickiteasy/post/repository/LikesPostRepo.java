package com.eeit87t3.tickiteasy.post.repository;

import com.eeit87t3.tickiteasy.post.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LikesPostRepo extends JpaRepository<LikesEntity, Integer> {

    LikesEntity findByMemberIDAndPostID(Integer memberID, Integer postID);

    @Query("SELECT new map(p.postTitle as title, p.postContent as content) FROM LikesEntity l JOIN PostEntity p ON l.postID = p.postID WHERE l.memberID = :memberID")
    List<Map<String, Object>> findLikesByMember(Integer memberID);

    Integer countByPostID(Integer postID);
}
