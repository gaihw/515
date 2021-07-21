package com.zmj.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.InterfaceChain;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 下载模板
     * @param response
     */
    void downloadExcel(HttpServletResponse response);

    /**
     * 上传用例
     */
    JsonResult uploadExcel(MultipartFile excelFile, String creator);
	
	/**
	 *用例执行
	 */
	JsonResult caseExecute(List<Integer> caseList);

    /**
     * 场景执行
     * @param sceneparams
     * @return
     */
	JsonResult sceneExecute(JSONObject sceneparams);
	
}
