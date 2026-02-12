package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        // Model에 데이터 담기 (String, 객체, 시간 등)
        model.addAttribute("message", "안녕하세요! Thymeleaf의 세계에 오신 걸 환영합니다.");
        model.addAttribute("serverTime", LocalDateTime.now());

        // Map 형태의 데이터도 전달 가능
        model.addAttribute("user", Map.of(
                "name", "홍길동",
                "role", "ADMIN"
        ));

        // 반환 값은 templates 폴더 내의 파일명 (hello.html)
        return "hello";
    }
}