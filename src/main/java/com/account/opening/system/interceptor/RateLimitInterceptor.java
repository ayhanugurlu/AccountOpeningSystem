package com.account.opening.system.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;


public class RateLimitInterceptor implements HandlerInterceptor {

    private final Bucket bucket;


    public RateLimitInterceptor(int rateLimitToken, int rateLimitTime) {
        Refill refill = Refill.intervally(rateLimitToken, Duration.ofMinutes(rateLimitTime));
        Bandwidth limit = Bandwidth.classic(rateLimitToken, refill);
        bucket = Bucket.builder().addLimit(limit).build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!bucket.tryConsume(1)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate Limit exceeded"); // Set the response message
            return false; // Stop further processing
        }
        return true;
    }

}
