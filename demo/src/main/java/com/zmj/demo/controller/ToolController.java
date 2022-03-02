package com.zmj.demo.controller;


import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/tool")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @RequestMapping(value = "/base/getList", method = RequestMethod.GET)
    public Map getList() {
        Map map = new HashMap();

        map.put("pageSize", "");
        map.put("pageNo", "");
        map.put("total", "");
        map.put("data", toolService.getList());
        map.put("status", "success");
        map.put("errorCode", "");
        map.put("errorMsg", "");
        return map;
    }

    @RequestMapping(value = "/base/test", method = RequestMethod.GET)
    public String test() {
        return "test...";
    }

    /**
     * 余额同步
     *
     * @param userId
     * @param time
     * @return
     */
    @RequestMapping(value = "/check/getBalance", method = RequestMethod.GET)
    public JsonResult getBalance(@Param(value = "userId") String userId, @Param(value = "time") String time) {
        return toolService.getBalance(userId,time);
    }

    @RequestMapping(value = "/check/userCheck", method = RequestMethod.GET)
    public String userCheck(@Param(value = "userId") String userId, @Param(value = "time") String time) {
        return toolService.userCheck(userId,time);
    }


}
