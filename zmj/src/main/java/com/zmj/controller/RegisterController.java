package com.zmj.controller;


import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.BaseUtils;
import com.zmj.config.Config;
import com.zmj.dao.platform.UserDao;
import com.zmj.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@Slf4j
public class RegisterController {

    @Autowired
    private RedisService redisService;

    @Resource
    private UserDao userDao;

    @Autowired
    private BaseUtils baseUtils;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public JSONObject register(@RequestBody JSONObject jsonObject){
        JSONObject jsonObject1 = new JSONObject();
        String token = null;
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String mobile = jsonObject.getString("mobile");
        String name = jsonObject.getString("name");
        String pwmd5 = baseUtils.generate(password);
        int flag = userDao.register(username,name,pwmd5.toUpperCase(),mobile);
        if(flag >0 ){
            token = UUID.randomUUID().toString();
            try {
                redisService.addValueSecond(token,username, Config.MAX_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonObject1.put("code",200);
            jsonObject1.put("msg","success");
            jsonObject1.put("token",token);
            return jsonObject1;
        }else{
            jsonObject1.put("code",202);
            jsonObject1.put("msg","注册失败！");
            return jsonObject1;
        }

    }

    @RequestMapping(value = "/register/check",method = RequestMethod.POST)
    public boolean check(@RequestBody JSONObject jsonObject){
        String username = jsonObject.getString("username");
        int flag = userDao.check(username);
        if(flag > 0 ){
            return true;
        }else{
            return false;
        }
    }
}
