package com.ms.core.common.service;

import org.springframework.mail.MailException;

public interface NotificationService {

	void sendNotification(String from, String to, String subject, String text) throws MailException, InterruptedException;

}
