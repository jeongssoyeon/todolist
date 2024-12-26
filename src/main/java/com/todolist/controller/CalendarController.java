package com.todolist.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;



@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendarPage(HttpSession session, Model model) {
        // 세션에서 username 가져오기
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login"; // 세션에 아이디가 없으면 로그인 페이지로 리다이렉트
        }

        model.addAttribute("username", username);
        return "calendar"; // calendar.html 반환
    }
}
