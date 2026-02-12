package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/study")
public class StudyController {

    @GetMapping("/syntax")
    public String syntax(Model model){
        model.addAttribute("rawText",
                "Welcome to <strong>Thymeleaf</strong>");
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "첫 번째 학습", "홍길동", 10));
        posts.add(new Post(2L, "두 번째 학습", "성춘향", 5));
        model.addAttribute("posts", posts);

        model.addAttribute("boardForm", new Post());
        return "study/syntax";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Post {
        private Long id;
        private String title;
        private String writer;
        private int views;
        private Post() { }
    }
}
