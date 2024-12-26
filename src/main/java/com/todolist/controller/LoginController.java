package com.todolist.controller;

import com.todolist.domain.User;
import com.todolist.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

	@Autowired
	private UserRepository userRepository;

	// 로그인 페이지 보여주기
	@GetMapping("/login")
	public String showLoginPage() {
		return "loginPage"; // login.html 반환
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		Optional<User> user = userRepository.findByUsername(username);

		// Optional의 isPresent를 사용하여 사용자 검증
		if (user.isPresent()) {
			User foundUser = user.get(); // Optional에서 User 가져오기
			if (foundUser.getPassword().equals(password)) { // 비밀번호 일치 확인
				 // 로그인 성공 시 세션에 userId와 username 저장
                session.setAttribute("userId", foundUser.getUserId()); // userId 저장
                session.setAttribute("username", username); // username 저장
                
                System.out.println("Login Success: userId = " + foundUser.getUserId()); // 로그 출력
				return "redirect:/calendar"; // 로그인 성공 시 달력 페이지로 이동
			} else {
				model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
			}
		} else {
			model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
		}
		return "loginPage"; // 로그인 실패 시 다시 로그인 페이지로 반환
	}

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/login"; // 로그인 페이지로 리다이렉트
	}
}
