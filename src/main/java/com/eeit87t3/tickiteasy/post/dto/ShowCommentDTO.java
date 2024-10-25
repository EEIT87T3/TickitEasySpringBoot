package com.eeit87t3.tickiteasy.post.dto;

import java.sql.Timestamp;

public class ShowCommentDTO {
	private Integer postID;
    private Integer postCommentID;
    private Integer memberID;
    private String content;
    private Timestamp commentDate;
    // 構造函數
    public ShowCommentDTO(Integer postID, Integer postCommentID, Integer memberID, String content, Timestamp CommentDate) {
        this.postID = postID;
        this.postCommentID = postCommentID;
        this.memberID = memberID;
        this.content = content;
        this.commentDate = commentDate;
    }
    // getters 和 setters
    public Integer getPostID() { return postID; }
    public void setPostID(Integer postID) { this.postID = postID; }

    public Integer getPostCommentID() { return postCommentID; }
    public void setPostCommentID(Integer postCommentID) { this.postCommentID = postCommentID; }

    public Integer getMemberID() { return memberID; }
    public void setMemberID(Integer memberID) { this.memberID = memberID; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCommentDate() { return commentDate; }
    public void setCommentDate(Timestamp commentDate) { this.commentDate = commentDate; }
}
