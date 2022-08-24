package com.fake.smtp.service;

import com.fake.smtp.config.FakeSmtpConfigurationProperties;
import com.fake.smtp.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailRetentionTimerTest {

    @Mock
    private FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties;
    @Mock
    private EmailService emailService;
    @Mock
    private EmailRetentionTimer emailRetentionTimer;
    @Mock
    private Logger logger;

    @InjectMocks
    private EmailRetentionTimer sut;

    @Test
    void shouldTriggerDeletionWhenDataRetentionIsConfigured(){
        var maxNumber = 5;
        var persistence = mock(FakeSmtpConfigurationProperties.Persistence.class);
        when(persistence.getMaxNumberEmails()).thenReturn(maxNumber);
        when(fakeSmtpConfigurationProperties.getPersistence()).thenReturn(persistence);

        sut.deleteOutdatedMails();

        verify(emailService).deleteEmailsExceedingDateRetentionLimit(maxNumber);
    }

    @Test
    void shouldNotTriggerDeletionWhenConfiguredMaxNumberIsNull(){
        var persistence = mock(FakeSmtpConfigurationProperties.Persistence.class);
        when(persistence.getMaxNumberEmails()).thenReturn(null);
        when(fakeSmtpConfigurationProperties.getPersistence()).thenReturn(persistence);

        sut.deleteOutdatedMails();

        verify(emailService, never()).deleteEmailsExceedingDateRetentionLimit(anyInt());
    }

    @Test
    void shouldNotTriggerDeletionWhenConfiguredMaxNumberIsLessOrEqualToZero(){
        var persistence = mock(FakeSmtpConfigurationProperties.Persistence.class);
        when(persistence.getMaxNumberEmails()).thenReturn(0);
        when(fakeSmtpConfigurationProperties.getPersistence()).thenReturn(persistence);

        sut.deleteOutdatedMails();

        verify(emailService, never()).deleteEmailsExceedingDateRetentionLimit(anyInt());
    }

    @Test
    void shouldNotTriggerDeletionWhenNoPersistenceIsConfigured(){
        when(fakeSmtpConfigurationProperties.getPersistence()).thenReturn(null);

        sut.deleteOutdatedMails();

        verify(emailService, never()).deleteEmailsExceedingDateRetentionLimit(anyInt());
    }

}