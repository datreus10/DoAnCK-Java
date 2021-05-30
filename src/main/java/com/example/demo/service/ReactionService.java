package com.example.demo.service;

import java.util.Map;

import com.example.demo.model.Post;
import com.example.demo.model.Reaction;
import com.example.demo.model.User;
import com.example.demo.repo.ReactionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReactionRepo reactionRepo;

    public String createReaction(Long postId) {
        Post post = postService.getPostById(postId);
        User user = userService.getCurrentUser();
        if (post != null) {
            Reaction reaction = reactionRepo.findByPostAndUser(post, user);
            if (reaction != null) {
                reactionRepo.delete(reaction);
                return "unreaction thành công";
            } else {
                reaction = new Reaction();
                reaction.setPost(post);
                reaction.setUser(user);
                reactionRepo.save(reaction);
                return "reaction thành công";
            }
        }
        throw new IllegalStateException("Post id không hợp lệ");
    }

    public boolean isCurrentUserLiked(Post post) {
        return reactionRepo.findByPostAndUser(post, userService.getCurrentUser()) == null ? false : true;
    }
}
