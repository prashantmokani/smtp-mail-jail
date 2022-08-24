package com.fake.smtp.repository;

import com.fake.smtp.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository <Email, Long> {
}
