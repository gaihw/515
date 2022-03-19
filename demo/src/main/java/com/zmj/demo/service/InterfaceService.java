package com.zmj.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.auto.InterfaceChain;

import java.util.List;

public interface InterfaceService {

    /**
     * 查询
     */
    List<InterfaceChain> list(JSONObject jsonObject);

    /**
     * 添加
     */
    int add(InterfaceChain interfaceChain,String creator);

    /**
     * 删除
     */
    int delete(int id);

    /**
     * 修改
     */
    int edit(InterfaceChain interfaceChain,String creator);

    /**
     * 总条数
     */
    int acount(JSONObject jsonObject);
}
