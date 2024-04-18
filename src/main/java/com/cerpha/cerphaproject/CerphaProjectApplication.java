package com.cerpha.cerphaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CerphaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CerphaProjectApplication.class, args);
    }

}
