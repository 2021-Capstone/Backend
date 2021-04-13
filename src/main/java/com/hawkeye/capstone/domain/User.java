package com.hawkeye.capstone.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    @Column(name = "user_password")
    private String password;

    //이미지 저장 byte[]로 매핑하면 DB에서는 BLOB 저장(?)
    @Column(name = "profile_image")
    private byte[] image;

    @Column(name = "user_name")
    private String name;

    @OneToMany(mappedBy = "user")
    private WaitingList waitingList;

}
