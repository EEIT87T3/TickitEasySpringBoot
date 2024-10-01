package com.eeit87t3.tickiteasy.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Chuan(chuan13)
 */
@Entity @Table(name = "testImages")
public class TestImagesEntity {

	@Id @Column(name = "imageID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageID;
	
	@Column(name = "imagePath")
	private String imagePath;

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
}
