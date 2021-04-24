package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.Queue;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.domain.WaitingStatus;
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

    //그룹 생성
    @Transactional
    public Long createGroup(Group group, User user) {
        group.setHostId(user.getId());
        group.setCode(createEnterCode(user.getId()));
        groupRepository.save(group);
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

    //유저가 속해 있는 그룹 전부 찾기
    public List<Group> groupByUser(Long userId) {
        List<Queue> queueByUserList = queueRepository.findByUser(userId);

        List<Group> groupList = new ArrayList<>();

        for (Queue queue : queueByUserList) {
            if (queue.getWaitingList().getStatus() == WaitingStatus.ACCEPT)
                groupList.add(queue.getWaitingList().getGroup());
        }

        return groupList;
    }

    //그룹 삭제
    @Transactional
    public void deleteGroup(Long groupId){
        groupRepository.delete(groupRepository.findOne(groupId));
    }
}
