package com.fake.smtp.server.impl;

import com.fake.smtp.TestResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.internet.MimeMessage;

class RawDataTest {

    @Test
    void shouldReturnMimeMessage() throws Exception {
        RawData sut = new RawData("from", "to", TestResourceUtil.getTestFileContentBytes(("mail-with-subject.eml")));

        MimeMessage message = sut.toMimeMessage();
        Assertions.assertEquals("This is the mail title", message.getSubject());
    }

}