package com.hawkeye.capstone.controller;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.UserDto;
import com.hawkeye.capstone.dto.UserDto2;
import com.hawkeye.capstone.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// AuthController에 있는  authenticate/ api 통해서 로그인 성공하면  토큰 발급받는다
// 로그인 상태에서 다른 요청 보낼 시 Post man에서 Authorization 클릭 후  Type은 Bearer Token을 선택하고
// 발급받은 토큰 값을 입력해주고 요청을 보내면 된다


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")  // api 테스트
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/signup")  //회원 가입
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto2 userDto2
    ) {
        return ResponseEntity.ok(userService.signup(userDto2));
    }

    // 현재 로그인한 유저 정보 조회
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    // 현재 로그인한 유저 정보 수정 test
    @PatchMapping("/mypage2")
    public ResponseEntity<User> updateUserInfo(@Valid @RequestBody UserDto2 userDto2){
        User user = userService.getMyUserWithAuthorities().get();

        return ResponseEntity.ok(userService.updateInfo(user, userDto2.getEmail(),
                userDto2.getPassword(), userDto2.getName()));
    }


}
