package com.zmj.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.InterfaceChain;
import com.zmj.demo.enums.MessageEnum;
import com.zmj.demo.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auto")
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    @RequestMapping(value = "/interface/list",method = RequestMethod.POST)
    public JsonResult list(@RequestBody JSONObject jsonObject){

        Integer page = jsonObject.getInteger("page")-1;
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);

        try {
            return new JsonResult(interfaceService.list(jsonObject), "操作成功!",interfaceService.acount(jsonObject));
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100004.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/interface/add",method = RequestMethod.POST)
    public JsonResult add(@RequestBody InterfaceChain interfaceChain){
        String creator = "test";

        int result = 0;
        try{
            result = interfaceService.add(interfaceChain,creator);
            if (result == 1){
                return new JsonResult(0,"操作成功!");
            }else {
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),MessageEnum.ERROR_PLATFORM_100001.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/interface/delete",method = RequestMethod.POST)
    public JsonResult delete(@RequestParam(value = "id") int id){
        int result = 0;
        try {
            result = interfaceService.delete(id);
            if (result == 1){
                return new JsonResult(0,"操作成功!");
            }else {
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(), MessageEnum.ERROR_PLATFORM_100002.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/interface/edit",method = RequestMethod.POST)
    public JsonResult edit(@RequestBody InterfaceChain interfaceChain){
        int result = 0;
        String creator = "test";
        try {
            result = interfaceService.edit(interfaceChain,creator);
            if (result == 1){
                return new JsonResult(0,"操作成功!");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),MessageEnum.ERROR_PLATFORM_100003.getDesc());
            }
        }catch (Exception  e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),e.toString());
        }
    }
}
