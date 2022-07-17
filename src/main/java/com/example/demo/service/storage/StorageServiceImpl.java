package com.example.demo.service.storage;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("StorageServiceImpl")
public class StorageServiceImpl implements StorageService {
    private final Path root = Paths.get("upload");

    @Override
    public String upload(MultipartFile file) {
        File directory = new File("upload");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filename;
        try {
            filename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return filename;
    }

    @Override
    public byte[] getFile(String fileName) {
        try {
            byte[] content = Files.readAllBytes(this.root.resolve(fileName));
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteFile(String name) {
        File myObj = new File(name);
        return myObj.delete();

    }

    @Override
    public String getFileLink(String fileName) {
        return "files/" + fileName;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
