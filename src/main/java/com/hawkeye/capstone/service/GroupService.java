package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.WaitingStatus;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final QueueRepository queueRepository;

    public List<Group> groupByUser(Long userId){
        List<Queue> queueByUserList = queueRepository.findByUser(userId);

        List<Group> groupList = new ArrayList<>();

        for (Queue queue : queueByUserList) {
            if(queue.getWaitingList().getStatus() == WaitingStatus.ACCEPT)
                groupList.add(queue.getWaitingList().getGroup());
        }

        return groupList;
    }
}
