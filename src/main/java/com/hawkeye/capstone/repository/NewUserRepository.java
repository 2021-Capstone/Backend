package com.hawkeye.capstone.repository;

import com.hawkeye.capstone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
