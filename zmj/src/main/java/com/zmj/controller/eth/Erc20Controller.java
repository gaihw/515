package com.zmj.controller.eth;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eth.Erc20Chain;
import com.zmj.domain.JsonResult;
import com.zmj.service.Erc20Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class Erc20Controller {

    @Autowired
    private Erc20Service erc20Service;

    @RequestMapping(value= "/erc20/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody Erc20Chain erc20Chain){
        HashMap hashMap = new HashMap();
        String res = erc20Service.transaction(erc20Chain);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value = "/erc20/getrawtransaction",method = RequestMethod.GET)
    public JsonResult getrawtransaction(@RequestParam(value = "txid") String txid){
        String res = erc20Service.getrawtransaction(txid);
        JSONObject jsonObject = JSONObject.parseObject(res).getJSONObject("result");
        jsonObject.put("blockNumber",Integer.parseInt(jsonObject.getString("blockNumber").substring(2),16));
        jsonObject.put("from",jsonObject.getString("from"));
        jsonObject.put("to","0x"+jsonObject.getString("input").substring(34,74));
//        jsonObject.put("value", BigDecimal.valueOf(Integer.parseInt(jsonObject.getString("input").substring(74),16)).divide(BigDecimal.valueOf(1000000)));
        jsonObject.put("value", jsonObject.getString("input").substring(74).replaceAll("^(0+)", ""));
        jsonObject.put("nonce",Integer.parseInt(jsonObject.getString("nonce").substring(2),16));
        jsonObject.put("contranctaddress",jsonObject.getString("to"));
        jsonObject.put("blockHash",jsonObject.getString("blockHash"));

        log.info("{}",res);
        return new JsonResult(jsonObject);
    }
}
