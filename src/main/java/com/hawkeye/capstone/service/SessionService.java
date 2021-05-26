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
    private final GroupService groupService;

    public Session findOne(Long id) {
        return sessionRepository.findOne(id);
    }

    @Transactional
    public Long createSession(Long groupId) {

        Group group = groupService.findOne(groupId);
        Session session = new Session();
        session.setStartTime(LocalDateTime.now());
        session.setGroup(group);
        group.setOnAir(true);
        sessionRepository.save(session);

        return session.getId();
    }

    @Transactional
    public Long endSession(Long sessionId) {

        Session session = sessionRepository.findOne(sessionId);
        session.setEndTime(LocalDateTime.now());
        //변경 감지
        Group group = groupService.findOne(session.getGroup().getId());
        group.setOnAir(false);

        return session.getId();
    }

    @Transactional
    public boolean isOnAir(Long sessionId) {
        if (findOne(sessionId).getGroup().isOnAir())
            return true;
        else
            return false;
    }
}
