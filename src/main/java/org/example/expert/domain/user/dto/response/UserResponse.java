package org.example.expert.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class UserResponse implements Serializable {

    private Long id;
    private String email;

    public UserResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
