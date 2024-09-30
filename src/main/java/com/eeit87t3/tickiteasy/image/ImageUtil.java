package com.eeit87t3.tickiteasy.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Chuan
 */
@Component
public class ImageUtil {
	
	/**
	 * 將使用者上傳的圖片存入檔案系統。<br><br>
	 * 備註：請限制使用者上傳的圖片格式為 jpg、jpeg、png、gif 其中之一。<br>
	 * 備註：檔名不可有中文與空白。
	 * 
	 * @param imageDirectory ImageDirectory：Enum，請輸入對應名稱。
	 * @param imageFile MultipartFile：使用者上傳的圖片。
	 * @param baseName String：要儲存的基本檔名（不含副檔名）。
	 * @return pathString - String：可放入資料庫的路徑字串。
	 * @throws IllegalStateException MultipartFile 已被處理或轉移。
	 * @throws IOException 儲存為檔案的過程發生錯誤。
	*/
	public String saveImage(ImageDirectory imageDirectory, MultipartFile imageFile, String baseName) throws IllegalStateException, IOException {
		String fullFileName = baseName + "." +  getFileExtension(imageFile.getOriginalFilename());
		File destinationFile = new File(imageDirectory.getDirectory(), fullFileName);
		imageFile.transferTo(destinationFile);
		
		return imageDirectory.getFileNamePrefix() + fullFileName;
	}
	
	/**
	 * 將圖片從檔案系統裡刪除。<br><br>
	 * 注意：時間間隔太短，可能導致刪除失敗；目前尚未找到解決方法，請稍後再試。
	 * 
	 * @param pathString String：資料庫儲存的路徑字串。
	 * @return result - Boolean：此圖片檔案是否已不存在。
	*/
	public Boolean deleteImage(String pathString) {
		Boolean result = false;
		File imageFile = new File(System.getProperty("user.dir") + pathString);
		System.out.println(imageFile.getAbsolutePath());
		if (imageFile.exists() && imageFile.isFile()) {
//			System.out.println("imageFile.canWrite(): " + imageFile.canWrite());  // 測試用
			/* boolean deleteResult = */ imageFile.delete();
//			System.out.println("imageFile.delete(): " + deleteResult);  // 測試用
			result = !imageFile.exists();
		}
		return result;
	}
	
	/**
	 * 取得圖片的 byte 陣列。
	 * 
	 * @param pathString String：資料庫儲存的路徑字串。
	 * @return imageByteArray - byte[]：<code>null</code> 代表指定檔案不存在。
	 * @throws IOException 讀取指定檔案的過程發生錯誤。
	*/
	public byte[] getImageByteArray(String pathString) throws IOException {
		byte[] imageByteArray = null;
		File imageFile = new File(System.getProperty("user.dir") + pathString);
		if (imageFile.exists() && imageFile.isFile()) {
			imageByteArray = Files.readAllBytes(imageFile.toPath());
		}
		return imageByteArray;
	}
	
	/**
	 * 取得檔名中的副檔名。<br><br>
	 * 由 ChatGPT 生成。
	 * 
	 * @param filename String：檔名。
	 * @return fileExtension - String：副檔名；<code>（空字串）</code> 代表沒有找到副檔名。
	*/
    public String getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == filename.length() - 1) {
            return "";  // 沒有找到副檔名
        }
        return filename.substring(lastIndex + 1);  // 回傳副檔名
    }
    
	/**
	 * 根據副檔名判斷，取得圖片檔案對應的 MediaType 物件。<br><br>
	 * 由 ChatGPT 生成。
	 * 
	 * @param filename String：檔名。
	 * @return determinedMediaType - MediaType
	*/
    public MediaType determineMediaType(String filename) {
        String extension = getFileExtension(filename);

        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // 回傳二進制 stream
        }
    }
}
