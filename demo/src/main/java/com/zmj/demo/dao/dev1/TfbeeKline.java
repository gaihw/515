package com.zmj.demo.dao.dev1;

import com.zmj.demo.domain.dev1.Market1minBtcusdt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TfbeeKline {
    /**
     *     1）#{}：select * from t_user where uid=#{uid}
     * 　　2）${}：select * from t_user where uid= '${uid}'
     * @param idx
     * @return
     */

    @Select("SELECT idx,amount,vol,open,close,high,low  FROM `tfbee_kline`.`market_1min_btcusdt` WHERE `idx` = '${idx}' ")
    List<Market1minBtcusdt> getMarket1minBtcUsdt(@Param("idx") String idx);
}
