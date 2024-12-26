package com.todolist.service;

import com.todolist.domain.Todo;
import com.todolist.domain.User;
import com.todolist.repository.TodoRepository;
import com.todolist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TodoService {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;

	// Constructor Injection
	public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
		this.todoRepository = todoRepository;
		this.userRepository = userRepository;
	}

	// **특정 사용자(userId)의 모든 투두 리스트 조회**
	public List<Todo> findAllTodosByUserId(Long userId) {
		return todoRepository.findByUser_UserId(userId);
	}

	// **특정 날짜와 사용자(userId)의 투두 리스트 조회**
	public List<Todo> findTodosByDateAndUserId(LocalDate date, Long userId) {
		return todoRepository.findByDateAndUser_UserId(date, userId);
	}

	// **투두 항목 저장 (사용자 설정 포함)**
	@Transactional
	public void saveTodoWithUser(Todo todo, Long userId) {
		// 사용자 조회
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// Todo에 사용자 설정
		todo.setUser(user);
		todoRepository.save(todo);
	}

	// **투두 항목 삭제**
	@Transactional
	public void deleteTodoById(Long id) {
		todoRepository.deleteById(id);
	}

	// **투두 항목 수정**
	public Todo findById(Long id) {
		return todoRepository.findById(id).orElse(null);
	}

	// TodoService 클래스에 추가
	public void updateTodo(Todo todo) {
	    todoRepository.save(todo); // 상태 업데이트를 저장
	}
	
}
