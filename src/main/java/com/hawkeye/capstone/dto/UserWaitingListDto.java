package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.WaitingStatus;
import lombok.Data;

@Data
public class UserWaitingListDto{
    private Long id;
    private String groupName;
    private Long groupId;
    private WaitingStatus waitingStatus;

    public UserWaitingListDto(String groupName, Long groupId, WaitingStatus waitingStatus, Long id) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.waitingStatus = waitingStatus;
        this.id = id;
    }
}