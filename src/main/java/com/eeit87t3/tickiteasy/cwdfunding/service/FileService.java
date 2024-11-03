package com.eeit87t3.tickiteasy.cwdfunding.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TingXD (chen19990627)
 */
@Service
public class FileService {

    public String getFileExtension(MultipartFile file) {
        // 獲取原始文件名
        String originalFilename = file.getOriginalFilename();
        
        // 確保文件名不為空
        if (originalFilename != null && originalFilename.contains(".")) {
            // 從最後一個點開始截取副檔名
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // 返回空字串表示無法獲取副檔名
        return "";
    }
}

