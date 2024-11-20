package org.example.expert.domain.user.entity

import jakarta.persistence.*
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.user.enums.UserRole

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var email: String,
    var password: String? = null,
    var nickname: String,

    @Enumerated(EnumType.STRING)
    var userRole: UserRole

) : Timestamped() {

    fun changePassword(password: String?) {
        this.password = password
    }

    fun updateRole(userRole: UserRole) {
        this.userRole = userRole
    }
}
