package com.eeit87t3.tickiteasy.post.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.entity.PostEntity;
import com.eeit87t3.tickiteasy.post.repository.PostRepo;


@Service
public class PostService {
	
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	@Autowired
	private ImageUtil imageUtil;
	
	//取得所有貼文
	@Transactional(readOnly = true)
	public List<PostEntity> findAll(){
		return postRepo.findAll();
	}
	
	//根據貼文ID取得單筆貼文
    @Transactional(readOnly = true)
    public PostEntity findById(Integer postId) {
    	Optional<PostEntity> optioanl = postRepo.findById(postId);
		
		if(optioanl.isPresent()) {
			return optioanl.get();
		}
		
		return null;
    }
    
    //根據Category取得多筆貼文
    @Transactional(readOnly = true)
    public  List<PostEntity> findByCategory(CategoryEntity postCategory) {
    	return postRepo.findByPostCategory(postCategory);
    }
//    //根據搜尋內容取得多筆貼文
//    @Transactional(readOnly = true)
//    public List<PostEntity> findByEnter(String enter){
//    	return postRepo.findByEnter(enter);
//    }
    
    //新增單筆貼文
    @Transactional
    public PostEntity insert(PostEntity post) {
    	return  postRepo.save(post);
    }
    //更新單筆貼文
    @Transactional
    public PostEntity update(Integer postID, PostEntity updatedPost) {

        // 首先查找該貼文是否存在
        PostEntity existingPost = postRepo.findById(postID)
                                          .orElseThrow(() -> new RuntimeException("Post not found"));

        // 更新貼文的屬性
        existingPost.setPostTitle(updatedPost.getPostTitle());
        existingPost.setPostContent(updatedPost.getPostContent());

        // 儲存更新後的貼文
        return postRepo.save(existingPost); // 返回更新後的貼文實體
    }
    
    @Transactional
    public Boolean delete(Integer postID) {
        // 查詢貼文是否存在
        Optional<PostEntity> optional = postRepo.findById(postID);
        
        // 如果存在則刪除
        if (optional.isPresent()) {
            postRepo.deleteById(postID);
            return !postRepo.existsById(postID); // 返回是否成功刪除
        } 
        
        // 如果貼文不存在，返回 false
        return false;
    }


    
}
