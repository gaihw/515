package com.zmj.dao.btcd;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

public interface BtcDao {

    /**
     * 币种的rpc节点地址
     */
//    Map<String, Object> getRpcUrl(@Param("currencyId") int currencyId);
    String getRpcUrl(@Param("currencyId") int currencyId);

}
