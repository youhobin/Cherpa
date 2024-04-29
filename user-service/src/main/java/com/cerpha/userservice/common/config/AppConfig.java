package com.cerpha.userservice.common.config;


import com.cerpha.userservice.common.encryption.AESEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AESEncryption aesEncryption() {
        return new AESEncryption();
    }

}
