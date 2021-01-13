package com.zmj.controller.eos;


import com.zmj.domain.eos.EosChain;
import com.zmj.service.EosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class EosController {

    @Autowired
    private EosService eosService;

    @RequestMapping(value= "/eos/getPublicKeys", method = RequestMethod.GET)
    public String getPublicKeys(){
        String res = eosService.get_public_keys();
        log.info("{}",res);
        return res;
    }

    @RequestMapping(value= "/eos/unlock", method = RequestMethod.POST)
    public String unlock(@RequestBody EosChain eosChain){
        String res = eosService.unlock(eosChain);
        log.info("{}",res);
        return res;
    }

}
