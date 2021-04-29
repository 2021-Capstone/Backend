package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.QueueRepository;
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
    private final UserService userService;
    private final WaitingListService waitingListService;

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
    public Group findOne(Long groupId){
        return groupRepository.findOne(groupId);
    }

    //유저가 속해 있는 그룹 전부 찾기
    public List<Group> groupByUser(Long userId) {
        List<Queue> queueByUserList = queueRepository.findByUser(userId);

        List<Group> groupList = new ArrayList<>();

        for (Queue queue : queueByUserList) {
            if (queue.getStatus() == WaitingStatus.ACCEPT)
                groupList.add(queue.getWaitingList().getGroup());
        }

        return groupList;
    }

    //그룹 삭제
    @Transactional
    public void deleteGroup(Long groupId){
        groupRepository.delete(groupRepository.findOne(groupId));
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
    public Long joinGroup(User user, String groupEnterCode){
        if(groupRepository.findByCode(groupEnterCode).isEmpty()){
            throw new IllegalStateException("해당 코드는 유효하지 않습니다.");
        }
        else{
            Group findGroup = groupRepository.findByCode(groupEnterCode).get(0);
            Queue queue = new Queue();
            queueRepository.init(queue, user, findGroup.getWaitingList());
            queueRepository.save(queue);

            return findGroup.getId();
        }
    }

}

