package com.todolist.controller;

import com.todolist.domain.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todolist.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 등록 화면 반환
    @GetMapping("/register")
    public String showRegisterForm() {
        return "user/register"; // templates/user/register.html 페이지 반환
    }

    // 사용자 등록 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user);
            // 회원가입 성공 메시지 전달
            redirectAttributes.addFlashAttribute("message", "회원가입 성공!");
            return "redirect:/login";
        } catch (Exception e) {
            // 중복 아이디 오류 처리
            redirectAttributes.addFlashAttribute("error", "이미 존재하는 아이디입니다.");
            return "redirect:/users/register";
        }
    }

}
