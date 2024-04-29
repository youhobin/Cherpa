package com.cerpha.userservice.cerpha.user.domain;

import com.cerpha.userservice.cerpha.BaseTimeEntity;
import com.cerpha.userservice.common.converter.EncryptionConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Convert(converter = EncryptionConverter.class)
    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Convert(converter = EncryptionConverter.class)
    @Column(name = "user_name")
    private String name;

    @Convert(converter = EncryptionConverter.class)
    @Column(name = "user_phone")
    private String phone;

    @Convert(converter = EncryptionConverter.class)
    @Column(name = "user_address")
    private String address;

    @Column(name = "user_nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole role;

    @Builder
    public Users(String email, String password, String name, String nickname, String phone, String address, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public void updateProfile(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
