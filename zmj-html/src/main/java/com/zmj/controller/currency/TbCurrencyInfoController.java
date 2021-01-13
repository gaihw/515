package com.zmj.controller.currency;

import com.zmj.domain.currency.TbCurrencyInfo;
import com.zmj.service.TbCurrencyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TbCurrencyInfoController {

    @Autowired
    private TbCurrencyInfoService tbCurrencyInfoService;

    /**
     * 查询币种配置信息表
     * @return
     */
    @RequestMapping(value= "/currency/tbCurrencyInfo", method = RequestMethod.GET)
    public List<TbCurrencyInfo> getCurrencyInfo(){
        List<TbCurrencyInfo> list = tbCurrencyInfoService.getCurrencyInfo();
        log.info("{}",list);
        return list;
    }

    /**
     * get方法更新币种
     * @param currencyId
     * @param statusFlag
     * @return
     */
    @RequestMapping(value= "/currency/updateCurRpcGet", method = RequestMethod.GET)
    public Map updateCurRpcGet(@RequestParam(value = "currencyId",required=true)int currencyId,
                            @RequestParam(value = "statusFlag",required=true)int statusFlag){
        int row = tbCurrencyInfoService.updateCurrencyRpcGet(currencyId,statusFlag);
        Map<String,String> map = new HashMap<>();
        if (row == 1){
            map.put("code","0");
            map.put("data","{}");
            map.put("msg","success");
            log.info("{}",map);
            return map;
        }
        log.info("{}",map);
        return null;
    }

    /**
     * post方法更新币种
     * @param tbCurrencyInfo
     * @return
     */
    @RequestMapping(value= "/currency/updateCurRpc", method = RequestMethod.POST)
    public Map updateCurRpcPost(@RequestBody TbCurrencyInfo tbCurrencyInfo){
        int row = tbCurrencyInfoService.updateCurrencyRpcPost(tbCurrencyInfo);
        Map<String,String> map = new HashMap<>();
        if (row == 1){
            map.put("code","0");
            map.put("data","{}");
            map.put("msg","success");
            log.info("{}",map);
            return map;
        }
        log.info("{}",map);
        return null;
    }
}
