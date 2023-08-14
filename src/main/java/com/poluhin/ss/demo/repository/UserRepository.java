package com.poluhin.ss.demo.repository;

import com.poluhin.ss.demo.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRep")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
