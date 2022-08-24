package com.fake.smtp.server.impl;

import com.fake.smtp.server.SmtpServer;
import org.subethamail.smtp.server.SMTPServer;

public class SmtpServerImpl implements SmtpServer {

    final SMTPServer smtpServer;

    SmtpServerImpl(SMTPServer smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public void start() {
        smtpServer.start();
    }

    @Override
    public void stop() {
        smtpServer.stop();
    }
}
