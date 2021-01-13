package com.zmj.controller.currency;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmj.domain.currency.TbCurrencyInfo;
import com.zmj.domain.JsonResult;
import com.zmj.service.TbCurrencyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TbCurrencyInfoController {

    @Autowired
    private TbCurrencyInfoService tbCurrencyInfoService;

    /**
     * 查询币种配置信息表
     * @return
     */
    @RequestMapping(value= "/currency/info", method = RequestMethod.GET)
    public JsonResult getCurrencyInfo(@RequestParam(value = "currencyId")String currencyId
                                , @RequestParam(value = "page")Integer page
                                , @RequestParam(value = "limit")Integer limit){
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = tbCurrencyInfoService.selectCurrencyCount();
        PageInfo<List<TbCurrencyInfo>> pageInfo = new PageInfo(tbCurrencyInfoService.getCurrencyInfo(currencyId,limit*(page-1),limit));
//        List<TbCurrencyInfo> list = tbCurrencyInfoService.getCurrencyInfo(currencyId);
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }

    /**
     * get方法更新币种
     * @param currencyId
     * @param statusFlag
     * @return
     */
    @RequestMapping(value= "/currency/updateCurRpcGet", method = RequestMethod.GET)
    public JsonResult updateCurRpcGet(@RequestParam(value = "currencyId",required=true)String currencyId,
                            @RequestParam(value = "statusFlag",required=true)int statusFlag){
        int row = tbCurrencyInfoService.updateCurrencyRpcGet(currencyId,statusFlag);
        if (row == 1){
            return new JsonResult(0,"success");
        }
        return null;
    }

    /**
     * post方法更新币种
     * @param tbCurrencyInfo
     * @return
     */
    @RequestMapping(value= "/currency/edit", method = RequestMethod.POST)
    public JsonResult updateCurRpcPost(@RequestBody TbCurrencyInfo tbCurrencyInfo){
        int row = tbCurrencyInfoService.updateCurrencyRpcPost(tbCurrencyInfo);
        System.out.println(row);
        if (row == 1){
            return new JsonResult(0,"success");
        }
        return null;
    }

    /**
     * post方法添加币种
     * @param tbCurrencyInfo
     * @return
     */
    @RequestMapping(value= "/currency/add", method = RequestMethod.POST)
    public JsonResult insertCurrency(@RequestBody TbCurrencyInfo tbCurrencyInfo){
        int row = tbCurrencyInfoService.insertCurrency(tbCurrencyInfo);
        if (row == 1){
            return new JsonResult(0,"success");
        }
        return null;
    }
}
