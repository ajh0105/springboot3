package com.example.tour.config;

import com.example.tour.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/tour/**", "/review/**") // 보호 영역
                .excludePathPatterns(
                        "/",
                        "/member/login",
                        "/member/new",
                        "/tour/list",
                        "/tour/detail/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**"
                );
    }
}
