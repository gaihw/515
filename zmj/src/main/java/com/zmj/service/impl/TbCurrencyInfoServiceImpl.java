package com.zmj.service.impl;

import com.zmj.dao.platform.TbCurrencyInfoDao;
import com.zmj.domain.currency.TbCurrencyInfo;
import com.zmj.service.TbCurrencyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbCurrencyInfoServiceImpl implements TbCurrencyInfoService {

    @Autowired
    private TbCurrencyInfoDao tbCurrencyInfoDao;

    @Override
    public List<TbCurrencyInfo> getCurrencyInfo(String currencyId,int page,int limit){

        return tbCurrencyInfoDao.getCurrencyInfo(currencyId,page,limit);
    }

    @Override
    public int updateCurrencyRpcGet(String currencyId,int statusFlag){
        return tbCurrencyInfoDao.updateCurrencyRpcByCurIdGet(currencyId,statusFlag);
    }

    @Override
    public int updateCurrencyRpcPost(TbCurrencyInfo tbCurrencyInfo){
        return tbCurrencyInfoDao.updateCurrencyRpcByCurIdPost(tbCurrencyInfo);
    }

    @Override
    public int insertCurrency(TbCurrencyInfo tbCurrencyInfo) {
        return tbCurrencyInfoDao.insertCurrency(tbCurrencyInfo);
    }

    @Override
    public int selectCurrencyCount() {
        return tbCurrencyInfoDao.selectCurrencyCount();
    }

}
