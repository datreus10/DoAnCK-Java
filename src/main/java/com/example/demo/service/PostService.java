package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.repo.PostRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private StorageService storageService;

    public boolean createPost(String postContent, MultipartFile multipartFile) {
        if (!postContent.isBlank()) {
            Post newPost = new Post();
            newPost.setPostContent(postContent);
            if (!multipartFile.isEmpty()) {
                newPost.setMedia(storageService.uploadFile(multipartFile));
            }
            postRepo.save(newPost);
            return true;
        }
        return false;
    }
}
