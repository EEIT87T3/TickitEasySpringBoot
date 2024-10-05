package com.eeit87t3.tickiteasy.post.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.post.entity.CommentEntity;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.CommentRepo;


@Service
public class CommentService {
	
	@Autowired
	private CommentRepo commentRepo;

	
	//根據貼文ID取得多筆留言
	@Transactional(readOnly = true)
	public List<CommentEntity> findById(int postID) {
	    // 直接從 commentRepo 取得結果，若沒有資料則返回空的 List
	    return commentRepo.findByPostPostID(postID);
	}

	
	//根據貼文ID新增留言
    void insert(CommentEntity post) {
    	
    }
    //根據貼文ID更新留言
    void update(CommentEntity post) {
    	
    }
    void delete(int id) {
    	
    }

    
}
