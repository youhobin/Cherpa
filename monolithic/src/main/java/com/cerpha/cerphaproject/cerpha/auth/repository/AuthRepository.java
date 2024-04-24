package com.cerpha.cerphaproject.cerpha.auth.repository;

import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(@Param("email") String email);

}
