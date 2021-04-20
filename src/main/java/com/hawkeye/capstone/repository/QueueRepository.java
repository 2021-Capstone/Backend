package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepository {

    private final EntityManager em;
    private final UserRepository userRepository;

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
}
