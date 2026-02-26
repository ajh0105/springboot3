package com.example.tour.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(String uploadDir, MultipartFile file) throws Exception {
        if(file.isEmpty()) return null;

        String original = file.getOriginalFilename();  //C:\kt\images\abc.jpg
        String ext = original.substring(original.lastIndexOf("."));
        //임의의 파일명으로 생성 => UUID -> 2c38-4c67-6789.jpg
        String savedName = UUID.randomUUID() + ext;

        File dest = new File(uploadDir + savedName);         //C:\\upload\\2c38-4c67-6789.jpg
        file.transferTo(dest);      //실제 업로드

        return savedName;
    }
}
