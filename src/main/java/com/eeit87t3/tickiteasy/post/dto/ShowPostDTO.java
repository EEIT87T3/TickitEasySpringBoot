package com.eeit87t3.tickiteasy.post.dto;

import java.sql.Timestamp;
import java.util.List;

public class ShowPostDTO {
	//getallpost用
	private Integer postID; // 對應的 postID
    private String nickname; // 會員的暱稱
    private String postTitle; // 文章標題
    private String postContent; // 文章內容
    private String categoryName; // 類別名稱
    private Integer categoryId; // 類別Id
    private String tagName; // 標籤名稱
    private Integer tagId; // 標籤ID
//    private List<String> imagePaths; // 僅包含圖片路徑的列表

//    private String postImgUrl; // 文章圖片URL
    private Integer status; // 文章狀態
    private Timestamp postTime; // 新增文章時間
    private Timestamp editTime; // 編輯文章時間
    private Integer likesCount;
    private Integer viewCount;
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
	public Integer getCategoryId() {
		return categoryId;
	}
	public String getTagName() {
		return tagName;
	}
	public Integer getTagId() {
		return tagId;
	}
//	public String getPostImgUrl() {
//		return postImgUrl;
//	}
	public Integer getStatus() {
		return status;
	}
	public Timestamp getPostTime() {
		return postTime;
	}
	public Timestamp getEditTime() {
		return editTime;
	}
	public Integer getLikesCount() {
		return likesCount;
	}
	public Integer getViewCount() {
		return viewCount;
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
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
//	public void setPostImgUrl(String postImgUrl) {
//		this.postImgUrl = postImgUrl;
//	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}
	public void setEditTime(Timestamp editTime) {
		this.editTime = editTime;
	}
	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
//	public List<String> getImagePaths() {
//		return imagePaths;
//	}
//	public void setImagePaths(List<String> imagePaths) {
//		this.imagePaths = imagePaths;
//	}
    

}
