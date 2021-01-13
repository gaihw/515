package com.zmj.controller.eos;


import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eos.EosChain;
import com.zmj.domain.JsonResult;
import com.zmj.service.EosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class EosController {

    @Autowired
    private EosService eosService;

    @RequestMapping(value= "/eos/getPublicKeys", method = RequestMethod.GET)
    public JsonResult getPublicKeys(){
        return new JsonResult(eosService.get_public_keys());
    }

    @RequestMapping(value = "/eos/open",method = RequestMethod.GET)
    public JsonResult open(@RequestParam("testcreate") String testcreate){
        return new JsonResult(eosService.open(testcreate));
    }

    @RequestMapping(value= "/eos/unlock", method = RequestMethod.POST)
    public JsonResult unlock(@RequestBody EosChain eosChain){
        String res = eosService.unlock(eosChain);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value = "/eos/getblockcount" ,method = RequestMethod.GET)
    public JsonResult getblock(){
        String head_block_num = JSONObject.parseObject(eosService.getblock()).getString("head_block_num");
        log.info("{}",head_block_num);
        return new JsonResult(JSONObject.parseObject(eosService.getblock()).getString("head_block_num"));
    }

    @RequestMapping(value= "/eos/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody EosChain eosChain){
        String res = eosService.transaction(eosChain);
//        String res = "{\"result\":\"22b5d9802d131ac035d247b15baac918cdf190ef47457c1aa42fe97aab17823e\",\"error\":null,\"id\":\"curltest\"}";
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getString("transaction_id"));
    }
}
