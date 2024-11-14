package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
            "WHERE (?1 IS NULL OR t.weather LIKE %?1%) " +
            "AND (?2 IS NULL OR t.modifiedAt >= ?2) " +
            "AND (?3 IS NULL OR t.modifiedAt <= ?3) " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeatherAndDateRange(String weather, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
