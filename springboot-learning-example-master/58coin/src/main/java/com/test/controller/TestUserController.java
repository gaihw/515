package com.test.controller;

import com.test.domain.TestUser;
import com.test.domain.User;
import com.test.service.TestUserService;
import com.test.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
public class TestUserController {
    Map<String,Object> response = new HashMap<String, Object>();
    @Autowired
    private TestUserService testUserService;
//
    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public Map<String,Object> login(HttpServletResponse response_res,
                                    @RequestBody TestUser testUser){
        response_res.setHeader("Access-Control-Allow-Origin", "*");
        //允许请求方式
        response_res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //用来指定本次预检请求的有效期，单位为秒，在此期间不用发出另一条预检请求
        response_res.setHeader("Access-Control-Max-Age", "3600");
        //请求包含的字段内容，如有多个可用哪个逗号分隔如下
        response_res.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild");
        //访问控制允许凭据，true为允许
        response_res.setHeader("Access-Control-Allow-Credentials", "true");
        response.clear();

        try{
           int i =  testUserService.findTestUser(testUser.getUserName(),testUser.getPassword());
            Cookie cookie = new Cookie("login","true");
            response_res.addCookie(cookie);
            log.info("查询结果{}",i);
            if(i == 1){
                response.put("code",200);
                response.put("msg","success");
                response.put("data",null);
                return response;
            }

        }catch (Exception e){
            response.put("msg",e.toString());
            return response;
        }
        return null;
    }
}
