package com.eeit87t3.tickiteasy.image;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Chuan
 */
@Controller
public class ImageController {

	@Autowired
	private ResourceLoader resourceLoader;
	@Autowired
	private ImageUtil imageUtil;
	
	// 取得圖片
	@ResponseBody
	@GetMapping("/images/{folderName}/{fileName}")
    public ResponseEntity<?> getImage(
    		@PathVariable String folderName,
    		@PathVariable String fileName) {
		String pathPrefix = System.getProperty("user.dir") + "/images/";
        Resource resource = resourceLoader.getResource("file:" + pathPrefix + folderName + "/" + fileName);
        try {
			byte[] imageFile = StreamUtils.copyToByteArray(resource.getInputStream());
			
			HttpHeaders headers = new HttpHeaders();
			MediaType determinedMediaType = imageUtil.determineMediaType(fileName);
			headers.setContentType(determinedMediaType);
			
			return new ResponseEntity<>(imageFile, headers, HttpStatus.OK);
		} catch (IOException e) {
			System.out.println("在 /images/ 下找不到指定的圖片檔案。");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
}
