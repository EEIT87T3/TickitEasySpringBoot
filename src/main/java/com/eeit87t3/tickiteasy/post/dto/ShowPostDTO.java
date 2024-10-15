package com.eeit87t3.tickiteasy.post.dto;

public class ShowPostDTO {
	//getallpost用
	private Integer postID; // 對應的 postID
    private String nickname; // 會員的暱稱
    private String postTitle; // 文章標題
    private String postContent; // 文章內容
    private String categoryName; // 類別名稱
    private String tagName; // 標籤名稱
    private String postImgUrl; // 文章圖片URL
    private Integer status; // 文章狀態
    private String postTime; // 文章時間
    
	public Integer getPostID() {
		return postID;
	}
	public String getNickname() {
		return nickname;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public String getPostContent() {
		return postContent;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public String getTagName() {
		return tagName;
	}
	public String getPostImgUrl() {
		return postImgUrl;
	}
	public Integer getStatus() {
		return status;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostID(Integer postID) {
		this.postID = postID;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public void setPostImgUrl(String postImgUrl) {
		this.postImgUrl = postImgUrl;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
    
 
}
