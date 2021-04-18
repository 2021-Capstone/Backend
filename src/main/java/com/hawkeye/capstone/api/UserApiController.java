package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    // 회원 정보 수정 - 마이페이지  /  ** 일단 email, 이름 수정 ** / 추후에  프로필이미지 추가해야 함
    @PutMapping("/api/mypage/revise/{id}")
    public UpdateUserResponse updateMember(@PathVariable("id") Long id,
                                           @RequestBody @Valid UpdateUserRequest request){

        userService.update(id, request.getEmail(), request.getName());

        User findUser = userService.findOne(id);

        return new UpdateUserResponse(findUser.getId(), findUser.getEmail(), findUser.getName());
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

    @Data
    static class UpdateUserRequest {  // 정보 수정 시

        @NotEmpty
        private String email;

        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateUserResponse {
        private Long id;
        private String email;
        private String name;
    }

    /**
     * 회원 정보 조회 - 마이페이지
     */
    @GetMapping("api/mypage/{id}")
    public UserDTO getUserInfo(@PathVariable("id")Long id){

        User findUser = userService.findOne(id);

        return new UserDTO(findUser.getEmail(), findUser.getName(), findUser.getPassword());
    }


    @Data
    @AllArgsConstructor
    static class UserDTO{  // user id값 통해서 이메일, 이름, 패스워드(?) 조회
        private String email;
        private String name;
        private String password;
    }
}
