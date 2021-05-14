package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.GroupSearchDto;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import com.hawkeye.capstone.service.WaitingListService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainPageApiController {

    private final GroupService groupService;
    private final UserService userService;
    private final WaitingListService waitingListService;

    //host 입장에서 WaitingList 조회(status가 wait인 것들만)
    @GetMapping("/api/main/getWaitingList/{groupId}")
    public WaitingListDto getWaitingList(@PathVariable Long groupId) {
        WaitingList waitingList = groupService.findOne(groupId).getWaitingList();

        return new WaitingListDto(waitingList);
    }

    //속해 있는 그룹 리스트 조회
    @GetMapping("/api/main/getGroupList/{userId}")
    public List<GroupSearchDto> getGroupList(@PathVariable Long userId) {

        List<GroupSearchDto> groupSearchDtoList = groupService.getGroupListWithRole(userId);

        return groupSearchDtoList;
    }

    @Data
    static class WaitingListDto {

        private int count;
        private String groupName;
        private List<QueueDto> queueList;

        public WaitingListDto(WaitingList waitingList) {
            count = waitingList.getCount();
            groupName = waitingList.getGroup().getName();
            List<QueueDto> queueListAll = waitingList.getQueueList().stream()
                    .map(queue -> new QueueDto(queue))
                    .collect(Collectors.toList());
            for (QueueDto queueDto : queueListAll) {
                if (queueDto.getWaitingStatus() == WaitingStatus.WAIT)
                    queueList.add(queueDto);
            }
        }
    }

    @Data
    static class QueueDto {
        private String userName;
        private String userEmail;
        private WaitingStatus waitingStatus;

        public QueueDto(Queue queue) {
            userName = queue.getUser().getName();
            userEmail = queue.getUser().getEmail();
            waitingStatus = queue.getStatus();
        }
    }
}
