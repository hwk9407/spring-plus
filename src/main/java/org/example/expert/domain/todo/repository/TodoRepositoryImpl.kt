package org.example.expert.domain.todo.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.expert.domain.comment.entity.QComment
import org.example.expert.domain.manager.entity.QManager
import org.example.expert.domain.todo.dto.response.SearchTodoResponse
import org.example.expert.domain.todo.entity.QTodo
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.QUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class TodoRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : TodoRepositoryQuery {

    override fun findByIdWithUser(id: Long?): Todo? {
        val qTodo = QTodo.todo
        val qUser = QUser.user

        return queryFactory
            .selectFrom(qTodo)
            .join(qTodo.user, qUser)
            .fetchJoin()
            .where(qTodo.id.eq(id))
            .fetchOne()
    }

    override fun searchTodosWithFilters(
        title: String?,
        manager: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        pageable: Pageable?
    ): Page<SearchTodoResponse>? {
        val qManager = QManager.manager
        val qTodo = QTodo.todo
        val qUser = QUser.user
        val qComment = QComment.comment

        val builder = BooleanBuilder()

        // 1. 제목으로 검색 (부분 일치)
        if (!title.isNullOrEmpty()) {
            builder.and(qTodo.title.containsIgnoreCase(title))
        }

        // 2. 일정 생성일 범위로 검색
        if (startDate != null && endDate != null) {
            builder.and(
                qTodo.createdAt.between(
                    startDate.toLocalDate().atStartOfDay(),
                    endDate.toLocalDate().atTime(23, 59, 59)
                )
            )
        }

        // 3. 담당자 닉네임으로 검색 (부분 일치)
        if (!manager.isNullOrEmpty()) {
            builder.and(qManager.user.nickname.containsIgnoreCase(manager))
        }


        // 4. 실제 데이터 조회
        val content = queryFactory.select(
            Projections.constructor(
                SearchTodoResponse::class.java,
                qTodo.title,
                qManager.count(),
                qComment.count()
            )
        )
            .from(qTodo)
            .innerJoin(qTodo.managers, qManager)
            .innerJoin(qManager.user, qUser)
            .leftJoin(qTodo.comments, qComment)
            .where(builder)
            .groupBy(qTodo.id)
            .offset(pageable!!.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        // 5. 총 페이지 수
        var total = queryFactory
            .select(qTodo.count())
            .from(qTodo)
            .innerJoin(qTodo.managers, qManager)
            .innerJoin(qManager.user, qUser)
            .leftJoin(qTodo.comments, qComment)
            .where(builder)
            .fetchOne()
        total = if ((total != null)) total else 0L


        return PageImpl(content, pageable, total)
    }
}
