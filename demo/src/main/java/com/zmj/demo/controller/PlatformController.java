package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.PlatformChain;
import com.zmj.demo.enums.MessageEnum;
import com.zmj.demo.service.PlatformService;
//import com.zmj.demo.service.impl.config.RedisService;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(value = "/api/auto")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private RedisService redisService;


    @RequestMapping(value = "/platform/list",method = RequestMethod.POST)
    public JsonResult getPlatformList(@RequestBody JSONObject jsonObject){
        log.info("项目列表请求参数:{}",jsonObject);
        Integer page = jsonObject.getInteger("page")-1;
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = platformService.getPlatformAcount(jsonObject);
        PageInfo<List<PlatformChain>> pageInfo = new PageInfo(platformService.getPlatformList(jsonObject));
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }

    @RequestMapping(value = "/platform/add",method = RequestMethod.POST)
    public JsonResult addPlatform(@RequestBody PlatformChain platformChain){
        String creator = "admin";
        int result = 0;
        try{
            result = platformService.addPlatform(platformChain,creator);
            if (result == 1){
                return new JsonResult(0,"添加成功!");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),MessageEnum.ERROR_PLATFORM_100001.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),e.toString());
        }
    }
    @RequestMapping(value = "/platform/delete",method = RequestMethod.POST)
    public JsonResult deletePlatform(@RequestParam(value = "id") Integer id){
        try{
            int del_res = platformService.deletePlatformData(id);
            if (del_res > 0){
                return new JsonResult(0,"删除成功");
            }else {
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(),MessageEnum.ERROR_PLATFORM_100002.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/platform/edit",method = RequestMethod.POST)
    public JsonResult editPlatform(@RequestBody PlatformChain platformChain){
        String creator = "test";

        int result = 0;
        try{
            result = platformService.editPlatform(platformChain,creator);
            if (result == 1){
                return new JsonResult(0,"修改成功");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),MessageEnum.ERROR_PLATFORM_100003.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/platform/list")
    public JsonResult listPlatform(){
        return null;
    }


}
