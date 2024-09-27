package com.eeit87t3.tickiteasy.image.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

	@Autowired
	private ResourceLoader resourceLoader;
	
	// 取得圖片
	@ResponseBody
	@GetMapping("/images/{folderName}/{fileName}")
    public ResponseEntity<?> getImage(
    		@PathVariable String folderName,
    		@PathVariable String fileName) {
		String basePath = System.getProperty("user.dir") + "/images/";
        Resource resource = resourceLoader.getResource("file:" + basePath + folderName + "/" + fileName);
        try {
			byte[] imageFile = StreamUtils.copyToByteArray(resource.getInputStream());
			
			HttpHeaders headers = new HttpHeaders();
			MediaType determinedMediaType = determineMediaType(fileName);
			headers.setContentType(determinedMediaType);
			
			return new ResponseEntity<>(imageFile, headers, HttpStatus.OK);
		} catch (IOException e) {
			System.out.println("在 /images/ 下找不到指定的圖片檔案。");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
	
	// 上傳圖片
	@PostMapping("/images/test/upload")
	public String uploadImage(
			@RequestParam MultipartFile imageFile) {
		String uuidFileName = UUID.randomUUID().toString() + "." +  getFileExtension(imageFile.getOriginalFilename());
		try {
			File uploadDirectory = new File(System.getProperty("user.dir") + "/images/test");
			if (!uploadDirectory.exists()) {
				uploadDirectory.mkdirs();
			}
			
			File destinationFile = new File(uploadDirectory, uuidFileName);
			imageFile.transferTo(destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/images/test/" + uuidFileName;
	}
	
	
	
    private MediaType determineMediaType(String filename) {
        String extension = getFileExtension(filename);

        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
//            case "webp":
//            	return MediaType.valueOf("image/webp");
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // 回傳二進制 stream
        }
    }

    private String getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == filename.length() - 1) {
            return ""; // 沒有找到副檔名
        }
        return filename.substring(lastIndex + 1); // 回傳副檔名
    }
}
