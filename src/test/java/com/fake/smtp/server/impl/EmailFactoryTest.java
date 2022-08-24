package com.fake.smtp.server.impl;

import com.fake.smtp.TestResourceUtil;
import com.fake.smtp.model.ContentType;
import com.fake.smtp.model.Email;
import com.fake.smtp.model.EmailAttachment;
import com.fake.smtp.model.EmailContent;
import com.fake.smtp.util.TimestampProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailFactoryTest {

    private static final String SENDER = "sender";
    private static final String RECEIVER = "receiver";

    @Mock
    private TimestampProvider timestampProvider;

    @InjectMocks
    private EmailFactory sut;

    @Test
    void shouldCreateEmailForEmlFileWithSubjectAndContentTypePlain() throws Exception {
        var now = new Date();
        var testFilename = "mail-with-subject.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertPlainTextEmail(now, dataAsString, result);
    }

    private void assertPlainTextEmail(Date now, String dataAsString, Email result) {
        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(1));
        assertFalse(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertEquals("This is the message content", result.getPlainContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateEmailForEmlFileWithSubjectAndContentTypeHtml() throws Exception {
        var now = new Date();
        var testFilename = "mail-with-subject-and-content-type-html.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(1));
        assertFalse(result.getPlainContent().isPresent());
        assertTrue(result.getHtmlContent().isPresent());
        assertEquals("<html><head></head><body>Mail Body</body></html>", result.getHtmlContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateEmailForEmlFileWithSubjectAndWithoutContentType() throws Exception {
        var now = new Date();
        var testFilename = "mail-with-subject-without-content-type.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertPlainTextEmail(now, dataAsString, result);
    }

    @Test
    void shouldCreateEmailForEmlFileWithoutSubjectAndContentTypePlain() throws Exception {
        var now = new Date();
        var testFilename = "mail-without-subject.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals(EmailFactory.UNDEFINED, result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(1));
        assertFalse(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertEquals("This is the message content", result.getPlainContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateMailForPlainText() throws Exception {
        var now = new Date();
        var dataAsString = "this is just some dummy content";
        var data = dataAsString.getBytes(StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals(EmailFactory.UNDEFINED, result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(1));
        assertFalse(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertEquals(dataAsString, result.getPlainContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateMailForMultipartWithContentTypeHtmlAndPlain() throws Exception {
        var now = new Date();
        var testFilename = "multipart-mail.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(2));
        assertTrue(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertEquals("This is the message content", result.getPlainContent().get().getData());
        assertEquals("<html><head></head><body>Mail Body</body></html>", result.getHtmlContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateMailForMultipartWithoutContentTypeHtml() throws Exception {
        var now = new Date();
        var testFilename = "multipart-mail-plain-only.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertPlainTextEmail(now, dataAsString, result);
    }

    @Test
    void shouldCreateMailForMultipartWithUnknownContentType() throws Exception {
        var now = new Date();
        var testFilename = "multipart-mail-unknown-content-type.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("This is the mail title", result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(2));
        assertFalse(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertThat(result.getContents().stream().map(EmailContent::getContentType).collect(toList()), contains(ContentType.PLAIN, ContentType.PLAIN));
        assertThat(result.getContents().stream().map(EmailContent::getData).collect(toList()), containsInAnyOrder("This is the message content 1", "This is the message content 2"));
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), empty());
    }

    @Test
    void shouldCreateMailForMultipartWithPlainAndHtmlContentAndAttachments() throws Exception {
        var now = new Date();
        var testFilename = "multipart-mail-html-and-plain-with-attachments.eml";
        var data = TestResourceUtil.getTestFileContentBytes(testFilename);
        var dataAsString = new String(data, StandardCharsets.UTF_8);
        var rawData = new RawData(SENDER, RECEIVER, data);

        when(timestampProvider.now()).thenReturn(now);

        var result = sut.convert(rawData);

        assertEquals(SENDER, result.getFromAddress());
        assertEquals(RECEIVER, result.getToAddress());
        assertEquals("Test-Alternative-Mail 4", result.getSubject());
        assertEquals(dataAsString, result.getRawData());
        assertThat(result.getContents(), hasSize(2));
        assertTrue(result.getHtmlContent().isPresent());
        assertTrue(result.getPlainContent().isPresent());
        assertEquals("This is the test mail number4", result.getPlainContent().get().getData());
        assertEquals("<html><head></head><body>This is the test mail number 4</body>", result.getHtmlContent().get().getData());
        assertEquals(now, result.getReceivedOn());
        assertThat(result.getAttachments(), hasSize(2));
        assertThat(result.getAttachments().stream().map(EmailAttachment::getFilename).collect(toList()), containsInAnyOrder("customizing.css", "app-icon.png"));
    }
}