package com.cerpha.cerphaproject.cerpha.user.domain;

import com.cerpha.cerphaproject.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(String email, String password, String name, String nickname, String phone, String address, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
}
