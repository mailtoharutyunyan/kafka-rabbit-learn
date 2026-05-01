package am.learn.rabbit.rpc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit/rpc")
@RequiredArgsConstructor
public class RpcController {

    private final RpcClient client;

    @PostMapping("/call")
    public String call(@RequestParam(defaultValue = "hello world") String request) {
        return client.call(request);
    }
}
