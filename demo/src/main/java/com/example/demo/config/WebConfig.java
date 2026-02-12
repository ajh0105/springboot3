package com.example.demo.config;

import com.example.demo.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")  // 모든 경로에 적용
                .excludePathPatterns(
                        "/", "/home",
                        "/member/join", "/member/login", "/member/logout",
                        "/board/list",
                        "/css/**", "/js/**", "/images/**",
                        "/error", "/favicon.ico"
                );  // 인터셉터로 가지 않는 경로: 제외할 경로
    }
}