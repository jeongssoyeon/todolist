package com.todolist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long todoId;

    @Column(nullable = false)
    private String content; // 할 일 내용

    @Column(nullable = false)
    private boolean completed = false; // 완료 여부

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now(); // 생성 날짜

    @Column(nullable = false)
    private LocalDate date; // 특정 날짜 추가

    // Foreign Key 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
