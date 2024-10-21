package com.eeit87t3.tickiteasy.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "postImages")
public class PostImagesEntity {
	@Id @Column(name = "imageID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageID;
	
	@Column(name = "imagePath")
	private String imagePath;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "postID", nullable = false) // 設置外鍵
	private PostEntity post; // 對應的 PostEntity
	
	public Integer getImageID() {
		return imageID;
	}
	public void setImageID(Integer imageID) {
		this.imageID = imageID;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public PostEntity getPost() {
		return post;
	}
	public void setPost(PostEntity post) {
		this.post = post;
	}
	
}
