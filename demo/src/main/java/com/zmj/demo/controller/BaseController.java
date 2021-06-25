package com.zmj.demo.controller;


import com.zmj.demo.domain.BaseDomain;
import com.zmj.demo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BaseController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "/base/getList",method = RequestMethod.GET)
    public Map getList(){
        Map map = new HashMap();

        Map map1 = new HashMap();
        map1.put("pageSize","");
        map1.put("pageNo","");
        map1.put("total","");
        map1.put("interfaceReturnListDtos",baseService.getList());

//        Map map2 = new HashMap();
//        map2.put("result",true);
//        map2.put("errorMessage","连接成功");
//        map2.put("validateMessage","");
//        map2.put("data",map1);

        map.put("status","success");
        map.put("errorCode","");
        map.put("errorMsg","");
        map.put("data",map1);
        return map;
    }

    @RequestMapping(value = "/base/test",method = RequestMethod.GET)
    public String test(){
        return "test...";
    }
}
