package com.eeit87t3.tickiteasy.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "follows")
public class FollowsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followID;

    @Column(name = "memberID", nullable = false)
    private Long memberID;

    @Column(name = "followedMemberID", nullable = false)
    private Long followedMemberID;  // 被關注的會員ID

    @Column(name = "followCreatedTime")
    private LocalDateTime followCreatedTime;

    @PrePersist
    protected void onCreate() {
        followCreatedTime = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getFollowID() {  // 修改為 getFollowID
        return followID;
    }

    public Long getMemberID() {
        return memberID;
    }

    public Long getFollowedMemberID() {  // 修改為 getFollowedMemberID
        return followedMemberID;
    }

    public void setFollowID(Long followID) {  // 修改為 setFollowID
        this.followID = followID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    public void setFollowedMemberID(Long followedMemberID) {  // 修改為 setFollowedMemberID
        this.followedMemberID = followedMemberID;
    }

    public LocalDateTime getFollowCreatedTime() {
        return followCreatedTime;
    }

    public void setFollowCreatedTime(LocalDateTime followCreatedTime) {
        this.followCreatedTime = followCreatedTime;
    }
}
