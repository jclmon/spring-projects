package com.jcom.auth.api.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.jcom.auth.api.event.UserEvent;
import com.jcom.auth.api.model.User;
import com.ms.core.common.controller.ControllerBase;

@Controller
public class UserEventController extends ControllerBase implements ApplicationListener<UserEvent> {

	private static Logger logger = LoggerFactory.getLogger(UserEventController.class);

	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;

	@Override
	public void onApplicationEvent(UserEvent event) {
		logger.info("Received spring custom event");
		User data = event.getSource().getData();
			this.brokerMessagingTemplate.convertAndSend("/topic/users", new UserResponse(data.getId(), event.getTimestamp()));
	}
	
}