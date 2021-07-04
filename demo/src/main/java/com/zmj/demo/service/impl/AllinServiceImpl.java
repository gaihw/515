package com.zmj.demo.service.impl;

import com.zmj.demo.dao.allin.Allin102Dao;
import com.zmj.demo.domain.allin.Allin102Domain;
import com.zmj.demo.service.AllinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllinServiceImpl implements AllinService {

    @Autowired
    private Allin102Dao allin102Dao;

    @Override
    public List<Allin102Domain> getList() {
        return allin102Dao.getList();
    }



}
