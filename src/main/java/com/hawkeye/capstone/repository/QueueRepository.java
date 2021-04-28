package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.domain.WaitingList;
import com.hawkeye.capstone.domain.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepository {

    private final EntityManager em;
    private final UserRepository userRepository;

    public void init(Queue queue, User user, WaitingList waitingList){
        waitingList.setCount(waitingList.getCount() + 1);
        queue.setUser(user);
        queue.setWaitingList(waitingList);
        queue.getWaitingList().getQueueList().add(queue);
        queue.setStatus(WaitingStatus.WAIT);
    }

    public void save(Queue queue){
        em.persist(queue);
    }

    public Queue findOne(Long id){
        return em.find(Queue.class, id);
    }

    public List<Queue> findByUser(Long userId){
        List<Queue> findQueueList = em.createQuery("select q from Queue q where q.user = :user", Queue.class)
                .setParameter("user", userRepository.findOne(userId))
                .getResultList();

        return findQueueList;

    }

    @Transactional
    public void setStatus(Queue queue, WaitingStatus waitingStatus){
        Queue one = findOne(queue.getId());
        one.setStatus(waitingStatus);
    }
}