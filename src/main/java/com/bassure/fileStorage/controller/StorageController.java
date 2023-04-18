package com.bassure.fileStorage.controller;

import com.bassure.fileStorage.serviceimplementation.StorageServiceImplementation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageServiceImplementation service;

    @PostMapping("/upload")
    public List<String> uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "username") String username, @RequestParam(value = "fileType") String fileType) {
        return service.uploadFile(file, username, fileType);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "username") String username, @RequestParam(value = "fileType") String fileType) {
        byte[] data = service.downloadFile(username, fileType);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + username + fileType + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@RequestParam(value = "username") String username, @RequestParam(value = "fileType") String fileType) {
        return new ResponseEntity<>(service.deleteFile(username,fileType), HttpStatus.OK);
    }
}
