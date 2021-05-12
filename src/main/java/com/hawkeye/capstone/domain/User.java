package com.hawkeye.capstone.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    @Column(name = "user_password")
    private String password;

    //이미지 저장 byte[]로 매핑하면 DB에서는 BLOB 저장(?)
//    @Column(name = "profile_image", length = 100000)
//    private byte[] image;

    @Column(name = "user_name")
    private String name;

    @Column(name = "image_directory")
    private String imageDir;

}
