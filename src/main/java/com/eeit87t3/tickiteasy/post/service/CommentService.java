package com.eeit87t3.tickiteasy.post.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.post.dto.ShowCommentDTO;
import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;


@Service
public class CommentService {
	
	@Autowired
	private CommentRepo commentRepo;

	//根據留言ID取得單筆留言
    @Transactional(readOnly = true)
    public CommentEntity findById(Integer commentID) {
    	Optional<CommentEntity> optioanl = commentRepo.findById(commentID);
		
		if(optioanl.isPresent()) {
			return optioanl.get();
		}
		
		return null;
    }
	//根據貼文ID取得多筆留言
	@Transactional(readOnly = true)
	public List<CommentEntity> findByPostId(int postID) {
	    // 直接從 commentRepo 取得結果，若沒有資料則返回空的 List
	    return commentRepo.findByPostPostID(postID);
	}
//	public List<ShowCommentDTO> getCommentsByPostId(Integer postID) {
//	  
//	    return commentRepo.getCommentsByPostId(postID);
//	}


	//根據貼文ID新增留言
	@Transactional
	public CommentEntity insert(CommentEntity comment) {
		return  commentRepo.save(comment);
    }
    //根據留言ID更新留言
	public CommentEntity update(Integer commentID,CommentEntity updatedComment) {

        // 首先查找該貼文是否存在
    	CommentEntity existingComment = commentRepo.findById(commentID)
                                          .orElseThrow(() -> new RuntimeException("Comment not found"));

        // 更新貼文的屬性
        existingComment.setContent(updatedComment.getContent());

        // 儲存更新後的貼文
        return commentRepo.save(existingComment); // 返回更新後的貼文實體
    }
	
	@Transactional
    public Boolean delete(Integer commentID) {
        // 查詢貼文是否存在
        Optional<CommentEntity> optional = commentRepo.findById(commentID);
        
        // 如果存在則刪除
        if (optional.isPresent()) {
        	commentRepo.deleteById(commentID);
            return !commentRepo.existsById(commentID); // 返回是否成功刪除
        } 
        
        // 如果貼文不存在，返回 false
        return false;
    }

    
}
