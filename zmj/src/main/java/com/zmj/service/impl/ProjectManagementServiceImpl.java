package com.zmj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.dao.platform.ProjectDao;
import com.zmj.domain.interfaceTest.ProjectApiChain;
import com.zmj.domain.interfaceTest.ProjectCaseChain;
import com.zmj.domain.interfaceTest.ProjectChain;
import com.zmj.service.ProjectManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectManagementServiceImpl implements ProjectManagementService {

    @Resource
    private ProjectDao projectDao;

    @Override
    public int addNewProject(ProjectChain projectChain,String creator) {
        return projectDao.addNewProject(projectChain.getPlatform(),projectChain.getProject(),
                projectChain.getModule(),projectChain.getIp(),projectChain.getState(),creator);
    }

    @Override
    public List<ProjectChain> list(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String ip = jsonObject.getString("ip");
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        return projectDao.list(platform,project,module,ip,limit*(page-1),limit);
    }

    @Override
    public int acount(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String ip = jsonObject.getString("ip");
        return projectDao.acount(platform,project,module,ip);
    }

    @Override
    public int deleteData(int id) {
        return projectDao.deleteData(id);
    }

    @Override
    public int edit(ProjectChain projectChain, String creator) {
        return projectDao.edit(projectChain.getId(),projectChain.getPlatform(),projectChain.getProject(),
                projectChain.getModule(),projectChain.getIp(),projectChain.getState(),creator);
    }

    @Override
    public List<ProjectApiChain> apiList(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String name = jsonObject.getString("name");
        String path = jsonObject.getString("path");
        String method = jsonObject.getString("method");
        String param = jsonObject.getString("param");
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        return projectDao.apiList(platform,project,module,name,path,method,param,limit*(page-1),limit);
    }

    @Override
    public List<String> apiListPlatform() {
        return projectDao.apiListPlatform();
    }

    @Override
    public List<String> apiListProject(String platform) {
        return projectDao.apiListProject(platform);
    }

    @Override
    public List<ProjectApiChain> apiListModule(String platform,String project) {
        return projectDao.apiListModule(platform,project);
    }

    @Override
    public int addNewApi(ProjectApiChain projectApiChain, String creator) {
        return projectDao.addNewApi(projectApiChain.getPlatformId(),projectApiChain.getPlatform(),projectApiChain.getProject(),
                projectApiChain.getModule(),projectApiChain.getName(),projectApiChain.getPath(),
                projectApiChain.getMethod(),projectApiChain.getParam(),projectApiChain.getIsToken(),projectApiChain.getState(),creator);
    }

    @Override
    public int editApi(ProjectApiChain projectApiChain, String creator) {
        return projectDao.editApi(projectApiChain.getId(),
                projectApiChain.getPlatformId(),
                projectApiChain.getPlatform(),
                projectApiChain.getProject(),
                projectApiChain.getModule(),
                projectApiChain.getName(),
                projectApiChain.getPath(),
                projectApiChain.getMethod(),
                projectApiChain.getParam(),
                projectApiChain.getIsToken(),
                projectApiChain.getState(),
                creator);
    }
    @Override
    public int apiAcount(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String name = jsonObject.getString("name");
        String path = jsonObject.getString("path");
        String method = jsonObject.getString("method");
        String param = jsonObject.getString("param");
        return projectDao.apiAcount(platform,project,module,name,path,method,param);
    }

    @Override
    public int deleteApiData(int id) {
        return projectDao.deleteApiData(id);
    }

    @Override
    public int addNewCase(ProjectCaseChain projectCaseChain, String creator) {
        return projectDao.addNewCase(projectCaseChain.getApiId(),projectCaseChain.getPlatform(),projectCaseChain.getProject(),
                projectCaseChain.getModule(),projectCaseChain.getName(),projectCaseChain.getParam(),
                projectCaseChain.getAssertData(),projectCaseChain.getState(),creator);
    }

    @Override
    public int editCase(ProjectCaseChain projectCaseChain, String creator) {
        return projectDao.editCase(projectCaseChain.getId(),
                projectCaseChain.getApiId(),
                projectCaseChain.getPlatform(),
                projectCaseChain.getProject(),
                projectCaseChain.getModule(),
                projectCaseChain.getName(),
                projectCaseChain.getParam(),
                projectCaseChain.getAssertData(),
                projectCaseChain.getState(),
                creator);
    }

    @Override
    public int caseAcount(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String name = jsonObject.getString("name");
        String param = jsonObject.getString("param");
        String assertData = jsonObject.getString("assertData");
        return projectDao.caseAcount(platform,project,module,name,param,assertData);
    }

    @Override
    public int deleteCaseData(int id) {
        return projectDao.deleteCaseData(id);
    }

    @Override
    public List<ProjectCaseChain> caseList(JSONObject jsonObject) {
        String platform = jsonObject.getString("platform");
        String project = jsonObject.getString("project");
        String module = jsonObject.getString("module");
        String name = jsonObject.getString("name");
        String path = jsonObject.getString("param");
        String method = jsonObject.getString("assertData");
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        return projectDao.caseList(platform,project,module,name,path,method,limit*(page-1),limit);
    }

    @Override
    public List<ProjectCaseChain> caseListName(String platform, String project, String module) {
        return projectDao.caseListName(platform,project,module);
    }

    @Override
    public List<ProjectCaseChain> caseInfo(int id) {
        return projectDao.caseInfo(id);
    }
}
