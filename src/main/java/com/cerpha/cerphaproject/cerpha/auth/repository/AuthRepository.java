package com.cerpha.cerphaproject.cerpha.auth.repository;

import com.cerpha.cerphaproject.cerpha.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {

}
