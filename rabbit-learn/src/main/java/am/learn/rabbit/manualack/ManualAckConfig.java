package am.learn.rabbit.manualack;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManualAckConfig {

    public static final String QUEUE = "learn.manualack";
    public static final String FACTORY = "manualAckContainerFactory";

    @Bean
    Queue manualAckQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean(name = FACTORY)
    SimpleRabbitListenerContainerFactory manualAckContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setPrefetchCount(1);
        return factory;
    }
}
