package com.zmj.demo.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 拦截器，拦截器中需要取出header中的token，然后去redis中进行判断，如果存在，则允许操作，否则返回提示信息
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
//        response.setStatus(302);
        //获取请求头部的token，如果为空，重新登录
        String token = request.getHeader("token");
        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isEmpty(token)) {
            jsonObject.put("code",401);
            jsonObject.put("msg","用户未登录或登录失效，请登录后操作！");
            response.getWriter().write(jsonObject.toString());
            return false;
        }
        //如果请求头部携带token，判断token在redis中是否过期，如果过期，重新登录
        Object loginStatus = redisService.getValue(token);
        if( Objects.isNull(loginStatus)){
            jsonObject.put("code",401);
            jsonObject.put("msg","用户未登录或登录失效，请登录后操作！");
            response.getWriter().write(jsonObject.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
