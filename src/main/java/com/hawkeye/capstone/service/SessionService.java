package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Session;
import com.hawkeye.capstone.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session findOne(Long id){
        return sessionRepository.findOne(id);
    }

    public Long createSession(Group group){
        Session session = new Session();
        session.setStartTime(LocalDateTime.now());
        session.setGroup(group);
        group.getSessionList().add(session);
        sessionRepository.save(session);

        return session.getId();
    }
}
