package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepositoryQuery {

    Optional<Todo> findByIdWithUser(Long id);

    Optional<Page<SearchTodoResponse>> searchTodosWithFilters(String title, String manager, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
