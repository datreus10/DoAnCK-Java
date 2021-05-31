package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Post;
import com.example.demo.model.User;
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

    @Autowired
    private ReactionService reactionService;

    public Post getPostById(Long id) {
        return postRepo.findById(id).orElseGet(() -> null);
    }

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

    public List<Map<String, Object>> getAllPost() {
        return fillter(postRepo.findAllByOrderByPostTimeDesc());
    }

    public List<Map<String, Object>> getPostByUser(User user) {
        return fillter(postRepo.findPostByUserOrderByPostTimeDesc(user));
    }

    public List<Map<String, Object>> fillter(List<Post> posts) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("postContent", post.getPostContent());
            temp.put("postDate", post.getPostTime());
            temp.put("userId", post.getUser().getUserId().toString());
            temp.put("userAvatar", post.getUser().getAvatar());
            temp.put("userName", post.getUser().getFullName());
            temp.put("postId", post.getPostId().toString());
            temp.put("comments", post.getComments());
            temp.put("reactions", post.getReactions());
            temp.put("isCurrentUserLiked", reactionService.isCurrentUserLiked(post));
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
