package org.ms.webfluxdemo.controller;

import org.ms.webfluxdemo.ratelimit.ExtRateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhenglai
 * @since 2019-04-14 14:24
 */
@RestController
public class RateLimitTestController {
    @GetMapping("/test/ratelimit")
    @ExtRateLimiter(permitsPerSeccond = 1.0,  timeout = 50)
    public String test() {
        return "success";
    }
}
