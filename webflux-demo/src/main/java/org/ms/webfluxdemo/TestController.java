package org.ms.webfluxdemo;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Zhenglai
 * @since 2019-04-09 00:34
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("/1")
    public String get1() {
        log.info("get1 start");
        var result = longRunTask();
        log.info("get1 end");
        return result;
    }

    @GetMapping("/2")
    public Mono<String> get2() {
        log.info("get2 start");
        var result = Mono.fromSupplier(this::longRunTask);
        log.info("get2 end");
        return result;
    }


    @GetMapping(value = "/3", produces = "text/event-stream")
    public Flux<String> get3() {
        log.info("get3 start");
        var result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "flux data--" + i;
        }));
        log.info("get3 end");
        return result;
    }

    private String longRunTask() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world";
    }
}
