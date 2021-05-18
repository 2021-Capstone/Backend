package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.TimeLineLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class TimeLineLogRepository {

    private final EntityManager em;
    private final HistoryRepository historyRepository;

    public void save(TimeLineLog timeLineLog){
        em.persist(timeLineLog);
    }

    public List<TimeLineLog> findByHistory(Long historyId){
        return em.createQuery("select t from TimeLineLog t " +
                "where t.history = :history", TimeLineLog.class)
                .setParameter("history", historyRepository.findOne(historyId))
                .getResultList();
    }
}
