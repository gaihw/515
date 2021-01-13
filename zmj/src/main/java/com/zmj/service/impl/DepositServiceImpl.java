package com.zmj.service.impl;

import com.zmj.dao.orther.deposit.DepositDao;
import com.zmj.domain.orther.DepositChain;
import com.zmj.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositServiceImpl implements DepositService {

    @Autowired
    private DepositDao depositDao;

    @Override
    public List<DepositChain> getDeposit() {
        return depositDao.getDeposit();
    }
}
