package com.zmj.controller.eth;


import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eth.EthChain;
import com.zmj.service.EthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Slf4j
@RestController
public class EthController {

    @Autowired
    private EthService ethService;

    @RequestMapping(value= "/eth/getAddPrivPub", method = RequestMethod.POST)
    public JSONObject getPublicKeys(@RequestBody EthChain ethChain){
        JSONObject res = ethService.getAddPrivPub(ethChain);
        log.info("{}",res);
        return res;
    }

    @RequestMapping(value= "/eth/getBlockNum", method = RequestMethod.POST)
    public BigInteger getBlockNum(@RequestBody EthChain ethChain){
        BigInteger res = ethService.getBlockNumber(ethChain);
        log.info("{}",res);
        return res;
    }

    @RequestMapping(value= "/eth/transaction", method = RequestMethod.POST)
    public String transaction(@RequestBody EthChain ethChain){
        String res = ethService.transaction(ethChain);
        log.info("{}",res);
        return res;
    }

}
