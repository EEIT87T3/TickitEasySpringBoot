package com.eeit87t3.tickiteasy.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class FavoritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favID;

    @Column(name = "memberID", nullable = false)
    private Long memberID;

    @Column(name = "postID", nullable = false)
    private Long postID;

    @Column(name = "favCreatedTime")
    private LocalDateTime favCreatedTime;

    @PrePersist
    protected void onCreate() {
        favCreatedTime = LocalDateTime.now();
    }

    // Getters and Setters

    // 時間
    public Long getFavID() {  // 修改為 getFavID
        return favID;
    }

    public Long getMemberID() {
        return memberID;
    }

    public Long getPostID() {
        return postID;
    }

    public void setFavID(Long favID) {  // 修改為 setFavID
        this.favID = favID;
    }

    public void setMemberID(Long memberID) {  // 統一大小寫
        this.memberID = memberID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public LocalDateTime getFavCreatedTime() {
        return favCreatedTime;
    }

    public void setFavCreatedTime(LocalDateTime favCreatedTime) {
        this.favCreatedTime = favCreatedTime;
    }
}
