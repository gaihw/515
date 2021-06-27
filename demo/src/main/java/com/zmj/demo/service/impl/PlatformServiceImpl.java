package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.dao.auto.PlatformDao;
import com.zmj.demo.domain.auto.PlatformChain;
import com.zmj.demo.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformDao platformDao;

    @Override
    public int addPlatform(PlatformChain PlatformChain, String creator) {
        return 0;
    }

    @Override
    public List<PlatformChain> getPlatformList(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String ip = jsonObject.getString("ip");
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        return platformDao.list(platform,project,module,ip,limit*(page-1),limit);

    }

    @Override
    public int getPlatformAcount(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String ip = jsonObject.getString("ip");
        return platformDao.acount(platform,project,module,ip);
    }

    @Override
    public int deletePlatformData(int id) {
        return 0;
    }

    @Override
    public int editPlatform(PlatformChain projectChain, String creator) {
        return 0;
    }
}
