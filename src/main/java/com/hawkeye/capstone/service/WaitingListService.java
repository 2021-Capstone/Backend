package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.WaitingList;
import com.hawkeye.capstone.domain.WaitingStatus;
import com.hawkeye.capstone.dto.UserWaitingListDto;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;
    private final QueueRepository queueRepository;

    public WaitingList findOne(Long id){
        return waitingListRepository.findOne(id);
    }

    @Transactional
    public void createWaitingList(WaitingList waitingList, Group group){
        waitingList.setGroup(group);
        waitingListRepository.save(waitingList);
    }

    @Transactional
    public void updateCount(WaitingList waitingList, int value){
        waitingList.countPlus(value);
    }

    @Transactional
    public List<UserWaitingListDto> getWaitingListDto(Long userId){

        List<UserWaitingListDto> userWaitingListDtoList = new ArrayList<>();
        List<Queue> findQueueList = queueRepository.findByUser(userId);

        for (Queue queue : findQueueList) {

            if(queue.getStatus() == WaitingStatus.WAIT){

                WaitingList findWaitingList = queue.getWaitingList();
                Group findGroup = findWaitingList.getGroup();
                userWaitingListDtoList.add(
                        new UserWaitingListDto(findGroup.getName(), findGroup.getId(), queue.getStatus(), findWaitingList.getId()));

            }

        }

        return userWaitingListDtoList;
    }
}
