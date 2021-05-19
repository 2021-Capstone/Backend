package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.dto.*;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final QueueRepository queueRepository;
    private final WaitingListRepository waitingListRepository;
    private final UserService userService;
    private final WaitingListService waitingListService;
    private final QueueService queueService;

    //그룹 생성
    @Transactional
    public Long createGroup(Group group, User user) {
        group.setHostId(user.getId());
        group.setCode(createEnterCode(user.getId()));
        groupRepository.save(group);

        WaitingList waitingList = new WaitingList();
        waitingListService.createWaitingList(waitingList, group);
        return group.getId();
    }

    //입장 코드 생성
    public String createEnterCode(Long hostID) {

        String timeToString = LocalDateTime.now().toString();

        User host = userService.findOne(hostID);
        String hostEmail = host.getEmail();

        String key = timeToString + hostEmail;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().substring(0, 8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //그룹 찾기
    public Group findOne(Long groupId) {
        return groupRepository.findOne(groupId);
    }

    //유저가 속해 있는 그룹 전부 찾기
    public List<Group> groupByUser(Long userId) {
        List<Queue> queueByUserList = queueRepository.findByUser(userId);

        List<Group> groupList = groupRepository.findByHost(userId);

        for (Queue queue : queueByUserList) {
            if (queue.getStatus() == WaitingStatus.ACCEPT)
                groupList.add(queue.getWaitingList().getGroup());
        }

        return groupList;
    }

    //그룹 삭제
    @Transactional
    public void deleteGroup(Long groupId) {
        Group findGroup = groupRepository.findOne(groupId);
        List<Queue> findQueueList = findGroup.getWaitingList().getQueueList();
        for (Queue queue : findQueueList) {
            queueRepository.delete(queue);
        }
        waitingListRepository.delete(findGroup.getWaitingList());
        groupRepository.delete(findGroup);
    }

    //그룹 웨이팅리스트 조회
    public List<WaitingMemberDto> getGroupWaitingList(Long groupId){
        Group findGroup = findOne(groupId);

        List<WaitingMemberDto> waitingMemberDtoList = new ArrayList<>();

        List<Queue> findQueueList = queueRepository.findByGroupWithUser(groupId);

        for (Queue queue : findQueueList) {
            if (queue.getStatus() == WaitingStatus.WAIT)
                waitingMemberDtoList.add(new WaitingMemberDto(queue.getUser().getName(), queue.getUser().getEmail()));
        }

        return waitingMemberDtoList;
    }

    //그룹 정보 조회
    public GroupDetailDto searchGroup(Long groupId, Long userId) {

        Group findGroup = findOne(groupId);

        //호출한 유저가 HOST
        if (findGroup.getHostId() == userId) {

            GroupMemberDto findGroupMembers = getGroupMember(groupId);

            List<GroupMemberSimpleDto> groupMemberSimpleDtoList = new ArrayList<>();
            List<WaitingMemberDto> waitingMemberDtoList = new ArrayList<>();

            List<UserSearchDto> findUserSearchDtoList = findGroupMembers.getUserSearchDtoList();
            List<Queue> findQueueList = queueRepository.findByGroupWithUser(groupId);

            for (Queue queue : findQueueList) {
                if (queue.getStatus() == WaitingStatus.WAIT)
                    waitingMemberDtoList.add(new WaitingMemberDto(queue.getUser().getName(), queue.getUser().getEmail()));
            }

            for (UserSearchDto userSearchDto : findUserSearchDtoList) {
                groupMemberSimpleDtoList.add(new GroupMemberSimpleDto(userSearchDto.getName(), userSearchDto.getEmail()));
            }


            return new GroupDetailDto(GroupRole.HOST, userService.findOne(findGroup.getHostId()).getName(), findGroup.getCode(), findGroup.getName(),
                    findGroup.getAbsenceTime(), findGroup.getAlertDuration(), groupMemberSimpleDtoList, waitingMemberDtoList);
        }

        //호출한 유저가 GUEST

        else {
            return new GroupDetailDto(GroupRole.GUEST, userService.findOne(findGroup.getHostId()).getName(), findGroup.getCode(), findGroup.getName(),
                    findGroup.getAbsenceTime(), findGroup.getAlertDuration());
        }
    }

    //그룹 멤버 조회
    public GroupMemberDto getGroupMember(Long groupId) {
        List<Queue> findQueueList = new ArrayList<>();

        //해당 그룹의 모든 Queue조회
        List<Queue> tempQueueList = queueRepository.findByGroupWithUser(groupId);
        for (Queue queue : tempQueueList) {
            //status가 ACCEPT인 큐만 골라내기
            if (queue.getStatus() == WaitingStatus.ACCEPT) {
                findQueueList.add(queue);
            }
        }
        GroupMemberDto groupMemberDto = new GroupMemberDto(findQueueList);
        return groupMemberDto;
    }

    //그룹 정보 변경
    @Transactional
    public void updateGroup(Long groupId, String groupName, int absenceTime, int alertDuration) {
        Group findGroup = groupRepository.findOne(groupId);
        findGroup.setName(groupName);
        findGroup.setAbsenceTime(absenceTime);
        findGroup.setAlertDuration(alertDuration);
    }

    //그룹 입장 신청
    @Transactional
    public Long joinGroup(User user, String groupEnterCode) {
        if (groupRepository.findByCode(groupEnterCode).isEmpty()) {
            throw new IllegalStateException("해당 코드는 유효하지 않습니다.");
        } else {
            Group findGroup = groupRepository.findByCode(groupEnterCode).get(0);
            queueService.init(user, findGroup.getWaitingList());

            return findGroup.getId();
        }
    }

    //속한 그룹 리스트 조회 + 해당 그룹 내의 역할 분배
    @Transactional
    public List<GroupSearchDto> getGroupListWithRole(Long userId) {
        List<Group> findGroupList = groupByUser(userId);
        List<GroupSearchDto> groupSearchDtoList = new ArrayList<>();
        for (Group group : findGroupList) {
            GroupRole tempRole;
            if (group.getHostId() == userId)
                tempRole = GroupRole.HOST;
            else
                tempRole = GroupRole.GUEST;
            groupSearchDtoList.add(new GroupSearchDto(group.getName(), group.getCode()
                    , group.isOnAir(), tempRole, group.getId()));
        }
        return groupSearchDtoList;
    }

}

