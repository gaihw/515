package com.zmj.demo.controller;


import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.UserBalanceChain;
import com.zmj.demo.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/tool")
public class ToolController {

    @Autowired
    private ToolService baseService;

    @Autowired
    private AccountDao accountDao;

    @RequestMapping(value = "/base/getList",method = RequestMethod.GET)
    public Map getList(){
        Map map = new HashMap();

        map.put("pageSize","");
        map.put("pageNo","");
        map.put("total","");
        map.put("data",baseService.getList());
        map.put("status","success");
        map.put("errorCode","");
        map.put("errorMsg","");
        return map;
    }

    @RequestMapping(value = "/base/test",method = RequestMethod.GET)
    public String test(){
        return "test...";
    }

    @RequestMapping(value = "/check/userCheck",method = RequestMethod.GET)
    public String userCheck(@Param(value = "userId") String userId){
        return "测试对账！"+userId;
    }

    @RequestMapping(value = "/check/getBalance",method = RequestMethod.GET)
    public List<UserBalanceChain> getBalance(@Param(value = "userId") String userId){
        List<UserBalanceChain> res = accountDao.getUserBalanceHold(userId);
        log.info("用户:{},账户:{}",userId,res);
        return res;
    }
}
