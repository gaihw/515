package com.test.service;

import com.test.domain.Account;
import com.test.domain.User;

import java.math.BigDecimal;

/**
 * 业务逻辑
 */

public interface AccountService {
    /**
     * 查询用户账户信息
     *
     */
    Account findByUserId(int userId);
    /**
     * 充钱
     */
    void depositAccount(int userId, int currencyId, BigDecimal available);
    /**
     * 更新
     */
    void updateAccount(int userId, int currencyId, BigDecimal available);
}
