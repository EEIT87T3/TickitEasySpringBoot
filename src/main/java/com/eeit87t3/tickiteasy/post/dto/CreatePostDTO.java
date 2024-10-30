package com.eeit87t3.tickiteasy.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePostDTO {
	private Integer memberID;
	 private String postTitle;
	 private String postContent;
	 private String postImgUrl;
//	 private Integer status;
	 private Integer categoryID;
	 private Integer tagID;
	public Integer getMemberID() {
		return memberID;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public String getPostContent() {
		return postContent;
	}
	public String getPostImgUrl() {
		return postImgUrl;
	}
//	public Integer getStatus() {
//		return status;
//	}
	public Integer getCategoryID() {
		return categoryID;
	}
	public Integer getTagID() {
		return tagID;
	}
	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public void setPostImgUrl(String postImgUrl) {
		this.postImgUrl = postImgUrl;
	}
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}
	public void setTagID(Integer tagID) {
		this.tagID = tagID;
	}

	 
}
