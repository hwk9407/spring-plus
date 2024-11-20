package org.example.expert.domain.log.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.log.enums.EntityType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogAddDto {

    private Long userId;
    private Long targetId;
    private EntityType targetType;
    private String actionDescription;
}
