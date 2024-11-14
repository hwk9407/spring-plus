package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;

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
}
