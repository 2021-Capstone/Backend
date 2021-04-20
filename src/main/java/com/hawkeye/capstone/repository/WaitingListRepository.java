package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.WaitingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WaitingListRepository {

    private final EntityManager em;

    public void save(WaitingList waitingList){
        em.persist(waitingList);
    }

    public WaitingList findOne(Long id){
        WaitingList findWaitingList = em.find(WaitingList.class, id);
        return findWaitingList;
    }
}
