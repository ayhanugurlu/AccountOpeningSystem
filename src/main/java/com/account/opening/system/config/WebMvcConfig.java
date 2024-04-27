package com.account.opening.system.config;

import com.account.opening.system.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {



    @Value("${rate.limit.token:2}")
    private int rateLimitToken;

    @Value("${rate.limit.time:1}")
    private int rateLimitTime;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitToken, rateLimitTime   ))
                .addPathPatterns("/overview/**","/token","/register");

    }

}
