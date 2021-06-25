package com.zmj.demo.service.impl;

import com.zmj.demo.dao.BaseDao;
import com.zmj.demo.domain.BaseDomain;
import com.zmj.demo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;

    @Override
    public List<BaseDomain> getList() {
        return baseDao.getList();
    }



}
