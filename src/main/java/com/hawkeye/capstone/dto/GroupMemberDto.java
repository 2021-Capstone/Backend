package com.hawkeye.capstone.dto;

import com.hawkeye.capstone.domain.Queue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupMemberDto {
    private List<UserSearchDto> userSearchDtoList = new ArrayList<>();

    public GroupMemberDto(List<Queue> queueList) {
        for (Queue queue : queueList) {
            userSearchDtoList.add(new UserSearchDto(
                    queue.getUser().getEmail(), queue.getUser().getName(), queue.getUser().getImageDir()));
        }
    }
}
