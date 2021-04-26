package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class GroupApiController {

    private final GroupService groupService;
    private final UserService userService;

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
        user.setEmail("alahoon@naver.com");
        user.setPassword("123");
        userService.join(user, "123");

        Long id = groupService.createGroup(group, userService.findOne(request.getHostId()));
        return new CreateGroupResponse(id);
    }

    //그룹 삭제
    @DeleteMapping("/api/group/deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    //그룹 정보 확인
    @GetMapping("/api/group/getGroupInfo/{groupId}")
    public GroupDto getGroupInfo(@PathVariable("groupId") Long groupId) {
        Group findGroup = groupService.findOne(groupId);
        GroupDto groupDto = new GroupDto(findGroup.getName(), findGroup.getCode(), findGroup.getAbsenceTime(), findGroup.getAlertDuration(), findGroup.isOnAir(), findGroup.getHostId());
        return groupDto;
    }

    //그룹 정보 수정
    @PatchMapping("/api/group/getGroupInfo/{groupId}")
    public UpdateGroupInfoResponse updateGroupInfo(@PathVariable("groupId") Long groupId, @RequestBody UpdateGroupInfoRequest request) {
        groupService.update(groupId, request.getName(), request.getAbsenceTime(), request.getAlertDuration());
        Group findGroup = groupService.findOne(groupId);
        return new UpdateGroupInfoResponse(findGroup.getId(), findGroup.getName(), findGroup.getAbsenceTime(), findGroup.getAlertDuration());
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
