package com.fake.smtp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fakesmtp")
public class FakeSmtpConfigurationProperties {

    private static final int DEFAULT_PORT = 25;

    @NotNull
    private Integer port = DEFAULT_PORT;
    private InetAddress bindAddress;
    private Authentication authentication;
    private String filteredEmailRegexList;
    private boolean forwardEmails = false;

    @NotNull
    private Persistence persistence = new Persistence();

    @Getter
    @Setter
    public static class Authentication {
        @NotNull
        private String username;
        @NotNull
        private String password;
    }

    @Getter
    @Setter
    public static class Persistence {
        static final int DEFAULT_MAX_NUMBER_EMAILS = 100;

        @NotNull
        private Integer maxNumberEmails = DEFAULT_MAX_NUMBER_EMAILS;
    }
}
