package com.eeit87t3.tickiteasy.post.entity;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "post")
@Component
public class PostEntity {
	
	@Id
	@Column(name = "postID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postID;
	
	@Column(name = "memberID")
	private Integer memberID;
	
	@Column(name = "postTitle")
	private String postTitle;
	
	@Column(name = "postContent")
	private String postContent;
	
//	@Column(name = "postImgUrl")
//	private String postImgUrl;
	
	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "postTime")
	private Timestamp postTime;
//	gpt建議的如下:
//	@Column(name = "createdAt", updatable = false)
//	@CreationTimestamp
//	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "editTime",nullable = true)
	private Timestamp editTime;
	
	@Column(name = "likesCount",nullable = true)
	private Integer likesCount;
	
	@Column(nullable = false) 
	private Integer viewCount = 0; // 設定預設值為 0
	
	@Column(name = "status")
	private Integer status;


	//類別 Category 
	@ManyToOne(fetch = FetchType.EAGER)
	@BatchSize(size = 10)
	@JoinColumn(name = "categoryID", referencedColumnName = "categoryID")//name沒有重複就不需要insertable = false, updatable = false
	private CategoryEntity postCategory; 
	
	//標籤 Tag
	
	@ManyToOne(fetch = FetchType.EAGER)
	@BatchSize(size = 10)
	@JoinColumn(name = "tagID", referencedColumnName = "tagID")
	private TagEntity postTag; 
	
	//留言 Comment
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "post")//用到留言的時候再加載
    private Set<CommentEntity> comments;  // 使用 Set 來避免重複
	
	//會員 Member 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@BatchSize(size = 10)
	@JoinColumn(name = "memberID", referencedColumnName = "memberID", insertable = false, updatable = false)
	private Member member;  
	

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImagesEntity> images = new ArrayList<>();
    
//	@Transient
//    private static final String DEFAULT_PROFILE_PIC = "/images/default-avatar.png"; // 預設頭貼路徑
//	
	public PostEntity() {
	
	}

	public PostEntity(Integer postID) {
		this.postID = postID;
	}

	public Integer getPostID() {return postID;}
	public Integer getMemberID() {return memberID;}
	public String getPostTitle() {return postTitle;}
	public String getPostContent() {return postContent;}
//	public String getPostImgUrl() {return postImgUrl;}
	public Timestamp getPostTime() {return postTime;}
	public Integer getLikesCount() {return likesCount;}
	public Integer getViewCount() {return viewCount;}
	public Integer getStatus() {return status;}


	public CategoryEntity getPostCategory() {return postCategory;}
	public void setPostCategory(CategoryEntity postCategory) {this.postCategory = postCategory;}


	public TagEntity getPostTag() {return postTag;}
	public void setPostTag(TagEntity postTag) {this.postTag = postTag;}
	

	
//	public String getTagName() {
//		if (postTag != null) {
//			return postTag.getTagName();
//		}
//		return null; // 或者返回一個預設值
//	}
//	
//	public void setTagName(String tagName) {
//		if (postTag != null) {
//			postTag.setTagName(tagName);
//		} else {
//			throw new IllegalStateException("postTag is not initialized.");
//		}
//	}
	
	public void setPostID(Integer postID) {this.postID = postID;}
	public void setMemberID(Integer memberID) {this.memberID = memberID;}
	public void setPostTitle(String postTitle) {this.postTitle = postTitle;}
	public void setPostContent(String postContent) {this.postContent = postContent;}
//	public void setPostImgUrl(String postImgUrl) {this.postImgUrl = postImgUrl;}
	public void setPostTime(Timestamp postTime) {this.postTime = postTime;}
	public void setLikesCount(Integer likesCount) {this.likesCount = likesCount;}
	public void setViewCount(Integer viewCount) {this.viewCount = viewCount;}
	public void setStatus(Integer status) {this.status = status;}
	


	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
    
	public Set<CommentEntity> getComments() {
	        return comments;
	}

	public void setComments(Set<CommentEntity> comments) {
	        this.comments = comments;
	}

	public Timestamp getEditTime() {
		return editTime;
	}

	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}

	public List<PostImagesEntity> getImages() {
		return images;
	}

	public void setImages(List<PostImagesEntity> images) {
		this.images = images;
	}
	  
	
	// 會員頭貼

//	public String getMemberProfilePic() {
//		if (member != null) {
//			return member.getProfilePic();
//		}
//		return null; // 或者返回一個預設值
//	}
//	
//	public void setMemberProfilePic(String profilePic) {
//		if (member != null) {
//				member.setProfilePic(profilePic);
//		} else {
//			// 處理 member 為 null 的情況
//			// 例如可以拋出異常或者設置一個默認值
//			throw new IllegalStateException("MemberBean is not initialized.");
//		}
//	}
//	public String getNickname() {
//		if (member != null) {
//			return member.getNickname();
//		}
//		return null; // 或者返回一個預設值
//	}
//	
//	public void setNickname(String nickname) {
//		if (member != null) {
//			member.setNickname(nickname);
//		} else {
//			// 處理 member 為 null 的情況
//			// 例如可以拋出異常或者設置一個默認值
//			throw new IllegalStateException("MemberBean is not initialized.");
//		}
//	}
	
}
