package com.fake.smtp.service;

import com.fake.smtp.repository.EmailRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @PersistenceContext
    private EntityManager entityManager;

    private final EmailRepository emailRepository;
    private final Logger logger;

    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository, Logger logger) {
        this.emailRepository = emailRepository;
        this.logger = logger;
    }

    @Override
    public int deleteEmailsExceedingDateRetentionLimit(int maxNumber) {

        List emails = entityManager.createQuery("select e from Email e order by e.receivedOn desc")
                .setFirstResult(maxNumber)
                .getResultList();

        var count = emails.size();

        this.emailRepository.deleteAll(emails);

        return count;
    }
}
