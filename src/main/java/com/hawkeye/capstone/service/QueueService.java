package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.domain.WaitingList;
import com.hawkeye.capstone.domain.WaitingStatus;
import com.hawkeye.capstone.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    //큐 초기화 및 생성
    @Transactional
    public void init(User user, WaitingList waitingList){

        waitingList.countPlus(1);
        Queue queue = Queue.builder()
                .user(user)
                .waitingList(waitingList)
                .status(WaitingStatus.WAIT)
                .build();
        queueRepository.save(queue);
    }

}
