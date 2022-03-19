package com.zmj.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.auto.PlatformChain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformService {
    /**
     * 添加项目
     *
     */
    int addPlatform(PlatformChain platformChain, String creator);

    /**
     * 查询项目
     */
    List<PlatformChain> getPlatformList(JSONObject jsonObject);


    /**
     * 获取平台总条数
     * @param jsonObject
     * @return
     */
    int getPlatformAcount(JSONObject jsonObject);

    /**
     * 逻辑删除数据
     * @param id
     * @return
     */
    int deletePlatformData(int id);

    /**
     * 编辑项目
     * @param projectChain
     * @param creator
     * @return
     */
    int editPlatform(PlatformChain projectChain,String creator);

    /**
     * 平台列表
     * @return
     */
    List<String> listPlatform();

    /**
     * 项目列表
     * @param platform
     * @return
     */
    List<String> listProject(@Param("platform") String platform);

    /**
     * 模块列表
     * @param platform
     * @param project
     * @return
     */
    List<PlatformChain> listModule(@Param("platform") String platform, @Param("project") String project);
}
