package com.cerpha.cerphaproject.common.config;


import com.cerpha.cerphaproject.common.converter.EncryptionConverter;
import com.cerpha.cerphaproject.common.encryption.AESEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AESEncryption aesEncryption() {
        return new AESEncryption();
    }

}
