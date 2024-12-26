package com.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 루트 경로("/") 요청을 loginPage로 리다이렉트
    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }
}
