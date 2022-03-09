package com.zmj.demo.controller;


import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/tool")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @RequestMapping(value = "/sms/getList", method = RequestMethod.GET)
    public Map getList() {
        Map map = new HashMap();
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
        return toolService.getUserBalance(userId,time);
    }

    @RequestMapping(value = "/check/getAllUserBalance", method = RequestMethod.GET)
    public JsonResult getAllUserBalance() {
        return toolService.getAllUserBalance();
    }

    @RequestMapping(value = "/check/userCheck", method = RequestMethod.GET)
    public String userCheck(@Param(value = "userId") String userId, @Param(value = "time") String time) {
        return toolService.userCheck(userId,time);
    }

    @RequestMapping(value = "/check/allUserCheck", method = RequestMethod.GET)
    public String allUserCheck() {
        return toolService.allUserCheck();
    }

    @RequestMapping(value = "/account/getUserPartner",method = RequestMethod.GET)
    public JsonResult getUserPartner(@Param("userId") String userId){
        return new JsonResult<>(0,toolService.getUserPartner(userId));
    }

    @RequestMapping(value = "/check/throughPositions",method = RequestMethod.GET)
    public String throughPositions(@Param("userId") String userId, @Param("money") BigDecimal money,@Param("marginType") int marginType){
        return toolService.throughPositions(userId,money,marginType);
    }

}
