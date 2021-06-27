package com.zmj.demo.service.impl;

import com.zmj.demo.dao.allin.Allin102Dao;
import com.zmj.demo.domain.BaseDomain;
import com.zmj.demo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private Allin102Dao allin102Dao;

    @Override
    public List<BaseDomain> getList() {
        return allin102Dao.getList();
    }



}
