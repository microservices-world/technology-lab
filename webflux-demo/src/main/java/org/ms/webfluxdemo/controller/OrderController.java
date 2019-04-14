package org.ms.webfluxdemo.controller;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhenglai
 * @since 2019-04-14 12:33
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    // One client request per second
    private RateLimiter rateLimiter = RateLimiter.create(1.0);

    @PostMapping("/orders")
    public String placeOrder() {
        // 限流代码，正常应该放在网关中，此处指示demo
        // double表示从桶中拿到令牌需要等待的时间
        double acquire = rateLimiter.acquire();
        System.out.println("Got token from bucket, need waiting: " + acquire);

        // business logic
        boolean result = orderService.addOrder();
        if (result) {
            return "Great, you second killed successfully";
        } else {
            return "Please retry later...";
        }
    }
}
