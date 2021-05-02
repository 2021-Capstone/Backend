package com.hawkeye.capstone;

import com.hawkeye.capstone.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

}



/*
*   INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
	INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');
	실행 후 DB에서 위 명령어 먼저 해주기
* */