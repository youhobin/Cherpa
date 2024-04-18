package com.cerpha.cerphaproject.cerpha.user.domain;

import com.cerpha.cerphaproject.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
}
