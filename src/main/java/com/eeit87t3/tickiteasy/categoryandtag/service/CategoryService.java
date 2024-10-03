package com.eeit87t3.tickiteasy.categoryandtag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.repository.CategoryRepo;

/**
 * @author Chuan(chuan13)
 */
@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	/**
	 * 以 categoryString 取得 CategoryEntity 物件。
	 * 
	 * @param categoryString String
	 * @return categoryEntity - CategoryEntity
	 */
	public CategoryEntity findByCategoryString(String categoryString) {
		return categoryRepo.findByCategoryString(categoryString);
	}

	/**
	 * 取得 Event 功能的活動型態列表。
	 * 
	 * @return eventCategoryList - List&lt;CategoryEntity>：Event 功能的活動型態列表。
	 */
	public List<CategoryEntity> findEventCategoryList() {
		return categoryRepo.findByCategoryStatus((short) 0);
	}
	
	/**
	 * 取得 Product 功能的活動型態列表。
	 * 
	 * @return productCategoryList - List&lt;CategoryEntity>：Product 功能的活動型態列表。
	 */
	public List<CategoryEntity> findProductCategoryList() {
		return categoryRepo.findByCategoryStatus((short) 0);
	}
	public CategoryEntity findProductCategoryById(Integer categoryId) {
	    return categoryRepo.findById(categoryId).orElse(null);
	}
	
	/**
	 * 取得 CwdFunding 功能的活動型態列表。
	 * 
	 * @return fundProjCategoryList - List&lt;CategoryEntity>：CwdFunding 功能的活動型態列表。
	 */
	public List<CategoryEntity> findFundProjCategoryList() {
		return categoryRepo.findByCategoryStatus((short) 0);
	}
	
	/**
	 * 取得 Post 功能的活動型態列表。
	 * 
	 * @return postCategoryList - List&lt;CategoryEntity>：Post 功能的活動型態列表。
	 */
	public List<CategoryEntity> findPostCategoryList() {
		List<CategoryEntity> postCategoryList = new ArrayList<>();
		postCategoryList.addAll(categoryRepo.findByCategoryStatus((short) 0));
		postCategoryList.addAll(categoryRepo.findByCategoryStatus((short) 1));
		return postCategoryList;
	}
}
