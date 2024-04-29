package com.cerpha.userservice.cerpha.user.repository;

import com.cerpha.userservice.cerpha.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
