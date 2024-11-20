package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface TodoRepository : JpaRepository<Todo?, Long?>, TodoRepositoryQuery {
    @Query(
        "SELECT t FROM Todo t LEFT JOIN FETCH t.user u " +
                "WHERE (?1 IS NULL OR t.weather LIKE %?1%) " +
                "AND (?2 IS NULL OR t.modifiedAt >= ?2) " +
                "AND (?3 IS NULL OR t.modifiedAt <= ?3) " +
                "ORDER BY t.modifiedAt DESC"
    )
    fun findAllByWeatherAndDateRange(
        weather: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageable: Pageable?
    ): Page<Todo>
}
