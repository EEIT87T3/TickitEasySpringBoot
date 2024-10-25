package com.eeit87t3.tickiteasy.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.post.entity.PostImagesEntity;
import com.eeit87t3.tickiteasy.test.TestImagesEntity;

public interface PostImagesRepo extends JpaRepository<PostImagesEntity, Integer>  {

}
