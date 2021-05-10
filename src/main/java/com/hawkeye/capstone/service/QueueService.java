package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.domain.WaitingList;
import com.hawkeye.capstone.domain.WaitingStatus;
import com.hawkeye.capstone.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    QueueRepository queueRepository;

    //큐 초기화 및 생성
    public void init(Queue queue, User user, WaitingList waitingList){
        waitingList.countPlus(1);
        queue.setUser(user);
        queue.setWaitingList(waitingList);
        queue.setStatus(WaitingStatus.WAIT);
        queueRepository.save(queue);
    }

}
