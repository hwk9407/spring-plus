package org.example.expert.domain.comment.entity

import jakarta.persistence.*
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.user.entity.User

@Entity
@Table(name = "comments")
data class Comment(
    val contents: String,

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @JoinColumn(name = "todo_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val todo: Todo
) : Timestamped() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
