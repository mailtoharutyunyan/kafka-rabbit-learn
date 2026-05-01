package am.learn.rabbit.headersexchange;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

// whereAll => queue gets message only if ALL headers match
// where...matches => queue gets message if that single header matches (any-of via multiple bindings)


@Configuration
public class HeadersExchangeConfig {

    public static final String EXCHANGE = "learn.headers";
    public static final String QUEUE_PDF = "learn.headers.pdf";
    public static final String QUEUE_IMAGE = "learn.headers.image";

    @Bean HeadersExchange headersExchange() { return new HeadersExchange(EXCHANGE); }
    @Bean Queue headersPdfQueue() { return QueueBuilder.durable(QUEUE_PDF).build(); }
    @Bean Queue headersImageQueue() { return QueueBuilder.durable(QUEUE_IMAGE).build(); }

    @Bean
    Binding bindPdf(HeadersExchange headersExchange, Queue headersPdfQueue) {
        return BindingBuilder.bind(headersPdfQueue).to(headersExchange)
                .whereAll(Map.of("format", "pdf", "type", "report")).match();
    }

    @Bean
    Binding bindImagePng(HeadersExchange headersExchange, Queue headersImageQueue) {
        return BindingBuilder.bind(headersImageQueue).to(headersExchange)
                .where("format").matches("png");
    }

    @Bean
    Binding bindImageJpg(HeadersExchange headersExchange, Queue headersImageQueue) {
        return BindingBuilder.bind(headersImageQueue).to(headersExchange)
                .where("format").matches("jpg");
    }
}
