package com.zmj.controller.xrp;


import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.JsonResult;
import com.zmj.domain.xrp.XrpChain;
import com.zmj.service.XrpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class XrpController {


    @Autowired
    private XrpService xrpService;


    @RequestMapping(value= "/xrp/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody XrpChain xrpChain){
        String res = xrpService.transaction(xrpChain);
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getJSONObject("result").getJSONObject("tx_json").getString("hash"));
    }


    @RequestMapping(value= "/xrp/accountInfo", method = RequestMethod.GET)
    public JsonResult getAccountInfo(@RequestParam String account){
        String res = xrpService.getAccountInfo(account);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/xrp/transactionInfo", method = RequestMethod.GET)
    public JsonResult getTransaction(@RequestParam String transaction){
        String res = xrpService.getTransaction(transaction);
        log.info("{}",res);
        return new JsonResult(res);
    }

}
