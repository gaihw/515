package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.dao.demodata.auto.PlatformDao;
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
    public int addPlatform(PlatformChain platformChain, String creator) {
        String platform = platformChain.getPlatform();
        String project = platformChain.getProject();
        String module = platformChain.getModule();
        String ip = platformChain.getIp();
        return platformDao.add(platform,project,module,ip,"test",creator);
    }

    @Override
    public List<PlatformChain> getPlatformList(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String ip = jsonObject.getString("ip");
        Integer page = jsonObject.getInteger("page")-1;
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

        return platformDao.delete(id);
    }

    @Override
    public int editPlatform(PlatformChain projectChain, String creator) {
        return platformDao.edit(projectChain.getId()
                ,projectChain.getPlatform()
                ,projectChain.getProject()
                ,projectChain.getModule()
                ,projectChain.getIp()
                ,projectChain.getState()
                ,projectChain.getCreator());
    }
}
