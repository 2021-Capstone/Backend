package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

    private final EntityManager em;

    public Session findOne(Long sessionId){
        return em.find(Session.class, sessionId);
    }

    public void save(Session session){
        em.persist(session);
    }
}
