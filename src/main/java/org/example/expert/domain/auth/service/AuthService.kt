package org.example.expert.domain.auth.service

import lombok.RequiredArgsConstructor
import org.example.expert.domain.auth.dto.request.SignupRequest
import org.example.expert.domain.auth.dto.response.SignupResponse
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.user.entity.User
import org.example.expert.domain.user.enums.UserRole
import org.example.expert.domain.user.repository.UserRepository
import org.example.expert.security.util.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun signup(signupRequest: SignupRequest): SignupResponse {
        if (userRepository!!.existsByEmail(signupRequest.email)) {
            throw InvalidRequestException("이미 존재하는 이메일입니다.")
        }

        val encodedPassword = passwordEncoder.encode(signupRequest.password)

        val userRole = UserRole.of(signupRequest.userRole)

        /*User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                signupRequest.getNickname(),
                userRole
        );*/
        val newUser = User(
            email = signupRequest.email,
            password = encodedPassword,
            nickname = signupRequest.nickname,
            userRole = userRole
        )
        val savedUser = userRepository.save(newUser)

        val bearerToken = jwtUtil.createToken(savedUser.id, savedUser.email, savedUser.nickname, userRole)

        return SignupResponse(bearerToken)
    }
}
