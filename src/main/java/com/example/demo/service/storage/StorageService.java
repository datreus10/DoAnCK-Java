package com.example.demo.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file);

    byte[] getFile(String fileName);

    boolean deleteFile(String name);

    String getFileLink(String fileName);

    Resource load(String filename);
}
