package com.eeit87t3.tickiteasy.post.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageUtil;
import com.eeit87t3.tickiteasy.post.dto.ShowPostDTO;
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
//	@Transactional(readOnly = true)
//	public List<PostEntity> findAll(){
//		return postRepo.findAll();
//	}
	public List<ShowPostDTO> getAllPosts() {
        List<PostEntity> posts = postRepo.findAllPostsWithDetails();
        
        return posts.stream().map(post -> {
        	ShowPostDTO dto = new ShowPostDTO();
            dto.setPostID(post.getPostID()); // 假設ID是主鍵
            dto.setNickname(post.getMember().getNickname());
            dto.setPostTitle(post.getPostTitle());
            dto.setPostContent(post.getPostContent());
            dto.setCategoryName(post.getPostCategory() != null ? post.getPostCategory().getCategoryName() : "N/A");
            dto.setTagName(post.getPostTag() != null ? post.getPostTag().getTagName() : "N/A");
//            dto.setPostImgUrl(post.getPostImgUrl() != null ? post.getPostImgUrl() : "N/A");
            dto.setLikesCount(post.getLikesCount() );
            dto.setViewCount(post.getViewCount() );
            dto.setStatus(post.getStatus());
            dto.setPostTime(post.getPostTime() ); // 根據需要格式化時間
            dto.setEditTime(post.getEditTime() ); // 根據需要格式化時間
            
            return dto;
        }).collect(Collectors.toList());
    }
    public Page<ShowPostDTO> getPostsByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> postPage = postRepo.findByPostCategory_CategoryId(categoryId, pageable);

        return postPage.map(this::convertToShowPostDTO);
    }	
    private ShowPostDTO convertToShowPostDTO(PostEntity post) {
        ShowPostDTO dto = new ShowPostDTO();
        dto.setPostID(post.getPostID());
        dto.setNickname(post.getMember().getNickname());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostContent(post.getPostContent());
        dto.setCategoryName(post.getPostCategory().getCategoryName());
        dto.setCategoryId(post.getPostCategory().getCategoryId());
        dto.setTagName(post.getPostTag() != null ? post.getPostTag().getTagName() : null);
        dto.setTagId(post.getPostTag() != null ? post.getPostTag().getTagId() : null);
//        dto.setPostImgUrl(post.getPostImgUrl());
        dto.setStatus(post.getStatus());
        dto.setPostTime(post.getPostTime()); // 或者轉換為你需要的格式
        dto.setEditTime(post.getEditTime()); // 或者轉換為你需要的格式
        dto.setLikesCount(post.getLikesCount());
        dto.setViewCount(post.getViewCount());
        return dto;
    }

//	public Page<PostEntity> getPostsByPage(int page, int size) {
//	    Pageable pageable = PageRequest.of(page, size); // page 是第幾頁, size 是每頁顯示的數量
//	    return postRepo.findAll(pageable);
//	}
//	
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
