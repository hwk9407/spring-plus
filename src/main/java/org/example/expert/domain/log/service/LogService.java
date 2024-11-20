package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.dto.request.LogAddDto;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 로그 추가는 기존 트랜잭션이 있더라도 독립적인 새로운 트랜잭션 수행
    public void add(LogAddDto logAddRequest) {
        Log log = new Log(
                logAddRequest.getUserId(),
                logAddRequest.getTargetId(),
                logAddRequest.getTargetType(),
                logAddRequest.getActionDescription()
        );
        logRepository.save(log);
    }
}
