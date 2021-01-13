package com.zmj.controller.orther;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String test(@RequestParam("currencyId") String currencyId,
                       @RequestParam("userId") String userId,
                       @RequestParam("siteId") String siteId){
        return currencyId+"==="+userId+"==="+siteId;
    }
}
