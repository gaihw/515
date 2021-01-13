package com.zmj.controller.trx;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.JsonResult;
import com.zmj.domain.trx.TrxChain;
import com.zmj.service.TrxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class TrxController {

    @Autowired
    private TrxService trxService;

    @RequestMapping(value = "/trx/getnewaddress",method = RequestMethod.GET)
    public JsonResult getnewaddress(){

        return new JsonResult(trxService.generateAddress());
    }

    @RequestMapping(value = "/trx/transaction",method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody TrxChain trxChain){
        String res = trxService.createtransaction(trxChain);
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getString("txid"));
    }

    @RequestMapping(value = "/trx/validateaddress",method = RequestMethod.GET)
    public JsonResult validateaddress(@RequestParam String address){
        return new JsonResult(trxService.validateAddress(address));
    }

    @RequestMapping(value = "/trx/gettransactioninfobyid",method = RequestMethod.GET)
    public JsonResult gettransactioninfobyid(@RequestParam String txid){
        return new JsonResult(trxService.gettransactioninfobyid(txid));
    }

    @RequestMapping(value = "/trx/gettransactionbyid",method = RequestMethod.GET)
    public JsonResult gettransactionbyid(@RequestParam String txid){
        return new JsonResult(trxService.gettransactionbyid(txid));
    }

    @RequestMapping(value = "/trx/getrawtransaction",method = RequestMethod.GET)
    public JsonResult getrawtransaction(@RequestParam String txid){
        return new JsonResult(trxService.getrawtransaction(txid));

    }

    @RequestMapping(value = "/trx/getaccountresource",method = RequestMethod.GET)
    public JsonResult getaccountresource(@RequestParam String address){
        return new JsonResult(trxService.getaccountresource(address));
    }

    @RequestMapping(value = "/trx/getcontract",method = RequestMethod.GET)
    public JsonResult getcontract(@RequestParam String address){
        return new JsonResult(trxService.getcontract(address));
    }

    @RequestMapping(value = "/trx/balanceof",method = RequestMethod.GET)
    public JsonResult getaccount(@RequestParam String address){
        String res = trxService.getaccount(address);
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getBigDecimal("balance").divide(BigDecimal.valueOf(1000000)));
    }

    @RequestMapping(value = "/trx/freezebalance",method = RequestMethod.POST)
    public JsonResult freezebalance(@RequestBody TrxChain trxChain){
        return new JsonResult(trxService.freezebalance(trxChain));
    }

    @RequestMapping(value = "/trx/createaccount",method = RequestMethod.POST)
    public JsonResult createaccount(@RequestBody TrxChain trxChain){
        return new JsonResult(trxService.createaccount(trxChain));
    }
}
