package com.fake.smtp.server.impl;

import com.fake.smtp.server.SmtpServer;
import com.fake.smtp.server.SmtpServerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static org.mockito.Mockito.mock;

@Profile("integrationtest")
@Service
public class MockSmtpServerFactory implements SmtpServerFactory {
    @Override
    public SmtpServer create() {
        return mock(SmtpServer.class);
    }
}
