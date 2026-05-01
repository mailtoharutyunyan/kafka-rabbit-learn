package am.learn.realworld.saga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = SagaConfig.ORDER_EVENTS, groupId = "saga-payment-group")
    public void onOrderCreated(String event) {
        log.info("[saga-payment] <- order event received: {}", event);

        boolean shouldFail = event.contains("\"shouldFail\":true");
        String orderId = extractField(event, "orderId");

        if (shouldFail) {
            String failEvent = "{\"orderId\":\"" + orderId + "\",\"status\":\"FAILED\",\"reason\":\"insufficient funds\"}";
            log.warn("[saga-payment] -> payment FAILED: {}", failEvent);
            kafkaTemplate.send(SagaConfig.PAYMENT_EVENTS, orderId, failEvent);
        } else {
            String successEvent = "{\"orderId\":\"" + orderId + "\",\"status\":\"COMPLETED\"}";
            log.info("[saga-payment] -> payment COMPLETED: {}", successEvent);
            kafkaTemplate.send(SagaConfig.PAYMENT_EVENTS, orderId, successEvent);
        }
    }

    private String extractField(String json, String field) {
        int start = json.indexOf("\"" + field + "\":\"") + field.length() + 4;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
