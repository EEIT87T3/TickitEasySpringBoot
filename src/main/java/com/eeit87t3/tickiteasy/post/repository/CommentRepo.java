package com.eeit87t3.tickiteasy.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eeit87t3.tickiteasy.post.dto.ShowCommentDTO;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;

public interface CommentRepo extends JpaRepository<CommentEntity,  Integer> {
//	@Query("SELECT new  com.eeit87t3.tickiteasy.post.dto(c.postID, c.postCommentID, c.memberID, c.content, c.commentDate) " +
//	           "FROM CommentEntity c WHERE c.post.postID = :postID")
//	List<ShowCommentDTO> getCommentsByPostId(@Param("postID") Integer postID);
	
	List<CommentEntity> findByPostPostID(int postID);
	
	@Query("SELECT new com.eeit87t3.tickiteasy.post.dto.ShowCommentDTO(c.postID, c.postCommentID, c.memberID, c.content, c.commentDate, c.editTime, m.nickname, m.profilePic) " +
		       "FROM CommentEntity c JOIN Member m ON c.memberID = m.memberID WHERE c.postID = :postID")
		List<ShowCommentDTO> findCommentsWithMemberInfoByPostID(@Param("postID") Integer postID);

}
