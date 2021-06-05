package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AzureBlobService {
    @Autowired
    private BlobClientBuilder client;

    public String upload(MultipartFile file) {
        if (file != null && file.getSize() > 0) {
            try {
                // implement your own file name logic.
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                client.blobName(fileName).buildClient().upload(file.getInputStream(), file.getSize());
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public byte[] getFile(String fileName) {
        try {
            File temp = new File("/temp/" + fileName);
            BlobProperties properties = client.blobName(fileName).buildClient().downloadToFile(temp.getPath());
            byte[] content = Files.readAllBytes(Paths.get(temp.getPath()));
            temp.delete();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String name) {
        try {
            client.blobName(name).buildClient().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFileLink(String fileName) {
        if (fileName.contains("http")) {
            return fileName;
        } else {
            BlobClient blobClient = client.blobName(fileName).buildClient();
            BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true); // grant read
                                                                                                   // permission
            OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1); // after 1 days expire
            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, blobSasPermission)
                    .setStartTime(OffsetDateTime.now());
            return blobClient.getBlobUrl() + "?" + blobClient.generateSas(values);
        }

    }
}
