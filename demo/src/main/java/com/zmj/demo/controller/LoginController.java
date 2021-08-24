package com.zmj.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.BaseUtils;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.demodata.UserDao;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.LoginChain;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private BaseUtils baseUtils;

    @Resource
    private UserDao userDao;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public JSONObject login(@RequestBody LoginChain loginChain){
        String token = null;
        JSONObject jsonObject1 = new JSONObject();
        String username = loginChain.getUsername();
        String password = loginChain.getPassword();
        String md5 = userDao.getPwd(username);
        log.info("用户:{},密码:{},登录时间为:{}",username,password,System.currentTimeMillis());
        if(!Objects.equals(null,md5)){
            boolean flag = baseUtils.verify(password,md5.toLowerCase());
            if (flag) {
                 token = UUID.randomUUID().toString();
                try {
                    redisService.addValueSecond(token,loginChain.getUsername(), Config.MAX_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonObject1.put("code",200);
                jsonObject1.put("token",token);
            }else {
                jsonObject1.put("code",201);
                jsonObject1.put("msg","用户不存在或者密码错误！");
            }
        }else {
            jsonObject1.put("code",201);
            jsonObject1.put("msg","用户不存在或者密码错误！");
        }
        return jsonObject1;
    }

    @RequestMapping(value = "/login/out",method = RequestMethod.GET)
    public JSONObject out(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("data",null);
        String token = request.getHeader("token");
        Boolean delete = redisService.delete(token);
        if (!delete) {
            jsonObject.put("msg","注销失败，请检查是否登录！");
            return jsonObject;
        }
        jsonObject.put("msg","注销成功！");
        return jsonObject;
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public JsonResult index(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return new JsonResult(401,"用户未登录或登录失效，请登录后操作！");
        }
        //如果请求头部携带token，判断token在redis中是否过期，如果过期，重新登录
        Object loginStatus = null;
        try {
            loginStatus = redisService.getValue(token);
            if( Objects.isNull(loginStatus)){
                return new JsonResult(401,"用户未登录或登录失效，请登录后操作！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult(200,"登录成功！");

    }
}
