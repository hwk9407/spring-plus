package org.example.expert.domain.todo.entity;

import jakarta.persistence.*
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Entity
@Table(name = "todos")
data class Todo(
    var title: String,
    var contents: String,
    var weather: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

) : Timestamped() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    val comments: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.ALL])
    val managers: MutableList<Manager> = mutableListOf()

    init {
        managers.add(Manager(user, this))
    }
}