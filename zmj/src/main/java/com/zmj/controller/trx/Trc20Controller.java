package com.zmj.controller.trx;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.JsonResult;
import com.zmj.domain.trx.Trc20Chain;
import com.zmj.service.Trc20Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class Trc20Controller {
    @Autowired
    private Trc20Service trc20Service;

    @RequestMapping(value = "/trc20/transaction",method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody Trc20Chain trc20Chain){
        String res = trc20Service.createtransaction(trc20Chain);
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getString("txid"));
    }

    @RequestMapping(value = "/trc20/balanceof",method = RequestMethod.GET)
    public JsonResult balanceof(@RequestParam String address){
        String res = trc20Service.balanceof(address);
        log.info("{}",res);
        return new JsonResult(BigDecimal.valueOf(Integer.parseInt(JSONObject.parseObject(res).getJSONArray("constant_result").getString(0),16)).divide(BigDecimal.valueOf(1000000)));
    }

    @RequestMapping(value = "/trc20/getrawtransaction",method = RequestMethod.GET)
    public JsonResult getrawtransaction(@RequestParam String txid){
        JSONObject jsonObject = trc20Service.getrawtransaction(txid);
        return new JsonResult(jsonObject);

    }
}
