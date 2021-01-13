package com.zmj.service.impl;

import com.zmj.dao.currency.TbCurrencyInfoDao;
import com.zmj.domain.currency.TbCurrencyInfo;
import com.zmj.service.TbCurrencyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCurrencyInfoServiceImpl implements TbCurrencyInfoService {

    @Autowired
    private TbCurrencyInfoDao tbCurrencyInfoDao;

    @Override
    public List<TbCurrencyInfo> getCurrencyInfo(){

        return tbCurrencyInfoDao.getCurrencyInfo();
    }

    @Override
    public int updateCurrencyRpcGet(int currencyId,int statusFlag){
        return tbCurrencyInfoDao.updateCurrencyRpcByCurIdGet(currencyId,statusFlag);
    }

    @Override
    public int updateCurrencyRpcPost(TbCurrencyInfo tbCurrencyInfo){
        return tbCurrencyInfoDao.updateCurrencyRpcByCurIdPost(tbCurrencyInfo);
    }

}
