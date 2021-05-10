package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.UserDto;
import com.hawkeye.capstone.service.FileService;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final FileService fileService;

    //회원가입
//    @PostMapping("/api/auth/register")
//    public CreateUserResponse saveMember(@RequestBody @Valid CreateUserRequest request, @RequestParam("file") MultipartFile file){
//
//        User user = new User();
//        user.setEmail(request.email);
//        user.setName(request.name);
//        user.setPassword(request.password);
//        user.setImageDir(fileService.fileUpload(file));
//        //user.setImage(request.image.getBytes(StandardCharsets.UTF_8));
//        //System.out.println("request = " + request.image.getBytes(StandardCharsets.UTF_8));
//
//        Long id = userService.join(user, request.passwordConfirm);
//        return new CreateUserResponse(id);
//    }

    //회원가입
    @PostMapping("/api/auth/register")
    public CreateUserResponse registerMember(@RequestParam("email") String email, @RequestParam("name") String name,
                                             @RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm,
                                             @RequestParam("file") MultipartFile file) {

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setImageDir(fileService.fileUpload(file));

        Long id = userService.join(user, passwordConfirm);
        return new CreateUserResponse(id);
    }

    //로그인
    @PostMapping("/api/auth/login")
    public LogInResponse logIn(@RequestBody @Valid LogInRequest request) {
        return new LogInResponse(userService.loadUserByEmail(request.getEmail(), request.getPassword()).getId());
    }

    //회원 조회
    @GetMapping("/api/mypage/{userId}")
    public UserDto userSearch(@PathVariable("userId") Long userId) {

        User findUser = userService.findOne(userId);
        //User를 UserDto로 변환
        UserDto userDto = new UserDto(findUser.getEmail(), findUser.getName(), findUser.getImageDir());
        return userDto;
    }

    //회원 정보 수정
    @PatchMapping("/api/mypage/{userId}")
    public UpdateUserResponse updateUser(@PathVariable("userId") Long userId, @RequestParam("email") String email,
                                         @RequestParam("name") String name, @RequestParam("file") MultipartFile file) {

        userService.update(userId, email, name, fileService.fileUpload(file));
        User findUser = userService.findOne(userId);
        return new UpdateUserResponse(findUser.getId(), findUser.getEmail());
    }

    //회원 정보 수정
//    @PatchMapping("/api/mypage/{userId}")
//    public UpdateUserResponse updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UpdateUserRequest request){
//
//        userService.update(userId, request.getEmail(), request.getName(), request.getImageDir());
//        User findUser = userService.findOne(userId);
//        return new UpdateUserResponse(findUser.getId(), findUser.getEmail());
//    }

    //이미지 저장
//    @PostMapping("/api/image/upload/{userId}")
//    public Long imageUpload(@PathVariable("userId")Long userId, @RequestParam("file") MultipartFile file){
//        String directory = fileService.fileUpload(file);
//        tempDir = directory;
//        userService.setImageDir(userId, directory);
//
//        return userId;
//    }

    //이미지 경로 불러오기
    @GetMapping("/api/image/getImage/{userId}")
    public String getImage(@PathVariable("userId") Long userId) {
        User findUser = userService.findOne(userId);
        return findUser.getImageDir();
    }

    @Data
    @AllArgsConstructor
    static class LogInResponse {
        private Long id;
    }

    @Data
    static class LogInRequest {
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
        private String imageDir;
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
