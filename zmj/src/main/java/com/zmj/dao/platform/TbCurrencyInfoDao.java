package com.zmj.dao.platform;

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
    List<TbCurrencyInfo> getCurrencyInfo(@Param("currencyId") String currencyId,@Param("page") int pageNum, @Param("limit") int pageSize);

    /**
     * 根据币种修改rpc配置
     */
    int updateCurrencyRpcByCurIdGet(@Param("currencyId") String currencyId,@Param("statusFlag") int statusFlag);
    int updateCurrencyRpcByCurIdPost(@Param("tbCurrencyInfo") TbCurrencyInfo tbCurrencyInfo);

    /**
     * 插入数据
     */
    int insertCurrency(@Param("tbCurrencyInfo") TbCurrencyInfo tbCurrencyInfo);

    /**
     * 查询总条数
     */
    int selectCurrencyCount();
}
