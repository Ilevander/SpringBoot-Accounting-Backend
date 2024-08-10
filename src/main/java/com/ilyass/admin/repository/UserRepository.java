package com.ilyass.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilyass.admin.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
