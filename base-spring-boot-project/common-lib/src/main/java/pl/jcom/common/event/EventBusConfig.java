package pl.jcom.common.event;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnMissingBean(EventBusConfig.class)
@Configuration
public class EventBusConfig {

	@Value("${xg.rabbitmq.queue:XGQueueCommon}")
	String queeName;

	@Value("${xg.rabbitmq.topic:spring-boot-exchange}")
	String exchangeTopic;

	@Value("${xg.rabbitmq.expiration:5000}")
	String expiration;

	@Value("${spring.rabbitmq.host:localhost}")
	String host;

	@Value("${spring.rabbitmq.username:guest}")
	String username;

	@Value("${spring.rabbitmq.password:guest}")
	private String password;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Bean
	public EventBus eventBus() {
		return new EventBus(applicationEventPublisher, rabbitTemplate(), queeName, exchangeTopic, expiration);
	}

	@Bean
	public Queue queue() {
		return new Queue(queeName, false);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchangeTopic);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queeName);
	}

//	 @Bean
//	 MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
//		 SimpleMessageListenerContainer simpleMessageListenerContainer = new
//		 SimpleMessageListenerContainer();
//		 simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
//		 simpleMessageListenerContainer.setQueues(queue());
//		 simpleMessageListenerContainer.setMessageListener(eventBus());
//		 return simpleMessageListenerContainer;
//	
//	 }

	// create custom connection factory
	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setUsername(password);
		return cachingConnectionFactory;
	}

	// create MessageListenerContainer using custom connection factory
	@Bean
	MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(eventBus());
		return simpleMessageListenerContainer;

	}

	// create RabbitTemplate
	@Bean
	RabbitTemplate rabbitTemplate() {
	    RabbitTemplate template = new RabbitTemplate(connectionFactory());
	    template.setRoutingKey(queeName);
	    return template;
	}
	
}
