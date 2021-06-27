package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.PlatformChain;
import com.zmj.demo.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @RequestMapping(value = "/platform/getPlatformList",method = RequestMethod.POST)
    public JsonResult getPlatformList(@RequestBody JSONObject jsonObject){
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = platformService.getPlatformAcount(jsonObject);
        PageInfo<List<PlatformChain>> pageInfo = new PageInfo(platformService.getPlatformList(jsonObject));
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }
}
