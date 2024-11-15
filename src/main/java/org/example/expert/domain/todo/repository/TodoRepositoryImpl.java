package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.SearchTodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long id) {
        QTodo qTodo = QTodo.todo;
        QUser qUser = QUser.user;

        return Optional.ofNullable(queryFactory
                .selectFrom(qTodo)
                .join(qTodo.user, qUser)
                .fetchJoin()
                .where(qTodo.id.eq(id))
                .fetchOne()
        );
    }

    @Override
    public Optional<Page<SearchTodoResponse>> searchTodosWithFilters(String title, String manager, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        QManager qManager = QManager.manager;
        QTodo qTodo = QTodo.todo;
        QUser qUser = QUser.user;
        QComment qComment = QComment.comment;

        BooleanBuilder builder = new BooleanBuilder();

        // 1. 제목으로 검색 (부분 일치)
        if (title != null && !title.isEmpty()) {
            builder.and(qTodo.title.containsIgnoreCase(title));
        }

        // 2. 일정 생성일 범위로 검색
        if (startDate != null && endDate != null) {
            builder.and(qTodo.createdAt.between(startDate.toLocalDate().atStartOfDay(), endDate.toLocalDate().atTime(23, 59, 59)));
        }

        // 3. 담당자 닉네임으로 검색 (부분 일치)
        if (manager != null && !manager.isEmpty()) {
            builder.and(qManager.user.nickname.containsIgnoreCase(manager));
        }


        // 4. 실제 데이터 조회
        List<SearchTodoResponse> content = queryFactory.select(Projections.constructor(SearchTodoResponse.class,
                        qTodo.title,
                        qManager.count(),
                        qComment.count()
                ))
                .from(qTodo)
                .innerJoin(qTodo.managers, qManager)
                .innerJoin(qManager.user, qUser)
                .leftJoin(qTodo.comments, qComment)
                .where(builder)
                .groupBy(qTodo.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 5. 총 페이지 수
        Long total = queryFactory
                .select(qTodo.count())
                .from(qTodo)
                .innerJoin(qTodo.managers, qManager)
                .innerJoin(qManager.user, qUser)
                .leftJoin(qTodo.comments, qComment)
                .where(builder)
                .fetchOne();
        total = (total != null) ? total : 0L;


        return Optional.of(new PageImpl<>(content, pageable, total));
    }
}
