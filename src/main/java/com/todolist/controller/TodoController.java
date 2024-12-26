package com.todolist.controller;

import com.todolist.domain.Todo;
import com.todolist.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/todo")
public class TodoController {

	private final TodoService todoService;

	// Constructor Injection: TodoService를 의존성 주입받습니다.
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	// 로그인 여부 확인 유틸리티 메서드
	private boolean isLoggedIn(HttpSession session) {
		return session.getAttribute("userId") != null;
	}

	/**
	 * 특정 날짜의 투두 리스트를 표시합니다.
	 * 
	 * @param dateStr 선택된 날짜 (Optional)
	 * @param session 사용자의 세션 정보
	 * @param model   View에 데이터를 전달하기 위한 모델 객체
	 * @return todo/list 페이지 반환
	 */
	@GetMapping
	public String showTodoList(@RequestParam(value = "date", required = false) String dateStr, HttpSession session,
			Model model) {
		// 로그인 여부 확인
		if (!isLoggedIn(session)) {
			return "redirect:/login";
		}

		Long userId = (Long) session.getAttribute("userId");
		String username = (String) session.getAttribute("username");

		List<Todo> todos;

		// 선택된 날짜가 없으면 전체 리스트 반환
		if (dateStr == null || dateStr.isEmpty()) {
			todos = todoService.findAllTodosByUserId(userId);
		} else {
			// 선택된 날짜의 투두 리스트 반환
			LocalDate date = LocalDate.parse(dateStr);
			todos = todoService.findTodosByDateAndUserId(date, userId);
			model.addAttribute("selectedDate", dateStr);
		}

		// 사용자 이름과 투두 리스트를 모델에 추가
		model.addAttribute("username", username);
		model.addAttribute("todoList", todos);

		return "todo/list"; // templates/todo/list.html 반환
	}

	/**
	 * 새로운 할 일을 추가합니다.
	 * 
	 * @param todo    추가할 Todo 객체
	 * @param session 사용자의 세션 정보
	 * @return 리다이렉트 URL (선택된 날짜 기준)
	 */
	@PostMapping("/add")
	public String addTodo(@ModelAttribute Todo todo, HttpSession session) {
		// 로그인 여부 확인
		if (!isLoggedIn(session)) {
			return "redirect:/login";
		}

		Long userId = (Long) session.getAttribute("userId");
		// 할 일을 저장 (사용자 ID 포함)
		todoService.saveTodoWithUser(todo, userId);

		return "redirect:/todo?date=" + todo.getDate();
	}

	/**
	 * 특정 할 일을 삭제합니다.
	 * 
	 * @param id      삭제할 Todo ID
	 * @param dateStr 선택된 날짜 (Optional)
	 * @param session 사용자의 세션 정보
	 * @return 리다이렉트 URL (선택된 날짜 기준)
	 */
	@PostMapping("/delete/{id}")
	public String deleteTodo(@PathVariable Long id, @RequestParam(value = "date", required = false) String dateStr,
			HttpSession session) {
		// 로그인 여부 확인
		if (!isLoggedIn(session)) {
			return "redirect:/login";
		}

		// 할 일을 삭제
		todoService.deleteTodoById(id);

		// 선택된 날짜로 리다이렉트
		return dateStr != null ? "redirect:/todo?date=" + dateStr : "redirect:/todo";
	}

	/**
	 * 특정 할 일을 수정합니다.
	 * 
	 * @param id          수정할 Todo ID
	 * @param updatedTodo 수정된 Todo 데이터 (JSON Body)
	 * @return 성공/실패 메시지와 HTTP 상태 코드
	 */
	@PostMapping("/edit/{id}")
	@ResponseBody
	public ResponseEntity<String> editTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
		try {
			Todo existingTodo = todoService.findById(id);
			if (existingTodo != null) {
				existingTodo.setContent(updatedTodo.getContent());
				todoService.updateTodo(existingTodo);
				return ResponseEntity.ok("Success");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	// 상태 업데이트 처리
	@PostMapping("/toggle/{id}")
	@ResponseBody
	public ResponseEntity<String> toggleTodoStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> requestBody) {
	    try {
	        boolean completed = requestBody.get("completed");
	        Todo existingTodo = todoService.findById(id);

	        if (existingTodo != null) {
	            existingTodo.setCompleted(completed); // 상태 변경
	            todoService.updateTodo(existingTodo); // 업데이트 저장
	            return ResponseEntity.ok("Status updated");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo not found");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status");
	    }
	}

}
