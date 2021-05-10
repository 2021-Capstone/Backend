package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.Authority;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.UserDto2;
import com.hawkeye.capstone.repository.NewUserRepository;
import com.hawkeye.capstone.repository.UserRepository;
import com.hawkeye.capstone.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NewUserRepository newUserRepository;
    private final PasswordEncoder passwordEncoder;

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

    // 토큰 테스트 - 회원가입
    @Transactional


    private void validatePassword(User user, String passwordConfirm) {

        if(!user.getPassword().equals(passwordConfirm)){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다. ");
        }
    }

    /**
     * 중복 이메일 체크
     */
    private void validateDuplicateUser(User user) {
        List<User> findUserList = userRepository.findByEmail(user.getEmail());
        if(!findUserList.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        }
    }

    /**
     * 회원 정보 수정
     */
    @Transactional //회원 정보 폼에서 데이터 가져와서 수정(변경 감지)
    public void update(Long id, String email, String name, String imageDir){
        User user = userRepository.findOne(id);
        user.setEmail(email);
        user.setName(name);
        //user.setImage(image.getBytes(StandardCharsets.UTF_8));
        user.setImageDir(imageDir);
    }

    /**
     * 회원 조회
     */
    public User findOne(Long userId){
        return userRepository.findOne(userId);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).get(0);
    }

    /**
     * 이미지 경로 저장
     */
    @Transactional
    public void setImageDir(User user, String directory){
        user.setImageDir(directory);
    }


    /**
     * 로그인
     */
    public User loadUserByEmail(String email, String password){
        User findUser = userRepository.findByEmail(email).get(0);
        if(findUser == null)
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        String encryptedPassword = userRepository.Encrypt(password);
        if(findUser.getPassword().equals(encryptedPassword))
            return findUser;
        else{
            System.out.println("encryptedPassword = " + encryptedPassword);
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }


    // Jwt Test - 회원 가입
    @Transactional
    public User signup(UserDto2 userDto2) {
        if (newUserRepository.findOneWithAuthoritiesByEmail(userDto2.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .email(userDto2.getEmail())
                .password(passwordEncoder.encode(userDto2.getPassword()))
                .name(userDto2.getName())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return newUserRepository.save(user);
    }

    // Jwt Test- 현재 로그인한 회원이 자신의 정보 가져오기
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(newUserRepository::findOneWithAuthoritiesByEmail);
    }

    // Jwt Test- 현재 로그인한 회원이 자신의 정보 수정하기
    @Transactional
    public User updateInfo(User user, String email, String name){
        user.setEmail(email);
        //user.setPassword(passwordEncoder.encode(password)); // encode = 비밀번호를 단방향 암호화
        user.setName(name);

        return user;
    }
}
