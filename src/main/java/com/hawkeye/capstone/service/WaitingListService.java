package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.WaitingList;
import com.hawkeye.capstone.repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;

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
}
