package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final UserService userService;
    private final WaitingListService waitingListService;

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

    //입장 수락
    @Transactional
    public void allowMember(Long groupId, String email){

        User findUser = userService.findByEmail(email);
        //해당 유저가 속한 Queue 전부 조회
        List<Queue> queueList = queueRepository.findByUser(findUser.getId());
        //각 Queue가 입장 신청을 한 그룹의 waitingList인지 조회
        for (Queue queue : queueList) {
            if(queue.getWaitingList().getGroup().getId() == groupId){
                //변경 감지 -> status ACCEPT로 변경
                queueRepository.setStatus(queue, WaitingStatus.ACCEPT);
                waitingListService.updateCount(queue.getWaitingList(), -1);
            }
        }

    }

    //입장 거절
    @Transactional
    public void rejectMember(Long groupId, String email){

        User findUser = userService.findByEmail(email);
        //해당 유저가 속한 Queue 전부 조회
        List<Queue> queueList = queueRepository.findByUser(findUser.getId());
        //각 Queue가 입장 신청을 한 그룹의 waitingList인지 조회
        for (Queue queue : queueList) {
            if(queue.getWaitingList().getGroup().getId() == groupId){
//                //변경 감지 -> status REJECT로 변경
//                queueRepository.setStatus(queue, WaitingStatus.REJECT);
                waitingListService.updateCount(queue.getWaitingList(), -1);
                //해당 Queue 삭제
                queueRepository.delete(queue);
            }
        }
    }
}
