package com.test.dao.account;

import com.test.domain.Account;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface AccountDao {
    /**
     * 查询账户信息
     */
    Account findByUserId(@Param("userId" ) int userId);
    /**
     * 充钱
     */
    void depositAccount(@Param("userId") int userId,
                        @Param("currencyId") int currencyId,
                        @Param("available") BigDecimal available);
    /**
     * 更新
     */
    void updateAccount(@Param("userId") int userId,
                       @Param("currencyId") int currencyId,
                       @Param("available") BigDecimal available);
}
