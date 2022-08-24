package com.fake.smtp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.fake.smtp.*")
@EnableJpaRepositories("com.fake.smtp.repository")
@EntityScan("com.fake.smtp.model")
public class SmtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmtpApplication.class, args);
    }

}