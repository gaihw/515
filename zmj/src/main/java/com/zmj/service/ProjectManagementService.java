package com.zmj.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.interfaceTest.ProjectApiChain;
import com.zmj.domain.interfaceTest.ProjectCaseChain;
import com.zmj.domain.interfaceTest.ProjectChain;

import java.util.List;

public interface ProjectManagementService {

    /**
     * 添加项目
     *
     */
    int addNewProject(ProjectChain projectChain,String creator);

    /**
     * 查询项目
     */
    List<ProjectChain> list(JSONObject jsonObject);

    int acount(JSONObject jsonObject);

    /**
     * 逻辑删除数据
     * @param id
     * @return
     */
    int deleteData(int id);

    /**
     * 编辑项目
     * @param projectChain
     * @param creator
     * @return
     */
    int edit(ProjectChain projectChain,String creator);


    /**
     * 查询接口
     */
    List<ProjectApiChain> apiList(JSONObject jsonObject);

    /**
     * 查询平台
     * @return
     */
    List<String> apiListPlatform();

    /**
     * 查询项目
     * @param platform
     * @return
     */
    List<String> apiListProject(String platform);

    /**
     * 查询模块
     * @param platform
     * @param project
     * @return
     */
    List<ProjectApiChain> apiListModule(String platform,String project);

    /**
     * 新增接口
     * @param projectApiChain
     * @param creator
     * @return
     */
    int addNewApi(ProjectApiChain projectApiChain,String creator);

    /**
     * 编辑接口
     * @param projectApiChain
     * @param creator
     * @return
     */
    int editApi(ProjectApiChain projectApiChain,String creator);

    /**
     * 接口总数
     * @param jsonObject
     * @return
     */
    int apiAcount(JSONObject jsonObject);

    /**
     * 删除接口
     * @param id
     * @return
     */
    int deleteApiData(int id);

    /**
     * 新增用例
     * @param projectCaseChain
     * @param creator
     * @return
     */
    int addNewCase(ProjectCaseChain projectCaseChain, String creator);

    /**
     * 编辑用例
     * @param projectCaseChain
     * @param creator
     * @return
     */
    int editCase(ProjectCaseChain projectCaseChain,String creator);

    /**
     * 用例个数
     * @param jsonObject
     * @return
     */
    int caseAcount(JSONObject jsonObject);

    /**
     * 删除用例
     * @param id
     * @return
     */
    int deleteCaseData(int id);
    /**
     * 查询用例
     */
    List<ProjectCaseChain> caseList(JSONObject jsonObject);

    /**
     * 查询接口名称
     */
    List<ProjectCaseChain> caseListName(String platform,String project,String module);

    /**
     * 用例详情查询
     * @param id
     * @return
     */
    List<ProjectCaseChain> caseInfo(int id);

}
