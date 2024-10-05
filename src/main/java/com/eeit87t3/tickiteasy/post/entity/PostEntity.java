package com.eeit87t3.tickiteasy.post.entity;


import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
//import member.bean.MemberBean;

@Entity
@Table(name = "post")
@Component
public class PostEntity {
	
	@Id
	@Column(name = "postID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postID;
	
	@Column(name = "themeID")
	private Integer themeID;
	
	@Column(name = "memberID")
	private Integer memberID;
	
	@Column(name = "postTitle")
	private String postTitle;
	
	@Column(name = "postContent")
	private String postContent;
	
	@Column(name = "postImgUrl")
	private String postImgUrl;
	
	@CreationTimestamp
	@Column(name = "postTime")
	private Timestamp postTime;
	
	@Column(name = "likesCount")
	private Integer likesCount;
	
	@Column(name = "viewCount")
	private Integer viewCount;
	
	@Column(name = "status")
	private Integer status;
	
	//  @ManyToOne 關聯到 Theme 實體
	@ManyToOne
	@JoinColumn(name = "themeID", referencedColumnName = "themeID", insertable = false, updatable = false)//避免重複映射
    private ThemeEntity theme;  

	//  @ManyToOne 關聯到 Category 實體
	@ManyToOne
	@JoinColumn(name = "categoryID", referencedColumnName = "categoryID", insertable = false, updatable = false)//避免重複映射
	private CategoryEntity postCategory; 
	
	//  @ManyToOne 關聯到 Tag 實體
	@ManyToOne
	@JoinColumn(name = "tagID", referencedColumnName = "tagID", insertable = false, updatable = false)//避免重複映射
	private TagEntity postTag; 

    @OneToMany(mappedBy = "post")
    private Set<CommentEntity> comments;  // 使用 Set 來避免重複
	
	//  @ManyToOne 關聯到 Member 實體
//	@ManyToOne
//	@JoinColumn(name = "memberID", referencedColumnName = "memberID", insertable = false, updatable = false)//避免重複映射
//	private MemberBean member;  
	
	@Transient
    private static final String DEFAULT_PROFILE_PIC = "/images/default-avatar.png"; // 預設頭貼路徑
	
	public PostEntity() {
		super();
	}

	public Integer getPostID() {return postID;}
	public Integer getThemeID() {return themeID;}
	public Integer getMemberID() {return memberID;}
	public String getPostTitle() {return postTitle;}
	public String getPostContent() {return postContent;}
	public String getPostImgUrl() {return postImgUrl;}
	public Timestamp getPostTime() {return postTime;}
	public Integer getLikesCount() {return likesCount;}
	public Integer getViewCount() {return viewCount;}
	public Integer getStatus() {return status;}


	public CategoryEntity getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(CategoryEntity postCategory) {
		this.postCategory = postCategory;
	}



	public TagEntity getPostTag() {
		return postTag;
	}

	public void setPostTag(TagEntity postTag) {
		this.postTag = postTag;
	}

	public void setPostID(Integer postID) {this.postID = postID;}
	public void setThemeID(Integer themeID) {this.themeID = themeID;}
	public void setMemberID(Integer memberID) {this.memberID = memberID;}
	public void setPostTitle(String postTitle) {this.postTitle = postTitle;}
	public void setPostContent(String postContent) {this.postContent = postContent;}
	public void setPostImgUrl(String postImgUrl) {this.postImgUrl = postImgUrl;}
	public void setPostTime(Timestamp postTime) {this.postTime = postTime;}
	public void setLikesCount(Integer likesCount) {this.likesCount = likesCount;}
	public void setViewCount(Integer viewCount) {this.viewCount = viewCount;}
	public void setStatus(Integer status) {this.status = status;}
	
	public ThemeEntity getThemeName() {
		return theme;
	}
	public void setThemeName(ThemeEntity theme) {
		this.theme = theme;
	}
//	public MemberBean getMember() {
//		return member;
//	}
//	public void setMember(MemberBean member) {
//		this.member = member;
//	}
    
	public Set<CommentEntity> getComments() {
	        return comments;
	}
	public void setComments(Set<CommentEntity> comments) {
	        this.comments = comments;
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
//	public String getMemberNickname() {
//		if (member != null) {
//			return member.getNickname();
//		}
//		return null; // 或者返回一個預設值
//	}
//	
//	public void setMemberNickname(String nickname) {
//		if (member != null) {
//			member.setNickname(nickname);
//		} else {
//			// 處理 member 為 null 的情況
//			// 例如可以拋出異常或者設置一個默認值
//			throw new IllegalStateException("MemberBean is not initialized.");
//		}
//	}
	
}
