package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /**
     * 회원 가입
     */
    @Transactional
    public Long join(User user, String passwordConfirm){

        validateDuplicateUser(user);
        validatePassword(user, passwordConfirm);
        userRepository.save(user);
        return user.getId();
    }

    private void validatePassword(User user, String passwordConfirm) {

        if(!user.getPassword().equals(passwordConfirm)){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다. ");
        }
    }

    /**
     * 중복 이메일 체크
     */
    private void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findByEmail(user.getEmail());
        if(!findUsers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다. ");
        }
    }

    private void validateDuplicateUser(String newEmail) {   // 이메일 수정 시 새로운 이메일 값 중복 체크
        List<User> findUsers = userRepository.findByEmail(newEmail);
        if(!findUsers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 이메일입니다. 수정 실패 ");
        }
    }


    /**
     * 특정 회원 조회
     */
    @Transactional(readOnly = true)
    public User findOne(Long userId){
        return userRepository.findOne(userId);
    }


    /**
     * 회원 정보 수정
     */
    @Transactional //회원 정보 폼에서 데이터 가져와서 수정(변경 감지)
    public void update(Long id, String email, String name){

        User user = userRepository.findOne(id);

        validateDuplicateUser(email);  // 변경하려는 이메일의 중복체크

        user.setEmail(email);
        user.setName(name);

    }
}