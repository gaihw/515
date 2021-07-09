package com.zmj.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.InterfaceChain;

import java.util.List;

public interface CaseService {

    /**
     * 查询
     */
    List<CaseChain> list(JSONObject jsonObject);

    /**
     * 添加
     */
    int add(CaseChain caseChain,String creator);

    /**
     * 删除
     */
    int delete(int id);

    /**
     * 修改
     */
    int edit(CaseChain caseChain,String creator);

    /**
     * 总条数
     */
    int acount(JSONObject jsonObject);
}
