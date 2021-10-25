package com.ms.core.common.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import com.ms.core.common.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	public NotificationServiceImpl(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}
	
	@Async
	@Override
	public void sendNotification(String from, String to, String subject, String text) throws MailException, InterruptedException {
		
        logger.info("Sending email to " + to + " ...");
        
        SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(to);
		mail.setFrom(from);
		mail.setSubject(subject);
		mail.setText(text);
		javaMailSender.send(mail);
		
		logger.info("Email Sent!");
		
	}
	
}