package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.dao.demodata.auto.InterfaceDao;
import com.zmj.demo.domain.auto.InterfaceChain;
import com.zmj.demo.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InterfaceServiceImpl implements InterfaceService {

    @Autowired
    private InterfaceDao interfaceDao;


    @Override
    public List<InterfaceChain> list(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String path = jsonObject.getString("path");
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        return interfaceDao.list(name,path,limit*(page-1),limit);
    }


    @Override
    public int acount(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String path = jsonObject.getString("path");
        return interfaceDao.acount(name,path);
    }

    @Override
    public int add(InterfaceChain interfaceChain, String creator) {

        Integer platformManageID = interfaceChain.getPlatformManageID();
        String name = interfaceChain.getName();
        String path = interfaceChain.getPath();
        String method = interfaceChain.getMethod();
        String contentType= interfaceChain.getContentType();
        String state = interfaceChain.getState();

        return interfaceDao.add(platformManageID,name,path,method,contentType,state,creator);
    }

    @Override
    public int delete(int id) {
        return interfaceDao.delete(id);
    }

    @Override
    public int edit(InterfaceChain interfaceChain, String creator) {

        Integer id = interfaceChain.getId();
        Integer platformManageID = interfaceChain.getPlatformManageID();
        String name = interfaceChain.getName();
        String path = interfaceChain.getPath();
        String method = interfaceChain.getMethod();
        String contentType= interfaceChain.getContentType();
        String state = interfaceChain.getState();

        return interfaceDao.edit(id,platformManageID,name,path,method,contentType,state,creator);
    }
}
