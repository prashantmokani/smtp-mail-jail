package com.fake.smtp.service;

import com.fake.smtp.config.FakeSmtpConfigurationProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class EmailRetentionTimer {

    private final FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties;
    private final Logger logger;
    private final EmailService emailService;

    @Autowired
    public EmailRetentionTimer(FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties, Logger logger, EmailService emailService) {
        this.fakeSmtpConfigurationProperties = fakeSmtpConfigurationProperties;
        this.logger = logger;
        this.emailService = emailService;
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 500)
    public void deleteOutdatedMails(){
        var persistence = fakeSmtpConfigurationProperties.getPersistence();
        if(isDataRetentionConfigured(persistence)){
            var maxNumber = persistence.getMaxNumberEmails();
            var count = emailService.deleteEmailsExceedingDateRetentionLimit(maxNumber);

            logger.info("Deleted {} emails which exceeded the maximum number {} of emails to be stored", count, maxNumber);
        }
    }

    private boolean isDataRetentionConfigured(FakeSmtpConfigurationProperties.Persistence persistence) {
        return persistence != null && persistence.getMaxNumberEmails() != null && persistence.getMaxNumberEmails() > 0;
    }

}
