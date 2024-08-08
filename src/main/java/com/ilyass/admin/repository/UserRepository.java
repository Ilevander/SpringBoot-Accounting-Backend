package com.ilyass.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ilyass.admin.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserByEmail(String email);
}
