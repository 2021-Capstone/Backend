package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;


    @Test
    public void 이름으로_회원찾기() throws Exception{
        //given
        User user = new User();
        user.setName("kim");
        //when
        userRepository.save(user);
        //then
        assertEquals(user.getName(), (userRepository.findByName("kim")).get(0).getName());
    }

    @Test
    @Rollback(value = false)
    public void 비밀번호_저장() throws Exception{
        //given
        User user = new User();
        user.setPassword("123");
        //when
        userRepository.save(user);
    }
}