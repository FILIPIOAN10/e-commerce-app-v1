package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Files names of the current / original files
        String originalFilename = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // mat.jpg -> 1234   --> 1234.jpg
        String fileName =randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.'))); // (1234.jpg)
        // images/
        String filePath = path + File.separator +fileName;

        // check if path exist and create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        // upload the server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // return file name
        return fileName;
    }
}
