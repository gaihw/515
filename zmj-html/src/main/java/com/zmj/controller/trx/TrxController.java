package com.zmj.controller.trx;

import com.zmj.domain.trx.TrxChain;
import com.zmj.service.TrxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class TrxController {

    @Autowired
    private TrxService trxService;

    @RequestMapping(value = "/trx/getnewaddress",method = RequestMethod.GET)
    public String getnewaddress(){

        return trxService.generateAddress();
    }

    @RequestMapping(value = "/trx/transaction",method = RequestMethod.POST)
    public String transaction(@RequestBody TrxChain trxChain){
        return trxService.createtransaction(trxChain);
    }

    @RequestMapping(value = "/trx/validateaddress",method = RequestMethod.GET)
    public String validateaddress(@RequestParam String address){
        return trxService.validateAddress(address);
    }

    @RequestMapping(value = "/trx/gettransactioninfobyid",method = RequestMethod.GET)
    public String gettransactioninfobyid(@RequestParam String txid){
        return trxService.gettransactioninfobyid(txid);
    }

    @RequestMapping(value = "/trx/gettransactionbyid",method = RequestMethod.GET)
    public String gettransactionbyid(@RequestParam String txid){
        return trxService.gettransactionbyid(txid);
    }

    @RequestMapping(value = "/trx/getaccountresource",method = RequestMethod.GET)
    public String getaccountresource(@RequestParam String address){
        return trxService.getaccountresource(address);
    }

    @RequestMapping(value = "/trx/getcontract",method = RequestMethod.GET)
    public String getcontract(@RequestParam String address){
        return trxService.getcontract(address);
    }

    @RequestMapping(value = "/trx/getaccount",method = RequestMethod.GET)
    public String getaccount(@RequestParam String address){
        return trxService.getaccount(address);
    }

    @RequestMapping(value = "/trx/freezebalance",method = RequestMethod.POST)
    public String freezebalance(@RequestBody TrxChain trxChain){
        return trxService.freezebalance(trxChain);
    }

    @RequestMapping(value = "/trx/createaccount",method = RequestMethod.POST)
    public String createaccount(@RequestBody TrxChain trxChain){
        return trxService.createaccount(trxChain);
    }
}
