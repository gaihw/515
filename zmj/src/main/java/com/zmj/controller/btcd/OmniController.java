package com.zmj.controller.btcd;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.btcd.OmniChain;
import com.zmj.domain.JsonResult;
import com.zmj.service.OmniService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class OmniController {

    @Autowired
    private OmniService OmniService;

    @RequestMapping(value= "/omni/transaction", method = RequestMethod.POST)
    public JsonResult transactino(@RequestBody OmniChain omniChain){
        String res = OmniService.transaction(omniChain);
        log.info("{}",res);
        return new JsonResult(JSONObject.parseObject(res).getString("result"));
    }
}
