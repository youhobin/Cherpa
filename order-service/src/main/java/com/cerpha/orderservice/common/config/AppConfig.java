package com.cerpha.orderservice.common.config;


import com.cerpha.orderservice.common.encryption.AESEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AESEncryption aesEncryption() {
        return new AESEncryption();
    }

}
