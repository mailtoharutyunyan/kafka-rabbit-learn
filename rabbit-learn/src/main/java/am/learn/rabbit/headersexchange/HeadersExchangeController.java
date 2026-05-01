package am.learn.rabbit.headersexchange;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbit/headers")
@RequiredArgsConstructor
public class HeadersExchangeController {

    private final HeadersExchangeProducer producer;

    @PostMapping("/send")
    public String send(@RequestParam(defaultValue = "pdf") String format,
                       @RequestParam(defaultValue = "report") String type,
                       @RequestParam(defaultValue = "file-data") String body) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("format", format);
        headers.put("type", type);
        producer.send(headers, body);
        return "sent headers=" + headers;
    }
}
