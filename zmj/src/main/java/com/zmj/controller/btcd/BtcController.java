package com.zmj.controller.btcd;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.btcd.BtcChain;
import com.zmj.domain.btcd.BtcRestChain;
import com.zmj.domain.JsonResult;
import com.zmj.service.BtcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class BtcController {

    @Autowired
    private BtcService btcService;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 查询币种配置信息表
     * @return
     */
    @RequestMapping(value= "/btc/getblockcount", method = RequestMethod.GET)
    public JsonResult getblockcount(){
        String url = "http://192.168.112.170:19112/btc/getblockcount";
        BtcRestChain btcRestChain = this.restTemplate.getForObject(url,BtcRestChain.class);
        System.out.println("btcRestChain==="+btcRestChain);
        return new JsonResult(btcRestChain);
    }

    @RequestMapping(value= "/btc/getpeerinfo", method = RequestMethod.GET)
    public JsonResult getpeerinfo(){
        String res = btcService.getpeerinfo();
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/btc/getrawtransaction", method = RequestMethod.GET)
    public JsonResult getrawtransaction(String txId){
        String res = btcService.getrawtransaction(txId);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/btc/getnewaddress", method = RequestMethod.GET)
    public JsonResult getnewaddress(){
        String res = btcService.getnewaddress();
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/btc/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody BtcChain btcChain){
        String res = btcService.sendtoaddress(btcChain);
//        String res = "{\"result\":\"22b5d9802d131ac035d247b15baac918cdf190ef47457c1aa42fe97aab17823e\",\"error\":null,\"id\":\"curltest\"}";
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getString("result"));
    }
}
