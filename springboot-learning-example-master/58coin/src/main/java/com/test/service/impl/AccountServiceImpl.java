package com.test.service.impl;

import com.test.dao.account.AccountDao;
import com.test.domain.Account;
import com.test.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    public Account findByUserId(int userId){
        return accountDao.findByUserId(userId);
    }

    public void depositAccount(int userId, int currencyId, BigDecimal available){
        accountDao.depositAccount(userId,currencyId,available);
    }

    public void updateAccount(int userId, int currencyId, BigDecimal available){
        accountDao.updateAccount(userId,currencyId,available);
    }
}
