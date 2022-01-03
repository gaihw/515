package com.zmj.demo.controller;


import com.zmj.demo.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ToolController {

    @Autowired
    private ToolService baseService;

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
}
