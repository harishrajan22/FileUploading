package com.bassure.fileStorage.serviceimplementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.bassure.fileStorage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageServiceImplementation implements StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public List<String> uploadFile(MultipartFile file,String fileType,String userName) {
        File fileObj = convertMultiPartFileToFile(file);
        List<String> uploadedDetails = new ArrayList<>();
        String fileName = userName+fileType;
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        uploadedDetails.add(bucketName);
        uploadedDetails.add(fileName);
        fileObj.delete();
        return uploadedDetails;
    }

    @Override
    public byte[] downloadFile(String username,String fileType) {
        String fileName = username+fileType;
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteFile(String username,String fileType) {
        String fileName = username+fileType;
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " Removed Successfully";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try ( FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
