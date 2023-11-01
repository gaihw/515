package com.zmj.demo.config.prometheus;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * 切面实现prometheus自定义监控指标
 *
 * @author gaihw
 * @date 2023/5/20 13:51
 */

@Aspect
@Component
public class PrometheusMetricsAspect {

    // 切入所有controller包下的请求方法
    @Pointcut("execution(* com.zmj.demo.controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object MetricsCollector(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = StringUtils.hasText( request.getParameter("userId"))?request.getParameter("userId"):"no userId";
        String appId = StringUtils.hasText( request.getParameter("appId"))?request.getParameter("appId"):"no appId";

        // 获取api url
        String api = request.getServletPath();
        // 获取请求方法
        String method = request.getMethod();
        long timeMillis = System.currentTimeMillis();
        LocalDate now = LocalDate.now();
        String[] tags = new String[10];
        tags[0]="api";
        tags[1] = api;
        tags[2]="method";
        tags[3]= method;
        tags[4]="day";
        tags[5]=now.toString();
        tags[6]="appId";
        tags[7]=appId;
        tags[8]="userId";
        tags[9]=userId;
//        统计PromQL
//        每分钟请求总数
//        60*sum(rate(http_request_test_all_total{}[1m]))
//        统计自定义标签api= /test/user
//        60*sum(rate(http_request_test_all_total{api="/test/user"}[1m]))
//        请求平均耗时
//        (sum(http_request_test_time_seconds)/sum(http_request_test_all_total))
//        P90 P99指标统计
//        avg(http_request_test_time_seconds{job="prometheus-test" ,quantile =~ "0.9|0.99"}) by (job,quantile)
//        最大耗时
//        max(http_request_test_time_seconds_max{job="prometheus-test"})
        // 请求次数加1
        //自定义的指标名称：http_request_test_all，指标包含数据
        Metrics.counter("http_request_test_all",tags).increment();
        Object object;
        try {
            object = joinPoint.proceed();
        } catch (Exception e) {
            // 请求失败次数加1
            Metrics.counter("http_request_test_error",tags).increment();
            throw e;
        } finally {
            long f = System.currentTimeMillis();
            long l = f - timeMillis;
            //记录请求响应时间
            Metrics.timer("http_request_test_time", tags).record(l, TimeUnit.MILLISECONDS);
        }
        return object;
    }
}
