package com.cerpha.cerphaproject.cerpha.user.domain;

import com.cerpha.cerphaproject.cerpha.BaseTimeEntity;
import com.cerpha.cerphaproject.cerpha.user.request.UpdateProfileRequest;
import com.cerpha.cerphaproject.common.converter.EncryptionConverter;
import com.cerpha.cerphaproject.common.encryption.AESEncryption;
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
    private String email;

    private String password;

    @Convert(converter = EncryptionConverter.class)
    private String name;

    @Convert(converter = EncryptionConverter.class)
    private String phone;

    @Convert(converter = EncryptionConverter.class)
    private String address;

    private String nickname;

    @Enumerated(EnumType.STRING)
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

    public void encryptPrivacy(AESEncryption aesEncryption) throws Exception {
        this.email = aesEncryption.encrypt(email);
        this.name = aesEncryption.encrypt(name);
        this.phone = aesEncryption.encrypt(phone);
        this.address = aesEncryption.encrypt(address);
    }

    public void updateProfile(UpdateProfileRequest request) {
        this.address = request.getAddress();
        this.phone = request.getPhone();
    }

}
