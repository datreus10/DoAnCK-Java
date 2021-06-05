package com.example.demo.controller;

import java.util.Map;

import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping()
    @ResponseBody
    public ResponseEntity<String> createPost(@RequestParam Map<String, String> body,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        boolean success = false;
        try {
            success = postService.createPost(body.get("postContent"), file);
        } catch (Exception e) {
            success = false;
        }
        if (success)
            return new ResponseEntity<>("Đăng bài thành công", HttpStatus.OK);
        return new ResponseEntity<>("Đăng bài thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // ResponseEntity<List<Map<String, String>>>
    @GetMapping
    public String getAllPosts(Model model){
        model.addAttribute("listPost", postService.getAllPost());
        return "fragment :: list_post";
    }
}
