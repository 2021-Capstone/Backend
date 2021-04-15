package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
