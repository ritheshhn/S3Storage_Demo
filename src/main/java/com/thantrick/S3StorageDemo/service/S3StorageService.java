package com.thantrick.S3StorageDemo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadFile(MultipartFile file){
        String filename = System.currentTimeMillis() +"_"+ file.getOriginalFilename();

        File fileConverted = convertFile(file);

        s3Client.putObject(bucketName, filename, fileConverted);

        fileConverted.delete();

        return filename + " file uploaded to S3 Bucket "+ bucketName;
    }

    public byte[] downloadFile(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

        byte[] fileContent = null;

        try {
            fileContent = IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public String deleteFile(String fileName){
        s3Client.deleteObject(bucketName, fileName);

        return fileName + " file deleted from S3 Bucket "+ bucketName;
    }

    private File convertFile(MultipartFile file){
        File convertedFile = new File(file.getName());
        FileOutputStream fos =  null;
        try {
            fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
