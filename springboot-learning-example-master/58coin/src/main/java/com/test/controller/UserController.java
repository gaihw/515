package com.test.controller;

import com.test.domain.Sms;
import com.test.domain.User;
import com.test.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/userInfoByMobile" , method = RequestMethod.GET)
    public User findUserInfoByMobile(@RequestParam(value = "mobile" ,required = true ) String mobile){
        return userInfoService.findUserInfoByMobile(mobile);
    }
    @RequestMapping(value = "/userInfoByUserId" , method = RequestMethod.GET)
    public User findUserInfoByUserId(@RequestParam(value = "id" ,required = true ) int id){
        return userInfoService.findUserInfoByUserId(id);
    }
    @RequestMapping(value = "/userInfoByUserIdOrMobile" , method = RequestMethod.GET)
    public User findUserInfoByUserIdOrMobile(@RequestParam(value = "id" ,required = true ) int id,
                                             @RequestParam(value = "mobile" ,required = true ) String mobile){
        return userInfoService.findUserInfoByUserIdOrMobile(id,mobile);
    }
    @RequestMapping(value = "/sms",method = RequestMethod.GET)
    public Sms sms(@RequestParam(value = "mobile",required=true)String mobile){
        Sms result = userInfoService.sms(mobile);
        log.info("短信验证码{}",result);
        return result;
    }
}
