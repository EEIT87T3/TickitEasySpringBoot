package com.eeit87t3.tickiteasy.cwdfunding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eeit87t3.tickiteasy.cwdfunding.entity.Tag;

/**
 * @author Chuan(chuan13)
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

	List<Tag> findByTagStatus(Short tagStatus);
}
