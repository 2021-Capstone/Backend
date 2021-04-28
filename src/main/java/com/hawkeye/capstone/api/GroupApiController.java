package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.domain.WaitingStatus;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupApiController {

    private final GroupService groupService;
    private final UserService userService;
    private final QueueRepository queueRepository;

    //그룹 생성
    @PostMapping("/api/group/createGroup")
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest request) {
        //그룹 생성
        Group group = new Group();
        group.setName(request.getName());
        group.setAbsenceTime(request.getAbsenceTime());
        group.setAlertDuration(request.getAlertDuration());

        //임시 호스트 생성
        User user = new User();
        user.setName("김지훈");
        user.setEmail("alahoon@naver.com");
        user.setPassword("123");
        userService.join(user, "123");

        Long id = groupService.createGroup(group, userService.findOne(request.getHostId()));
        return new CreateGroupResponse(id, group.getCode());
    }

    //그룹 삭제
    @DeleteMapping("/api/group/deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    //그룹 입장 신청
    @PostMapping("/api/group/joinGroup/{userId}")
    public JoinGroupResponse joinGroup(@PathVariable("userId") Long userId, @RequestBody JoinGroupRequest request){
        Long groupId = groupService.joinGroup(userService.findOne(userId), request.getGroupEnterCode());
        return new JoinGroupResponse(groupId);
    }

    //그룹 입장 수락
    @PatchMapping("/api/group/allowMember/{groupId}")
    public AllowMemberResponse allowMember(@PathVariable("groupId") Long groupId, @RequestBody AllowMemberRequest request){

        User findUser = userService.findByEmail(request.getEmail());
        //해당 유저가 속한 Queue 전부 조회
        List<Queue> queueList = queueRepository.findByUser(findUser.getId());
        //각 Queue가 입장 신청을 한 그룹의 waitingList인지 조회
        for (Queue queue : queueList) {
            if(queue.getWaitingList().getGroup().getId() == groupId){
                //변경 감지
                queueRepository.setStatus(queue, WaitingStatus.ACCEPT);
                //해당 Queue는 WaitingList에서 빠짐
            }
        }

        return new AllowMemberResponse(groupId);
    }

    //그룹 정보 확인
    @GetMapping("/api/group/getGroupInfo/{groupId}")
    public GroupDto getGroupInfo(@PathVariable("groupId") Long groupId) {
        Group findGroup = groupService.findOne(groupId);
        GroupDto groupDto = new GroupDto(findGroup.getName(), findGroup.getCode(), findGroup.getAbsenceTime(), findGroup.getAlertDuration(), findGroup.isOnAir(), findGroup.getHostId());
        return groupDto;
    }

    //그룹 정보 수정
    @PatchMapping("/api/group/editGroupInfo/{groupId}")
    public UpdateGroupInfoResponse updateGroupInfo(@PathVariable("groupId") Long groupId, @RequestBody UpdateGroupInfoRequest request) {
        groupService.updateGroup(groupId, request.getName(), request.getAbsenceTime(), request.getAlertDuration());
        Group findGroup = groupService.findOne(groupId);
        return new UpdateGroupInfoResponse(findGroup.getId(), findGroup.getName(), findGroup.getAbsenceTime(), findGroup.getAlertDuration());
    }

    //그룹의 유저 조회
    @Data
    @AllArgsConstructor
    static class AllowMemberResponse{
        private Long id;
    }

    @Data
    static class AllowMemberRequest{
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class JoinGroupResponse{
        private Long id;
    }

    @Data
    static class JoinGroupRequest{
        private String groupEnterCode;
    }

    @Data
    @AllArgsConstructor
    static class UpdateGroupInfoResponse{
        private Long id;
        private String name;
        private int absenceTime;
        private int alertDuration;
    }

    @Data
    static class UpdateGroupInfoRequest{
        private String name;
        private int absenceTime;
        private int alertDuration;
    }

    @Data
    @AllArgsConstructor
    static class CreateGroupResponse {
        private Long id;
        private String groupEnterCode;
    }

    @Data
    static class CreateGroupRequest {
        @NotEmpty
        private String name;
        //자리비움 시간
        @NotEmpty
        private int absenceTime;
        //알림 시간
        @NotEmpty
        private int alertDuration;
        //호스트
        @NotEmpty
        private long hostId;
    }

    @Data
    @AllArgsConstructor
    static class GroupDto {
        private String name;
        private String code;
        private int absenceTime;
        private int alertDuration;
        private boolean onAir;
        private Long hostId;
    }

}
