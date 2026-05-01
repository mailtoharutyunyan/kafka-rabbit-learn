package am.learn.realworld.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createOrder(String orderId, String amount, boolean shouldFail) {
        String event = "{\"orderId\":\"" + orderId + "\",\"amount\":" + amount
                + ",\"shouldFail\":" + shouldFail + ",\"status\":\"CREATED\"}";
        log.info("[saga-order] -> order.created: {}", event);
        kafkaTemplate.send(SagaConfig.ORDER_EVENTS, orderId, event);
    }

    @KafkaListener(topics = SagaConfig.PAYMENT_EVENTS, groupId = "saga-order-group")
    public void onPaymentEvent(String event) {
        if (event.contains("\"status\":\"COMPLETED\"")) {
            log.info("[saga-order] <- payment COMPLETED → order CONFIRMED: {}", event);
        } else if (event.contains("\"status\":\"FAILED\"")) {
            log.warn("[saga-order] <- payment FAILED → COMPENSATING (cancelling order): {}", event);
        }
    }
}
