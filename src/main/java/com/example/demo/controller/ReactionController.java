package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.service.ReactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/react")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, String>> createPost(@RequestBody Map<String, String> body) {
        Map<String, String> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        String msg = "Unexpected Error";
        try {
            Long postId = Long.valueOf(body.get("postId"));
            msg = reactionService.createReaction(postId);
        } catch (Exception e) {
            msg = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } finally {
            response.put("msg", msg);
        }
        return new ResponseEntity<>(response, status);

    }
}
