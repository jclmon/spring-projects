package pl.jcom.common.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pl.jcom.common.constants.ConfigConstants;

@Service
public class EventBus implements MessageListener {

	private final Log log = LogFactory.getLog(EventBus.class);
			
	private final ApplicationEventPublisher publisher;
	private final RabbitTemplate rabbitTemplate;
	private final String queeName;
	private final String exangeTopic;
	private final String expiration;

	/**
	 * Constructor cuando no se utiliza cola de eventos de rabbitmq
	 * 
	 * @param publisher
	 */
	public EventBus(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
		this.rabbitTemplate = null;
		this.queeName = null;
		this.exangeTopic = null;
		this.expiration = null;
	}
	
	/**
	 * Constructor
	 * 
	 * @param publisher
	 * @param rabbitTemplate
	 * @param queeName
	 * @param exchangeTopic
	 * @param expiration
	 */
	public EventBus(ApplicationEventPublisher publisher, RabbitTemplate rabbitTemplate,
			String queeName, String exchangeTopic, String expiration) {
		this.publisher = publisher;
		this.rabbitTemplate = rabbitTemplate;
		this.queeName = queeName;
		this.exangeTopic = exchangeTopic;
		this.expiration = expiration;
	}

	public void publishEvent(ApplicationEvent event) {
		this.publisher.publishEvent(event);
		if (event instanceof RemoteEvent && this.rabbitTemplate !=null) {
			this.sendMessage(event);
		}
	}

	public void sendMessage(ApplicationEvent event) {
		MessageProperties props = MessagePropertiesBuilder.newInstance()
				.setContentType(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT)
				.setMessageId(UUID.randomUUID().toString()).setExpiration(this.expiration).build();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(event);
		} catch (Throwable e) {
			log.error("Error serialize event ", e);
		}

		Message message = MessageBuilder.withBody(byteOut.toByteArray()).andProperties(props).build();
		rabbitTemplate.send(this.exangeTopic, this.queeName, message);
	}

	@Override
	public void onMessage(Message message) {
		try {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(message.getBody());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			AbstractEvent<?> event = (AbstractEvent<?>) in.readObject();

			if (!event.getApplicationId().equals(ConfigConstants.APPLICATION_ID) && (event instanceof SendRemote)) {
				publishEvent(event);
			}
		} catch (Throwable e) {
			log.error("Error parsing message ", e);
		}

	}

}