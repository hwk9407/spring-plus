package org.example.expert.domain.todo.service

import lombok.RequiredArgsConstructor
import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.response.SearchTodoResponse
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: User, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val weather = weatherClient.todayWeather

        val newTodo = Todo(
            todoSaveRequest.title,
            todoSaveRequest.contents,
            weather,
            authUser
        )
        val savedTodo = todoRepository.save(newTodo)

        return TodoSaveResponse(
            savedTodo.id,
            savedTodo.title,
            savedTodo.contents,
            weather,
            UserResponse(authUser.id, authUser.email)
        )
    }

    @Transactional(readOnly = true)
    fun getTodos(
        page: Int,
        size: Int,
        weather: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)

        val todos = todoRepository.findAllByWeatherAndDateRange(weather, startDate, endDate, pageable)

        return todos.map { todo: Todo ->
            TodoResponse(
                todo.id,
                todo.title,
                todo.contents,
                todo.weather,
                UserResponse(todo.user.id, todo.user.email),
                todo.createdAt,
                todo.modifiedAt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId) ?: throw InvalidRequestException("Todo not found")

        val user = todo.user

        return TodoResponse(
            todo.id,
            todo.title,
            todo.contents,
            todo.weather,
            UserResponse(user.id, user.email),
            todo.createdAt,
            todo.modifiedAt
        )
    }

    fun searchTodos(
        page: Int,
        size: Int,
        title: String?,
        manager: String?,
        startDateTime: LocalDateTime?,
        endDateTime: LocalDateTime?
    ): Page<SearchTodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        return todoRepository.searchTodosWithFilters(title, manager, startDateTime, endDateTime, pageable)
            ?: throw InvalidRequestException("Todo not found")
    }
}
