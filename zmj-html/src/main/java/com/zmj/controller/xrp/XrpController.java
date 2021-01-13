package com.zmj.controller.xrp;


import com.zmj.domain.xrp.XrpChain;
import com.zmj.service.XrpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class XrpController {


    @Autowired
    private XrpService xrpService;


    @RequestMapping(value= "/xrp/transaction", method = RequestMethod.POST)
    public String transaction(@RequestBody XrpChain xrpChain){
        String res = xrpService.transaction(xrpChain);
        log.info("{}",res);
        return res;
    }


    @RequestMapping(value= "/xrp/accountInfo", method = RequestMethod.GET)
    public String getAccountInfo(@RequestParam String account){
        String res = xrpService.getAccountInfo(account);
        log.info("{}",res);
        return res;
    }

    @RequestMapping(value= "/xrp/transactionInfo", method = RequestMethod.GET)
    public String getTransaction(@RequestParam String transaction){
        String res = xrpService.getTransaction(transaction);
        log.info("{}",res);
        return res;
    }

}
