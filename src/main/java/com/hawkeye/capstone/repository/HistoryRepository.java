package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.History;
import com.hawkeye.capstone.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    private final EntityManager em;
    private final SessionRepository sessionRepository;

    public void save(History history){
        em.persist(history);
    }

    public History findOne(Long historyId){
        return em.find(History.class, historyId);
    }

    public List<History> findAll(){
        return em.createQuery("select h from History h", History.class)
                .getResultList();
    }

    //수업의 Guest 히스토리 하나 검색
    public History findByGuestInSession(Long userId, Long sessionId){

        Session findSession = sessionRepository.findOne(sessionId);

        List<History> findHistoryList = findSession.getHistoryList();

        for (History history : findHistoryList) {

            if(history.getUser().getId() == userId)
                return history;

        }

        throw new IllegalStateException("해당 히스토리가 존재하지 않습니다.");
    }

    //수업의 Guest 히스토리 전체 검색
    public List<History> findAllInSession(Long sessionId){

        Session findSession = sessionRepository.findOne(sessionId);

        return findSession.getHistoryList();
    }
}
