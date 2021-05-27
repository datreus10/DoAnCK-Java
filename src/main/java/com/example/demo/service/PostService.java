package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    public boolean createPost(String postContent, MultipartFile multipartFile) {
        if (!postContent.isBlank()) {
            Post newPost = new Post();
            newPost.setPostContent(postContent);
            newPost.setUser(userService.getCurrentUser());
            if (multipartFile != null && !multipartFile.isEmpty()) {
                newPost.setMedia(storageService.uploadFile(multipartFile));
            }
            postRepo.save(newPost);
            return true;
        }
        return false;
    }

    public List<Map<String, String>> getAllPost() {
        List<Map<String, String>> result = new ArrayList<>();
        for (Post post : postRepo.findAllByOrderByPostTimeDesc()) {
            Map<String, String> temp = new HashMap<>();
            temp.put("postContent", post.getPostContent());
            temp.put("postDate", post.getPostTime());
            temp.put("userId", post.getUser().getUserId().toString());
            temp.put("userAvatar", post.getUser().getAvatar());
            temp.put("userName", post.getUser().getFullName());
            temp.put("postId", post.getPostId().toString());
            if (post.getMedia() != null && !post.getMedia().isEmpty()) {
                temp.put("postMediaUrl", storageService.getPresignedURL(post.getMedia()));
                temp.put("postMediaType", getMediaType(post.getMedia()));
            } else {
                temp.put("postMediaUrl", "");
                temp.put("postMediaType", "");
            }

            result.add(temp);
        }
        return result;
    }

    public String getMediaType(String fileName) {
        String extension = fileName.split("\\.")[1];
        if (extension.equals("mp4"))
            return "video";
        return "image";
    }
}
