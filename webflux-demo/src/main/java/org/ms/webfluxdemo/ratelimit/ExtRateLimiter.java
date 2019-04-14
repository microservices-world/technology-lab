package org.ms.webfluxdemo.ratelimit;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zhenglai
 * @since 2019-04-14 13:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtRateLimiter {
    /**
     *  每秒时间内往令牌桶中生成令牌的速率
     */
    double permitsPerSeccond();

    /**
     *  获得令牌的timeout毫秒数
     */
    long timeout();
}
