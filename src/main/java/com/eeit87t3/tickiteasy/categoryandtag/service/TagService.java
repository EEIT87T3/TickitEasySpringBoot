package com.eeit87t3.tickiteasy.categoryandtag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.TagRepo;

/**
 * @author Chuan (chuan13), Liang123456123
 */
@Service
public class TagService {

	@Autowired
	private TagRepo tagRepo;
	
	/**
	 * 以 tagString 取得 TagEntity 物件。
	 * 
	 * @param tagString String
	 * @return tagEntity - TagEntity
	 */
	public TagEntity findByTagString(String tagString) {
		return tagRepo.findByTagString(tagString);
	}
	
	/**
	 * 取得 Event 功能的主題分類列表。
	 * 
	 * @return eventTagList - List&lt;TagEntity>：Event 功能的主題分類列表。
	 */
	public List<TagEntity> findEventTagList() {
	    return tagRepo.findByTagStatus((short) 0);
	}

	/**
	 * 取得 Product 功能的主題分類列表。
	 * 
	 * @return productTagList - List&lt;TagEntity>：Product 功能的主題分類列表。
	 */
	public List<TagEntity> findProductTagList() {
	    return tagRepo.findByTagStatus((short) 0);
	}
	public TagEntity findProductTagById(Integer tagId) {
	    return tagRepo.findById(tagId).orElse(null);
	}
	/**
	 * 取得 CwdFunding 功能的主題分類列表。
	 * 
	 * @return fundProjTagList - List&lt;TagEntity>：CwdFunding 功能的主題分類列表。
	 */
	public List<TagEntity> findFundProjTagList() {
	    return tagRepo.findByTagStatus((short) 0);
	}

	/**
	 * 取得 Post 功能的主題分類列表。
	 * 
	 * @return postTagList - List&lt;TagEntity>：Post 功能的主題分類列表。
	 */
	public List<TagEntity> findPostTagList() {
	    List<TagEntity> postTagList = new ArrayList<>();
	    postTagList.addAll(tagRepo.findByTagStatus((short) 0));
	    postTagList.addAll(tagRepo.findByTagStatus((short) 1));
	    return postTagList;
	}
}
