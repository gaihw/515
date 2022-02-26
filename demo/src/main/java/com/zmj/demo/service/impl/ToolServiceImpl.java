package com.zmj.demo.service.impl;

import com.zmj.demo.dao.test.SmsEmailCode;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import com.zmj.demo.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private SmsEmailCode smsEmailCode;

    @Override
    public List<SmsEmailcodeDomain> getList() {
        return smsEmailCode.getList();
    }



}
