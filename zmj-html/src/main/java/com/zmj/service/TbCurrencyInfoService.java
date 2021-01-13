package com.zmj.service;

import com.zmj.domain.currency.TbCurrencyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbCurrencyInfoService {
    /**
     * 查询币种配置信息
     */
    List<TbCurrencyInfo> getCurrencyInfo();

    /**
     * 修改币种的rpc配置
     */
    int updateCurrencyRpcGet(int currencyId,int statusFlag);
    int updateCurrencyRpcPost(TbCurrencyInfo tbCurrencyInfo);

}
