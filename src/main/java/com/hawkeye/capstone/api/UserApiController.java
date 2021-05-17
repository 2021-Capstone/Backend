package com.hawkeye.capstone.api;

import com.hawkeye.capstone.domain.Group;
import com.hawkeye.capstone.domain.User;
import com.hawkeye.capstone.dto.GroupSearchDto;
import com.hawkeye.capstone.dto.UserDto;
import com.hawkeye.capstone.dto.UserSearchDto;
import com.hawkeye.capstone.jwt.JwtTokenProvider;
import com.hawkeye.capstone.repository.GroupRepository;
import com.hawkeye.capstone.repository.UserRepository;
import com.hawkeye.capstone.service.FileService;
import com.hawkeye.capstone.service.GroupService;
import com.hawkeye.capstone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final FileService fileService;
    private final GroupService groupService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //회원가입
//    @PostMapping("/api/auth/register")
//    public CreateUserResponse registerMember(@RequestParam("email") String email, @RequestParam("name") String name,
//                                             @RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm,
//                                             @RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
//                                             @RequestParam("file3") MultipartFile file3){
//
//        UserDto userDto = new UserDto(email, name, fileService.fileUpload(file1),
//                fileService.fileUpload(file2), fileService.fileUpload(file3), password);
//
//        Long id = userService.join(userDto, passwordConfirm);
//
//        return new CreateUserResponse(id);
//    }

    //CORS 테스트
    @Transactional
    @PostMapping("/api/auth/register")
    public void registerMember(@RequestParam("email") String email, @RequestParam("name") String name,
                                             @RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm){

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        userRepository.save(user);
    }

    //로그인 토큰
    @PostMapping("/api/auth/login")
    public LogInResponse logIn(@RequestBody @Valid LogInRequest request) {

        User findUser = userService.findByEmail(request.getEmail());

        if(userService.loadUserByEmail(request.getEmail(), request.getPassword())){
            //토큰 생성
            String token = jwtTokenProvider.createToken(findUser.getUsername(), findUser.getRoles());

            List<GroupSearchDto> groupSearchDtoList = groupService.getGroupListWithRole(findUser.getId());

            return new LogInResponse(findUser.getName(), findUser.getImageDir(), token, findUser.getId());
        }

        else{
            throw new IllegalStateException("로그인에 실패했습니다.");
        }

    }

    //회원 조회
    @GetMapping("/api/mypage/{userId}")
    public UserSearchDto userSearch(@PathVariable("userId") Long userId) {

        User findUser = userService.findOne(userId);
        //User를 UserSearchDto로 변환
        UserSearchDto userSearchDto = new UserSearchDto(findUser.getEmail(), findUser.getName(), findUser.getImageDir());
        return userSearchDto;
    }

    //회원 정보 수정
    @PatchMapping("/api/mypage/{userId}")
    public UpdateUserResponse updateUser(@PathVariable("userId") Long userId, @RequestParam("email") String email,
                                         @RequestParam("name") String name, @RequestParam("file1") MultipartFile file1,
                                         @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) {

        userService.update(userId, email, name, fileService.fileUpload(file1),
                fileService.fileUpload(file2), fileService.fileUpload(file3));
        User findUser = userService.findOne(userId);
        return new UpdateUserResponse(findUser.getId(), findUser.getEmail());
    }

    //프로필 이미지 경로 불러오기
    @GetMapping("/api/image/getImage/{userId}")
    public String getImage(@PathVariable("userId") Long userId) {
        User findUser = userService.findOne(userId);
        return findUser.getImageDir();
    }

    @Data
    @AllArgsConstructor
    static class LogInResponse {
        private String name;
        private String profileImage;
        private String token;
        private Long userId;
    }

    @Data
    @AllArgsConstructor
    static class GroupDto{
        private Long groupId;
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
}
