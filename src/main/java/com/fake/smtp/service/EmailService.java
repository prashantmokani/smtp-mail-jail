package com.fake.smtp.service;

public interface EmailService {

    int deleteEmailsExceedingDateRetentionLimit(int maxNumber);
}
