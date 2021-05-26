package com.example.demo.controller;

import java.io.ByteArrayOutputStream;

import com.example.demo.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file-cloud")
@CrossOrigin("*")
public class StorageController{
    @Autowired
    private StorageService service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }
    
    @GetMapping("/stream/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        return ResponseEntity.ok()
                .header("Accept-Ranges", "bytes")
                .header("Expires", "0")
                .header("Cache-Control", "no-cache, no-store")
                .header("Connection", "keep-alive")
                .header("Content-Transfer-Encoding", "binary")
                .contentType(contentType(filename))
                .body(service.downloadFile(filename));
    }

    private MediaType contentType(String keyname) {
        String[] arr = keyname.split("\\.");
        String type = arr[arr.length - 1];
        switch (type) {
        case "txt":
            return MediaType.TEXT_PLAIN;
        case "png":
            return MediaType.IMAGE_PNG;
        case "jpg":
            return MediaType.IMAGE_JPEG;
        case "mp4":
            return MediaType.valueOf("video/mp4");
        default:
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
