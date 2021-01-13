package com.zmj.service;

import com.zmj.domain.currency.TbCurrencyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TbCurrencyInfoService {
    /**
     * 查询币种配置信息
     */
    List<TbCurrencyInfo> getCurrencyInfo(String currencyId,int page,int limit);

    /**
     * 修改币种的rpc配置
     */
    int updateCurrencyRpcGet(String currencyId,int statusFlag);
    int updateCurrencyRpcPost(TbCurrencyInfo tbCurrencyInfo);

    /**
     * 插入数据
     */
    int insertCurrency(TbCurrencyInfo tbCurrencyInfo);

    /**
     * 查询总条数
     */
    int selectCurrencyCount();
}
