package com.todolist.repository;

import com.todolist.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // userId로 전체 조회
    List<Todo> findByUser_UserId(Long userId);
    // 날짜와 userId로 조회
    List<Todo> findByDateAndUser_UserId(LocalDate date, Long userId);
}
