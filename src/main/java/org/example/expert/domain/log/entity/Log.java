package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.log.enums.EntityType;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "logs")
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long targetId;

    @Enumerated(EnumType.STRING)
    private EntityType targetType;
    private String actionDescription;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Log(Long userId, Long targetId, EntityType targetType, String actionDescription) {
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.actionDescription = actionDescription;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
