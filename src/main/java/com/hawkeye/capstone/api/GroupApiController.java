package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.*;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupApiController {

    private final GroupService groupService;
    private final UserService userService;
    private final QueueRepository queueRepository;
    private final QueueService queueService;
    private final WaitingListService waitingListService;
    private final SessionService sessionService;

    //그룹 생성
    @PostMapping("/api/group/createGroup")
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest request) {
        //그룹 생성
        Group group = new Group();
        group.setName(request.getName());
        group.setAbsenceTime(request.getAbsenceTime());
        group.setAlertDuration(request.getAlertDuration());

        Long id = groupService.createGroup(group, userService.findOne(request.getUserId()));
        return new CreateGroupResponse(id, group.getCode());
    }

    //그룹 삭제
    @DeleteMapping("/api/group/deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    //그룹 입장 신청
    @PostMapping("/api/group/joinGroup/{userId}")
    public JoinGroupResponse joinGroup(@Valid @PathVariable("userId") Long userId, @RequestBody JoinGroupRequest request) {
        Long groupId = groupService.joinGroup(userService.findOne(userId), request.getGroupEnterCode());
        return new JoinGroupResponse(groupId);
    }

    //그룹 입장 수락
    @PostMapping("/api/group/allowMember/{groupId}")
    public AllowMemberResponse allowMember(@PathVariable("groupId") Long groupId, @RequestBody AllowMemberRequest request) {

        queueService.allowMember(groupId, request.getEmail());

        return new AllowMemberResponse(groupId);
    }

    //그룹 입장 거절
    @PostMapping("/api/group/rejectMember/{groupId}")
    public RejectMemberResponse rejectMember(@PathVariable("groupId") Long groupId, @RequestBody RejectMemberRequest request) {

        queueService.rejectMember(groupId, request.getEmail());

        return new RejectMemberResponse(groupId);
    }

    //그룹 대기열 조회
    @GetMapping("/api/group/getWaitingList/{groupId}")
    public List<WaitingMemberDto> getGroupWaitingList(@PathVariable("groupId") Long groupId){

        return groupService.getGroupWaitingList(groupId);
    }

    //그룹 탈퇴
    @PostMapping("/api/group/exitGroup/{groupId}")
    public ExitGroupResponse exitGroup(@PathVariable("groupId") Long groupId, @RequestBody ExitGroupRequest request) {

        groupService.exitGroup(groupId, request.getUserId());

        return new ExitGroupResponse(request.getUserId());
    }

    //유저가 속한 그룹 정보 확인
    @PostMapping("/api/group/getGroupInfo")
    public GroupDetailDto getGroupInfo(@RequestBody GroupDetailRequest groupDetailRequest) {

        return groupService.searchGroup(groupDetailRequest.groupId, groupDetailRequest.userId);

    }

    //그룹 정보 수정
    @PostMapping("/api/group/editGroupInfo/{groupId}")
    public GroupDetailDto updateGroupInfo(@PathVariable("groupId") Long groupId, @RequestBody UpdateGroupInfoRequest request) {

        groupService.updateGroup(groupId, request.getName(), request.getAbsenceTime(), request.getAlertDuration());

        return groupService.searchGroup(groupId, groupService.findOne(groupId).getHostId());
    }

    //그룹의 유저 조회
    @GetMapping("/api/group/getGroupMember/{groupId}")
    public GroupMemberDto getGroupMember(@PathVariable("groupId") Long groupId) {

        return groupService.getGroupMember(groupId);
    }

    //수업 시작
    @PostMapping("/api/group/startSession/{groupId}")
    public SessionDto startSession(@PathVariable("groupId") Long groupId) {
        Long sessionId = sessionService.createSession(groupId);

        return new SessionDto(sessionId);
    }

    //수업 종료
    @PostMapping("/api/group/endSession/{sessionId}")
    public SessionDto endSession(@PathVariable("sessionId") Long sessionId) {

        return new SessionDto(sessionService.endSession(sessionId));

    }

    @Data
    static class GroupDetailRequest{
        private Long groupId;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class ExitGroupResponse {
        private Long id;
    }

    @Data
    static class ExitGroupRequest {
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class SessionDto {
        private Long id;
    }

    @Data
    @AllArgsConstructor
    static class RejectMemberResponse {
        private Long id;
    }

    @Data
    static class RejectMemberRequest {
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class AllowMemberResponse {
        private Long id;
    }

    @Data
    static class AllowMemberRequest {
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class JoinGroupResponse {
        private Long id;
    }

    @Data
    static class JoinGroupRequest {
        private String groupEnterCode;
    }

    @Data
    @AllArgsConstructor
    static class UpdateGroupInfoResponse {
        private Long id;
        private String name;
        private int absenceTime;
        private int alertDuration;
    }

    @Data
    static class UpdateGroupInfoRequest {
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
        private long userId;
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
