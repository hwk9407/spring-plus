package org.example.expert.domain.log.entity

import jakarta.persistence.*
import org.example.expert.domain.log.enums.EntityType
import java.time.LocalDateTime

@Entity
@Table(name = "logs")
data class Log(
    val userId: Long,
    val targetId: Long,

    @Enumerated(EnumType.STRING)
    private val targetType: EntityType,
    val actionDescription: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(updatable = false)
    private var createdAt: LocalDateTime? = null

    @PrePersist
    protected fun onCreate() {
        this.createdAt = LocalDateTime.now()
    }
}