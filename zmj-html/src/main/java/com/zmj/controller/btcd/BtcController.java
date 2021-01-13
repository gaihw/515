package com.zmj.controller.btcd;

import com.zmj.service.BtcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
public class BtcController {

    @Autowired
    private BtcService btcService;


    /**
     * 查询币种配置信息表
     * @return
     */
    @RequestMapping(value= "/btc/getblockcount", method = RequestMethod.GET)
    public String getblockcount(){
        String res = btcService.getblockcount();
        log.info("{}",res);
        return res;
    }

    @RequestMapping(value= "/btc/getpeerinfo", method = RequestMethod.GET)
    public String getpeerinfo(){
        String res = btcService.getpeerinfo();
        log.info("{}",res);
        return res;
    }
}
