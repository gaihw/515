package com.zmj.dao.currency;

import com.zmj.domain.currency.TbCurrencyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TbCurrencyInfoDao {
    /**
     * 查询币种配置信息表
     */
    List<TbCurrencyInfo> getCurrencyInfo();

    /**
     * 根据币种修改rpc配置
     */
    int updateCurrencyRpcByCurIdGet(@Param("currencyId") int currencyId,@Param("statusFlag") int statusFlag);
    int updateCurrencyRpcByCurIdPost(@Param("tbCurrencyInfo") TbCurrencyInfo tbCurrencyInfo);
}
