package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.GroupSearchDto;
import com.hawkeye.capstone.dto.RecentTrendDto;
import com.hawkeye.capstone.dto.UserWaitingListDto;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.QueueService;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MainPageApiController {

    private final GroupService groupService;
    private final UserService userService;
    private final WaitingListService waitingListService;
    private final QueueRepository queueRepository;

    @GetMapping("api/main/getWaitingList/{userId}")
    //자신이 속한 WaitingList 전부 조회
    public List<UserWaitingListDto> getWaitingList(@PathVariable long userId){

        List<UserWaitingListDto> userWaitingListDtoList = waitingListService.getWaitingListDto(userId);
        userWaitingListDtoList.sort(new Comparator<UserWaitingListDto>() {
            @Override
            public int compare(UserWaitingListDto o1, UserWaitingListDto o2) {
                if(o1.getId() == o2.getId())
                    return 0;
                else if(o2.getId() > o1.getId())
                    return 1;
                else
                    return -1;
            }
        });

        return userWaitingListDtoList;
    }

    //host 입장에서 WaitingList 조회(status가 wait인 것들만)
    @GetMapping("/api/main/getGroupWaitingList/{groupId}")
    public HostWaitingListDto getWaitingList(@PathVariable Long groupId) {
        WaitingList waitingList = groupService.findOne(groupId).getWaitingList();

        return new HostWaitingListDto(waitingList);
    }

    //속해 있는 그룹 리스트 조회
    @GetMapping("/api/main/getGroupList/{userId}")
    public List<GroupSearchDto> getGroupList(@PathVariable Long userId) {

        List<GroupSearchDto> groupSearchDtoList = groupService.getGroupListWithRole(userId);

        //최신순 정렬
        groupSearchDtoList.sort(new Comparator<GroupSearchDto>() {
            @Override
            public int compare(GroupSearchDto o1, GroupSearchDto o2) {
                if(o1.getId() == o2.getId())
                    return 0;
                else if(o2.getId() > o1.getId())
                    return 1;
                else
                    return -1;
            }
        });

        return groupSearchDtoList;
    }

    //호스트 최근동향
    @GetMapping("/api/main/getHostRecentTrends/{userId}")
    public RecentTrendDto getHostRecent(@PathVariable Long userId){
        return new RecentTrendDto(GroupRole.HOST, 30, 50, 40);
    }
    //게스트 최근동향
    @GetMapping("/api/main/getGuestRecentTrends/{userId}")
    public RecentTrendDto getGuestRecent(@PathVariable Long userId){
        return new RecentTrendDto(GroupRole.GUEST, 70, 50, 30);
    }

    @Data
    static class HostWaitingListDto {

        private int count;
        private String groupName;
        private List<QueueDto> queueList;

        public HostWaitingListDto(WaitingList waitingList) {
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
