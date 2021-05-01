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
    private final GroupRepository groupRepository;

    public void init(Queue queue, User user, WaitingList waitingList){
        waitingList.countPlus(1);
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

    //큐를 조회할 때 유저도 함께 조회
    public List<Queue> findByGroupWithUser(Long groupId){
        List<Queue> findQueueList = em.createQuery("select q from Queue q " +
                "join fetch q.user " +
                "where q.waitingList = :waitingList", Queue.class)
                .setParameter("waitingList", groupRepository.findOne(groupId).getWaitingList())
                .getResultList();

        return findQueueList;
    }

    @Transactional
    public void setStatus(Queue queue, WaitingStatus waitingStatus){
        Queue one = findOne(queue.getId());
        one.setStatus(waitingStatus);
    }
}
