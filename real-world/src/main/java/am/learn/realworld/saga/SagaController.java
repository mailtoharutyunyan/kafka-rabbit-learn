package am.learn.realworld.saga;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/realworld/saga")
@RequiredArgsConstructor
public class SagaController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam(defaultValue = "99.90") String amount,
                              @RequestParam(defaultValue = "false") boolean shouldFail) {
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        orderService.createOrder(orderId, amount, shouldFail);
        return "saga started: orderId=" + orderId + " shouldFail=" + shouldFail
                + " (watch logs: order→payment→order confirmation/compensation)";
    }
}
