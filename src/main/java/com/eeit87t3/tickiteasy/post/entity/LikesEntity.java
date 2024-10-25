package com.eeit87t3.tickiteasy.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "likes")
public class LikesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeID;

    @Column(name = "memberID", nullable = false)
    private Long memberID;

    @Column(name = "postID", nullable = false)
    private Long postID;

    @Column(name = "likeCreatedTime")
    private LocalDateTime likeCreatedTime;  // 欄位名稱改為 likeCreatedTime

    @PrePersist
    protected void onCreate() {
        likeCreatedTime = LocalDateTime.now();  // 自動設置當前時間
    }

    // Getters and Setters

    public Long getLikeID() {
		return likeID;
	}

	public Long getMemberID() {
		return memberID;
	}

	public Long getPostID() {
		return postID;
	}

	public void setLikeID(Long likeID) {
		this.likeID = likeID;
	}

	public void setMemberID(Long memberID) {
		this.memberID = memberID;
	}

	public void setPostID(Long postID) {
		this.postID = postID;
	}
	
	//時間
	public LocalDateTime getLikeCreatedTime() {
	    return likeCreatedTime;
	}
	public void setLikeCreatedTime(LocalDateTime likeCreatedTime) {
        this.likeCreatedTime = likeCreatedTime;
    }
}
