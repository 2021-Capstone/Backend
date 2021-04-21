package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.*;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.QueueRepository;
import com.hawkeye.capstone.repository.UserRepository;
import com.hawkeye.capstone.repository.WaitingListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class GroupServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    QueueRepository queueRepository;
    @Autowired
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    WaitingListRepository waitingListRepository;
    @Autowired
    UserService userService;

    @Test
    public void 회원별_그룹찾기() throws Exception {
        //given
        User user = new User();
        user.setName("kim");
        user.setPassword("123");

        WaitingList waitingList = new WaitingList();
        waitingList.setStatus(WaitingStatus.ACCEPT);

        Group group = new Group();
        group.setName("gg");
        waitingList.setGroup(group);

        Queue queue = new Queue();
        queue.setUser(user);
        queue.setWaitingList(waitingList);


        //when
        userService.join(user, "123");
        groupRepository.save(group);
        queueRepository.save(queue);
        waitingListRepository.save(waitingList);

        //then
        assertEquals(group, groupService.groupByUser(user.getId()).get(0));
    }

    @Test
    public void 입장코드_생성() throws Exception{
        //given
        User user = new User();
        user.setName("professor");
        user.setPassword("123");
        user.setEmail("alahoon@naver.com");

        //when
        userService.join(user, "123");
        Long group = groupService.createGroup(new Group(), user);

    }
}