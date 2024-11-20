package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.dto.response.SearchTodoResponse
import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*

interface TodoRepositoryQuery {
    fun findByIdWithUser(id: Long?): Todo?

    fun searchTodosWithFilters(
        title: String?,
        manager: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageable: Pageable?
    ): Page<SearchTodoResponse>?
}
