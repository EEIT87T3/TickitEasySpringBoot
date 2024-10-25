package com.eeit87t3.tickiteasy.image;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Chuan(chuan13)
 */
@Controller
public class ImageController {

	@Autowired
	private ImageUtil imageUtil;
	
	/**
	 * JSON Controller: 取得圖片。
	 */
	@ResponseBody
	@GetMapping("/images/{folderName}/{fileName}")
    public ResponseEntity<?> getImage(
    		@PathVariable String folderName,
    		@PathVariable String fileName) {
		String pathString = "/images/" + folderName + "/" + fileName;
        try {
			byte[] imageByteArray = imageUtil.getImageByteArray(pathString);
			
			HttpHeaders headers = new HttpHeaders();
			MediaType determinedMediaType = imageUtil.determineMediaType(fileName);
			headers.setContentType(determinedMediaType);
			return new ResponseEntity<>(imageByteArray, headers, HttpStatus.OK);
		} catch (IOException e) {
			System.out.println("在 /images/ 下未找到指定的圖片檔案。");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
	
	@ResponseBody
	@GetMapping("/logo/{fileName}")
	public ResponseEntity<?> getLogo(@PathVariable String fileName) {
		String pathString = "/images/logo/" + fileName;
        try {
			byte[] imageByteArray = imageUtil.getImageByteArray(pathString);
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "image/svg+xml");
			
			return new ResponseEntity<>(imageByteArray, headers, HttpStatus.OK);
		} catch (IOException e) {
			System.out.println("在 /images/ 下未找到指定的圖片檔案。");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
