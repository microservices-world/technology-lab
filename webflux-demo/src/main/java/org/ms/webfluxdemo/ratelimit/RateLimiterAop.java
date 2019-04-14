package org.ms.webfluxdemo.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhenglai
 * @since 2019-04-14 14:05
 */
@Aspect
@Component
public class RateLimiterAop {
    private Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    // aop arounding: aop环绕通知
    @Pointcut("execution(public * org.ms.*.*(..))")
    public void rlAop() {}

    @Around(("rlAop()"))
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = getMethod(proceedingJoinPoint);
        if (Objects.isNull(method)) {
            return null;
        }

        ExtRateLimiter extRateLimiter = method.getDeclaredAnnotation(ExtRateLimiter.class);
        if (Objects.isNull(extRateLimiter)) {
            return proceedingJoinPoint.proceed();
        }

        double permitsPerSecond = extRateLimiter.permitsPerSeccond();
        long timeout = extRateLimiter.timeout();

        HttpServletRequest request = getRequest();
        String requestUri = request.getRequestURI();
        RateLimiter rateLimiter;
        if (map.containsKey(requestUri)) {
            rateLimiter = map.get(requestUri);
        } else {
            rateLimiter = RateLimiter.create(permitsPerSecond);
            map.put(requestUri, rateLimiter);
        }

        boolean acquire = rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        if (!acquire) {
            // fallback
            return fallback();
        }
        return proceedingJoinPoint.proceed();
    }

    private String fallback() {
        return "Fallbacking, to be  customized...";
    }

    public void fallback2() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-Type", "text/html;charset-UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Fallbacking, to be  done");
        } catch (Exception ex) {
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        return method;
    }
}
