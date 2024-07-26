package com.thantrick.S3StorageDemo.controller;

import com.thantrick.S3StorageDemo.service.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3upload")
public class S3DemoController {

    @Autowired
    private S3StorageService s3StorageService;

    @GetMapping("/testApi")
    public String TestApi(){
        return "Api Working!!!!";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileToS3(@RequestParam("file") MultipartFile file){
        return new ResponseEntity<>(s3StorageService.uploadFile(file), HttpStatus.ACCEPTED);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFileFromS3(@PathVariable("fileName") String fileName){

        byte[] fileByteArray = s3StorageService.downloadFile(fileName);
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileByteArray);
        return ResponseEntity
                    .ok()
                    .contentLength(fileByteArray.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; fileName=\""+fileName+"\"")
                    .body(byteArrayResource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFileFromS3(@PathVariable("fileName") String fileName){

        return new ResponseEntity<>(s3StorageService.deleteFile(fileName), HttpStatus.ACCEPTED);
    }

}
