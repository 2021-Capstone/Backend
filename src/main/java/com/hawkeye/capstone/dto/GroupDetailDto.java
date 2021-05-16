package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupDetailDto {
    private GroupRole role;
    private String host;
    private String groupCode;
    private String groupName;
    private int absenceTime;
    private int alertTime;
    private List<GroupMemberSimpleDto> groupMemberSimpleDtoList;
    private List<WaitingMemberDto> waitingMemberDtoList;

    public GroupDetailDto(GroupRole role, String host, String groupCode, String groupName, int absenceTime, int alertTime, List<GroupMemberSimpleDto> groupMemberSimpleDtoList, List<WaitingMemberDto> waitingMemberDtoList) {
        this.role = role;
        this.host = host;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.absenceTime = absenceTime;
        this.alertTime = alertTime;
        this.groupMemberSimpleDtoList = groupMemberSimpleDtoList;
        this.waitingMemberDtoList = waitingMemberDtoList;
    }

    public GroupDetailDto(GroupRole role, String host, String groupCode, String groupName, int absenceTime, int alertTime) {
        this.role = role;
        this.host = host;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.absenceTime = absenceTime;
        this.alertTime = alertTime;
    }
}
