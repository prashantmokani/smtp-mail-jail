package com.fake.smtp.server.impl;

import org.junit.jupiter.api.Test;
import org.subethamail.smtp.server.SMTPServer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SmtpServerImplTest {
    
    @Test
    void shouldCreateNewInstanceAndDelegateCallsToRealImplementation(){
        var delegate = mock(SMTPServer.class);

        var sut = new SmtpServerImpl(delegate);

        sut.start();
        verify(delegate).start();

        sut.stop();
        verify(delegate).stop();
    }

}