package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import com.hawkeye.capstone.service.WaitingListService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    //회원가입
    @PostMapping("/api/auth/register")
    public CreateUserResponse saveMember(@RequestBody @Valid CreateUserRequest request){

        User user = new User();
        user.setEmail(request.email);
        user.setName(request.name);
        user.setPassword(request.password);

        Long id = userService.join(user, request.passwordConfirm);
        return new CreateUserResponse(id);
    }

    //로그인
    @PostMapping("/api/auth/login")
    public LogInResponse logIn(@RequestBody @Valid LogInRequest request){
        return new LogInResponse(userService.loadUserByEmail(request.getEmail(), request.getPassword()).getId());
    }

    //회원 조회
    @GetMapping("/api/mypage/{userId}")
    public UserDto userSearch(@PathVariable("userId") Long userId){

        User findUser = userService.findOne(userId);
        //User를 UserDto로 변환
        UserDto userDto = new UserDto(findUser.getEmail(), findUser.getName(), findUser.getPassword());
        return userDto;
    }

    //회원 정보 수정
    @PatchMapping("/api/mypage/{userId}")
    public UpdateUserResponse updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UpdateUserRequest request){

        userService.update(userId, request.getEmail(), request.getName());
        User findUser = userService.findOne(userId);
        return new UpdateUserResponse(findUser.getId(), findUser.getEmail());
    }

    @Data
    @AllArgsConstructor
    static class LogInResponse{
        private Long id;
    }

    @Data
    static class LogInRequest{
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class UpdateUserResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateUserRequest {
        private String name;
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class UserDto{

        private String email;
        private String name;
        private String password;

    }

    @Data
    static class CreateUserResponse {
        private Long id;

        public CreateUserResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateUserRequest {

        @NotEmpty
        private String email;

        @NotEmpty
        private String name;

        @NotEmpty
        private String password;

        @NotEmpty
        private String passwordConfirm;
    }
}
