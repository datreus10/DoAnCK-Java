package com.example.demo.controller;

import java.util.Map;

import com.example.demo.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public String createComment(@RequestParam Map<String, String> data, Model model) {
        try {
            model.addAttribute("comment",
                    commentService.createComment(data.get("content"), Long.valueOf(data.get("postId"))));
            return "fragment :: comment";
        } catch (Exception e) {
            return "";
        }
    }
}
