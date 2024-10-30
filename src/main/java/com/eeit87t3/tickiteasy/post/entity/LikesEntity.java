package com.eeit87t3.tickiteasy.post.entity;

import java.time.LocalDateTime;

import com.eeit87t3.tickiteasy.member.entity.Member;

import jakarta.persistence.*;

@Entity
@Table(name = "postLikes")
public class LikesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeID;

    @Column(name = "memberID", nullable = false)
    private Integer memberID;

    @Column(name = "postID", nullable = false)
    private Integer postID;

    @ManyToOne
    @JoinColumn(name = "memberID", referencedColumnName = "memberID", insertable = false, updatable = false)
    private Member member; // 這裡是關聯到 Member 實體

    @ManyToOne
    @JoinColumn(name = "postID", referencedColumnName = "postID", insertable = false, updatable = false)
    private PostEntity post; // 這裡是關聯到 PostEntity 實體

    @Column(name = "likeCreatedTime")
    private LocalDateTime likeCreatedTime;

    @PrePersist
    protected void onCreate() {
        likeCreatedTime = LocalDateTime.now(); // 自動設置當前時間
    }

    // Getters and Setters

    public Integer getLikeID() {
        return likeID;
    }

    public void setLikeID(Integer likeID) {
        this.likeID = likeID;
    }

    public Integer getMemberID() {
        return memberID;
    }

    public void setMemberID(Integer memberID) {
        this.memberID = memberID;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public LocalDateTime getLikeCreatedTime() {
        return likeCreatedTime;
    }

    public void setLikeCreatedTime(LocalDateTime likeCreatedTime) {
        this.likeCreatedTime = likeCreatedTime;
    }
}
