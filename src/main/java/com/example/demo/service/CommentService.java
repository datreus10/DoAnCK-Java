package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.repo.CommentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    public Comment createComment(String commentContent, Long postId) {
        Post post = postService.getPostById(postId);
        if (post != null) {
            Comment comment = new Comment();
            comment.setCommentContent(commentContent);
            comment.setUser(userService.getCurrentUser());
            comment.setPost(post);
            return commentRepo.save(comment);
        }
        return null;
    }
}
