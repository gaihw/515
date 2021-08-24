package com.zmj.demo.config;

import com.zmj.demo.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器，拦截token
 */
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor initAuthInterceptor(){
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*调用我们创建的SessionInterceptor。
         * addPathPatterns("/interfaceTest/**)的意思是这个链接下的都要进入到SessionInterceptor里面去执行
         * excludePathPatterns("/login")的意思是login的url可以不用进入到SessionInterceptor中，直接
         * 放过执行。
         *
         * 注意：如果像注释那样写是不可以的。这样等于是创建了多个Interceptor。而不是只有一个Interceptor
         * 所以这里有个大坑，搞了很久才发现问题。
         *
         * */
        registry.addInterceptor(initAuthInterceptor())
                .addPathPatterns("/api/auto/**")
//                .addPathPatterns("/currency/**")
//                .addPathPatterns("/documentary/**")
//                .addPathPatterns("/fex/**")
//                .addPathPatterns("/main/**")
//                .addPathPatterns("/index")
                .excludePathPatterns("/login/**","/register/**");
    }

}
