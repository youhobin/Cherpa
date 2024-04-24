package com.cerpha.cerphaproject.cerpha.user.repository;

import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
