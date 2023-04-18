package com.bassure.fileStorage.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    public List<String> uploadFile(MultipartFile file,String fileType,String userName);
    public byte[] downloadFile(String username,String fileType);
    public String deleteFile(String username,String fileType);
    
}
