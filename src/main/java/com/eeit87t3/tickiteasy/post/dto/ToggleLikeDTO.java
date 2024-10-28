package com.eeit87t3.tickiteasy.post.dto;

public class ToggleLikeDTO {
    private Integer postID;  // 貼文ID
    private boolean isLiked;  // 是否喜歡的標記

    // Constructors
    public ToggleLikeDTO() {}

    public ToggleLikeDTO(Integer postID, boolean isLiked) {
        this.postID = postID;
        this.isLiked = isLiked;
    }

    // Getters and Setters
    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean liked) {
        isLiked = liked;
    }
    
}

