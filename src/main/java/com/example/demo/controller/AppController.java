package com.example.demo.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AppController {

    @GetMapping()
    public String getMainPage() {
        return "index";
    }
}
