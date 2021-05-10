package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Session;
import com.hawkeye.capstone.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session findOne(Long id){
        return sessionRepository.findOne(id);
    }

    @Transactional
    public Long createSession(Group group){
        Session session = new Session();
        session.setStartTime(LocalDateTime.now());
        session.setGroup(group);

        group.getSessionList().add(session); //어차피 한 그룹에서 수업은 하나만 할 수 있긴 함
        group.setOnAir(true);

        sessionRepository.save(session);

        return session.getId();
    }

    @Transactional
    public Long endSession(Session session){
        session.setEndTime(LocalDateTime.now());
        session.getGroup().getSessionList().remove(session);
        session.getGroup().setOnAir(false);

        return session.getId();
    }
}