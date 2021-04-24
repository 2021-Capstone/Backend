package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupApiController {

    private final GroupService groupService;
    private final UserService userService;

    //그룹 생성
    @PostMapping("/api/group/createGroup")
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest request){
        //그룹 생성
        Group group = new Group();
        group.setName(request.getGroupName());
        group.setAbsenceTime(request.getAbsenceTime());
        group.setAlertDuration(request.getAlertDuration());

        //임시 호스트 생성
        User user = new User();
        user.setEmail("alahoon@naver.com");
        user.setPassword("123");
        userService.join(user, "123");

        Long id = groupService.createGroup(group, user);
//        Long id = groupService.createGroup(group, request.getUser());
        return new CreateGroupResponse(id);
    }

    //그룹 삭제
    @DeleteMapping("/api/group/deleteGroup/{groupId}")
    public void deleteGroup (@PathVariable("groupId") Long groupId){
        groupService.deleteGroup(groupId);
    }

    @Data
    @AllArgsConstructor
    static class CreateGroupResponse{
        private Long id;
    }

    @Data
    static class CreateGroupRequest{
        private String groupName;
        //자리비움 시간
        private int absenceTime;
        //알림 시간
        private int alertDuration;
        //호스트
//        private User user;
    }

}
