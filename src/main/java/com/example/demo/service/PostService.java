package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Comment;
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
    private AzureBlobService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private ReactionService reactionService;

    public Post getPostById(Long id) {
        return postRepo.findById(id).orElseGet(() -> null);
    }

    public boolean createPost(Map<String, String> body, MultipartFile multipartFile) {
        if (!body.get("postContent").isBlank() || (multipartFile != null && !multipartFile.isEmpty())) {
            Post newPost = new Post();
            newPost.setPostContent(body.get("postContent"));
            newPost.setMode(body.get("mode"));
            newPost.setUser(userService.getCurrentUser());
            if (multipartFile != null && !multipartFile.isEmpty()) {
                newPost.setMedia(storageService.upload(multipartFile));
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

    public List<Map<String, Object>> getPostMainPage() {
        List<User> friends = friendService.getListFriend();
        List<Post> posts = postRepo.findByUserInAndModeInOrderByPostTimeDesc(friends,
                Arrays.asList("public", "friend"));
        posts.addAll(postRepo.findPostByUserOrderByPostTimeDesc(userService.getCurrentUser()));
        posts.sort(Comparator.comparing(Post::getPostTime2).reversed());
        return fillter(posts);
    }

    public List<Map<String, Object>> getPostByUserAndMode(List<User> users, List<String> modes) {
        return fillter(postRepo.findByUserInAndModeInOrderByPostTimeDesc(users, modes));
    }

    public List<Map<String, Object>> getPostByUserAndPostId(User user, Long postId) {
        List<Post> posts = new ArrayList<>();
        posts.add(postRepo.findByUserAndPostId(user, postId));
        return fillter(posts);
    }

    public List<Map<String, Object>> fillter(List<Post> posts) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("postContent", post.getPostContent());
            temp.put("postDate", post.getPostTime());
            temp.put("userId", post.getUser().getUserId().toString());
            temp.put("userAvatar", storageService.getFileLink(post.getUser().getAvatar()));
            temp.put("currentUserAvatar", storageService.getFileLink(userService.getCurrentUser().getAvatar()));
            temp.put("userName", post.getUser().getFullName());
            temp.put("postId", post.getPostId().toString());
            temp.put("comments", addLinkAvatarToComment(post.getComments()));
            temp.put("reactions", post.getReactions());
            temp.put("mode", post.getMode());
            temp.put("own", post.getUser().equals(userService.getCurrentUser()));
            temp.put("isCurrentUserLiked", reactionService.isCurrentUserLiked(post));
            if (post.getMedia() != null && !post.getMedia().isEmpty()) {
                temp.put("postMediaUrl", storageService.getFileLink(post.getMedia()));
                temp.put("postMediaType", getMediaType(post.getMedia()));
            } else {
                temp.put("postMediaUrl", "");
                temp.put("postMediaType", "");
            }

            result.add(temp);
        }
        return result;
    }

    public List<Comment> addLinkAvatarToComment(List<Comment> comments) {
        for (int i = 0; i < comments.size(); i++)
            comments.get(i).getUser().setAvatarLink(storageService.getFileLink(comments.get(i).getUser().getAvatar()));
        return comments;
    }

    public String getMediaType(String fileName) {
        String extension = fileName.split("\\.")[1];
        if (extension.equals("mp4"))
            return "video";
        return "image";
    }

    public void changeMode(Map<String, String> body) {
        if (!body.get("postId").isBlank()) {
            Long id = Long.parseLong(body.get("postId"));
            Post temp = postRepo.findByUserAndPostId(userService.getCurrentUser(), id);
            if (temp != null) {
                temp.setMode(body.get("mode"));
                postRepo.save(temp);
            }
        }
    }
}
