package com.hawkeye.capstone.controller;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.UserDto;
import com.hawkeye.capstone.dto.UserDto2;
import com.hawkeye.capstone.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

}
