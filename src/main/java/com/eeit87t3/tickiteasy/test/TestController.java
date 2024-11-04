package com.eeit87t3.tickiteasy.test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eeit87t3.tickiteasy.admin.entity.Admin;
import com.eeit87t3.tickiteasy.admin.repository.AdminRepo;
import com.eeit87t3.tickiteasy.categoryandtag.entity.CategoryEntity;
import com.eeit87t3.tickiteasy.categoryandtag.entity.TagEntity;
import com.eeit87t3.tickiteasy.categoryandtag.service.CategoryService;
import com.eeit87t3.tickiteasy.categoryandtag.service.TagService;
import com.eeit87t3.tickiteasy.image.ImageDirectory;
import com.eeit87t3.tickiteasy.image.ImageUtil;

/**
 * @author Lilian (Curriane), Chuan (chuan13)
 */
@Controller
public class TestController {

	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private TestImagesRepo testImagesRepo;
	@Autowired
	private ImageUtil imageUtil;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TagService tagService;
	
	/**
	 * String Controller：資料庫連線測試。
	 */
	@ResponseBody
	@GetMapping("/test/connection")
	public String getMethodName() {
		Optional<Admin> optional = adminRepo.findById(1);
		if (optional.isPresent()) {
			if ("T3管理員".equals(optional.get().getName())) {
				return "連線成功！";
			}
		}
		return "連線失敗。";
	}
	
	/**
	 * Page Controller：模板顯示測試。
	 */
	@GetMapping("/test/admin-template")
	public String testTemplate() {
		return "test/adminTemplate";
	}
	
	/**
	 * 會員中心模板測試。
	 */
	@GetMapping("test/member-template")
	public String testMemberTemplate() {
		return "test/memberTemplate";
	}
	
	/**
	 * Image Controller: 圖片上傳測試。
	 */
	@PostMapping("/test/image")
	public String uploadImage(@RequestParam MultipartFile imageFile) {
		String baseName = UUID.randomUUID().toString();
		String pathString = null;
		try {
			pathString = imageUtil.saveImage(ImageDirectory.TEST, imageFile, baseName);
			TestImagesEntity testImagesPO = new TestImagesEntity();
			testImagesPO.setImagePath(pathString);
			testImagesRepo.save(testImagesPO);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		System.out.println(pathString);
		return "redirect:" + pathString;
	}
	
	/**
	 * Image Controller: 圖片讀取測試。
	 */
	@ResponseBody
	@GetMapping("/test/image")
	public ResponseEntity<?> getImage(@RequestParam Integer id) {
		Optional<TestImagesEntity> resultOptional = testImagesRepo.findById(id);
		if (resultOptional.isPresent()) {
			TestImagesEntity testImagesPO = resultOptional.get();
			String imagePath = testImagesPO.getImagePath();
			byte[] imageByteArray;
			try {
				imageByteArray = imageUtil.getImageByteArray(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			HttpHeaders headers = new HttpHeaders();
			MediaType determinedMediaType = imageUtil.determineMediaType(imagePath);
			headers.setContentType(determinedMediaType);
			
			return new ResponseEntity<>(imageByteArray, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * String Controller: 圖片刪除測試。
	 */
	@ResponseBody
	@DeleteMapping("/test/image")
	public String deleteImage(@RequestParam Integer id) {
		Optional<TestImagesEntity> resultOptional = testImagesRepo.findById(id);
		if (resultOptional.isPresent()) {
			TestImagesEntity testImagesPO = resultOptional.get();
			String imagePath = testImagesPO.getImagePath();
			Boolean deleteImageResult = null;
			try {
				deleteImageResult = imageUtil.deleteImage(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (deleteImageResult == null) {
				return "deleteImageResult == null";
			} else if (deleteImageResult) {
				testImagesRepo.delete(testImagesPO);
				return "已成功執行。";
			} else {
				System.out.println("deleteImageResult: " + deleteImageResult);
				return "未成功執行：請稍後再試。";
			}
		} else {
			return "未執行：找不到該筆資料。";
		}
	}
	
	/**
	 * JSON Controller：取得 Event 功能的活動型態測試。
	 */
	@ResponseBody
	@GetMapping("/test/event-categorylist")
	public List<CategoryEntity> findEventCategoryList() {
		return categoryService.findEventCategoryList();
	}
	/**
	 * JSON Controller：取得 Post 功能的活動型態測試。
	 */
	@ResponseBody
	@GetMapping("/test/post-taglist")
	public List<TagEntity> findPostCategoryList() {
		return tagService.findPostTagList();
	}
}
