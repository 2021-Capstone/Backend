package com.hawkeye.capstone.service;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.UserDto;
import com.hawkeye.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public Long join(UserDto userDto, String passwordConfirm) {

        validateDuplicateUser(userDto.getEmail());
        validatePassword(userDto.getPassword(), passwordConfirm);

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .password(userDto.getPassword())
                .imageDir(userDto.getImageDir())
                .authorities(Collections.singleton(authority))
                .build();


        userRepository.save(user);
        return user.getId();
    }

    private void validatePassword(String password, String passwordConfirm) {

        if (!password.equals(passwordConfirm)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다. ");
        }
    }

    /**
     * 중복 이메일 체크
     */
    private void validateDuplicateUser(String email) {
        List<User> findUserList = userRepository.findByEmail(email);
        if (!findUserList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다. ");
        }
    }

    /**
     * 회원 정보 수정
     */
    @Transactional //회원 정보 폼에서 데이터 가져와서 수정(변경 감지)
    public void update(Long id, String email, String name, String imageDir) {
        User user = userRepository.findOne(id);
        user.setEmail(email);
        user.setName(name);
        //user.setImage(image.getBytes(StandardCharsets.UTF_8));
        user.setImageDir(imageDir);
    }

    /**
     * 회원 조회
     */
    public User findOne(Long userId) {
        return userRepository.findOne(userId);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get(0);
    }

    /**
     * 이미지 경로 저장
     */
    @Transactional
    public void setImageDir(User user, String directory) {
        user.setImageDir(directory);
    }

    /**
     * 로그인
     */
    public User loadUserByEmail(String email, String password) {
        User findUser = userRepository.findByEmail(email).get(0);
        if (findUser == null)
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        String encryptedPassword = userRepository.Encrypt(password);
        if (findUser.getPassword().equals(encryptedPassword))
            return findUser;
        else {
            System.out.println("encryptedPassword = " + encryptedPassword);
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

}
