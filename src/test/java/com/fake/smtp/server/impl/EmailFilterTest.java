package com.fake.smtp.server.impl;

import com.fake.smtp.config.FakeSmtpConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailFilterTest {
  private static final String TEST_EMAIL_ADDRESS_1 = "john@doe.com";
  private static final String TEST_EMAIL_ADDRESS_2 = "jane@doe.com";

  @Mock
  private FakeSmtpConfigurationProperties fakeSmtpConfigurationProperties;

  @Mock
  private Logger logger;

  @InjectMocks
  private EmailFilter sut;

  @Test
  void emptyFilter(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn(null);
    assertFalse(sut.ignore(TEST_EMAIL_ADDRESS_1, TEST_EMAIL_ADDRESS_2));

    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn("      ");
    assertFalse(sut.ignore(TEST_EMAIL_ADDRESS_1, TEST_EMAIL_ADDRESS_2));
  }

  @Test
  void noneMatchingFilter(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn(".*@google.com");
    assertFalse(sut.ignore(TEST_EMAIL_ADDRESS_1,TEST_EMAIL_ADDRESS_2));
  }

  @Test
  void matchingFilter(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn(".*@doe.com");
    assertTrue(sut.ignore(TEST_EMAIL_ADDRESS_1,TEST_EMAIL_ADDRESS_2));
  }

  @Test
  void matchingFilterMultipleRegexAnyMatch(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn(".*@other\\.com,jane@.*");
    assertTrue(sut.ignore(TEST_EMAIL_ADDRESS_1,TEST_EMAIL_ADDRESS_2));
  }

  @Test
  void matchingFilterMultipleRegexAllMatch(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn(".*@doe\\.com,jane@.*");
    assertTrue(sut.ignore(TEST_EMAIL_ADDRESS_1,TEST_EMAIL_ADDRESS_2));
  }

  @Test
  void invalidRegex(){
    when(fakeSmtpConfigurationProperties.getFilteredEmailRegexList()).thenReturn("****");
    assertFalse(sut.ignore(TEST_EMAIL_ADDRESS_1,TEST_EMAIL_ADDRESS_2));
  }
}
