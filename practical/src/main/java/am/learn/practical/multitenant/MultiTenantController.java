package am.learn.practical.multitenant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/practical/multitenant")
@RequiredArgsConstructor
public class MultiTenantController {

    private final MultiTenantProducer producer;

    @PostMapping("/kafka")
    public String kafka(@RequestParam(defaultValue = "tenant-a") String tenantId,
                        @RequestParam(defaultValue = "user-signed-up") String event) {
        producer.sendToKafka(tenantId, event);
        return "kafka: sent to topic learn.mt." + tenantId;
    }

    @PostMapping("/rabbit")
    public String rabbit(@RequestParam(defaultValue = "tenant-a") String tenantId,
                         @RequestParam(defaultValue = "order") String eventType,
                         @RequestParam(defaultValue = "order-123") String payload) {
        producer.sendToRabbit(tenantId, eventType, payload);
        return "rabbit: sent with routing=" + tenantId + "." + eventType;
    }

    @PostMapping("/demo")
    public String demo() {
        producer.sendToKafka("tenant-a", "user-login");
        producer.sendToKafka("tenant-b", "user-login");
        producer.sendToRabbit("tenant-a", "order.created", "order-A1");
        producer.sendToRabbit("tenant-b", "order.created", "order-B1");
        producer.sendToRabbit("tenant-a", "payment.processed", "pay-A1");
        return "sent events for both tenants — check logs for routing";
    }
}
