package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtils;
import com.zmj.demo.common.SqlUtils;
import com.zmj.demo.common.excel.ExcelUtils;
import com.zmj.demo.dao.auto.CaseDao;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.CaseExcelChain;
import com.zmj.demo.domain.auto.CaseExecuteChain;
import com.zmj.demo.enums.MessageEnum;
import com.zmj.demo.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CaseServiceImpl implements CaseService {

    @Autowired
    private CaseDao caseDao;

    @Autowired
    private SqlUtils sqlUtils;

    @Autowired
    private ExcelUtils excelUtils;

    @Autowired
    private HttpUtils httpUtils;

    @Override
    public List<CaseChain> list(JSONObject jsonObject) {
        String caseName = jsonObject.getString("caseName");
        String interfaceManageID = jsonObject.getString("interfaceManageID");
        Integer isSuccess = jsonObject.getInteger("isSuccess");
        Integer page = jsonObject.getInteger("page")-1;
        Integer limit = jsonObject.getInteger("limit");
        return caseDao.list(caseName,interfaceManageID,isSuccess,page,limit);
    }

    @Override
    public int add(CaseChain caseChain, String creator) {
        Integer interfaceManageID = caseChain.getInterfaceManageID();
        String caseName = caseChain.getCaseName();
        String headerData = caseChain.getHeaderData();
        String paramData = caseChain.getParamData();
        String assertType = caseChain.getAssertType();
        String assertData = caseChain.getAssertData();
        String state = caseChain.getState();
        return caseDao.add(interfaceManageID,caseName,headerData,paramData,assertType,assertData,state,creator);
    }

    @Override
    public int delete(int id) {
        return caseDao.delete(id);
    }

    @Override
    public int edit(CaseChain caseChain, String creator) {
        Integer id = caseChain.getId();
        Integer interfaceManageID = caseChain.getInterfaceManageID();
        String caseName = caseChain.getCaseName();
        String headerData = caseChain.getHeaderData();
        String paramData = caseChain.getParamData();
        String assertType = caseChain.getAssertType();
        String assertData = caseChain.getAssertData();
        String state = caseChain.getState();
        return caseDao.edit(id,interfaceManageID,caseName,headerData,paramData,assertType,assertData,state,creator);
    }

    @Override
    public int acount(JSONObject jsonObject) {
        Integer interfaceManageID = jsonObject.getInteger("interfaceManageID");
        String caseName = jsonObject.getString("caseName");
        Integer isSuccess = jsonObject.getInteger("isSuccess");
        return caseDao.acount(caseName,interfaceManageID,isSuccess);
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        excelUtils.downloadExcel(response);
    }

    @Override
    public JsonResult uploadExcel(MultipartFile excelFile, String creator) {
        List excel_data = excelUtils.uploadExcel(excelFile,creator);
        if (excel_data.isEmpty()){
            return new JsonResult(MessageEnum.ERROR_CASE_EXCEL.getCode(),MessageEnum.ERROR_CASE_EXCEL.getDesc());
        }else{
            int count = caseDao.addBatch(excel_data);
            return new JsonResult(0,"上传成功",count);
        }
    }
	
	@Override
	public JsonResult caseExecute(List<Integer> caseList) {

        //接口响应
        String res = "无";
        List<CaseExecuteChain> caseExecuteChainList = sqlUtils.getCaseExecuteList(caseList);
        //遍历用例集合
        for (CaseExecuteChain caseExecuteChain : caseExecuteChainList
        ) {
            try {

                //拼接请求的url
                String url = "http://" + caseExecuteChain.getIp() + caseExecuteChain.getPath();
                log.info("用例ID:{},访问路径:{},请求方法:{},请求格式:{},请求头信息:{},请求体信息:{},断言方式:{},断言数据:{}"
                        , caseExecuteChain.getId(), url, caseExecuteChain.getMethod(), caseExecuteChain.getContentType()
                        , caseExecuteChain.getHeaderData(), caseExecuteChain.getParamData(), caseExecuteChain.getAssertType(), caseExecuteChain.getAssertData());
                //get方法
                if (caseExecuteChain.getMethod() == 0) {
                    res = httpUtils.get(url);

                }
                //post方法
                else if (caseExecuteChain.getMethod() == 1) {
                    //form请求格式的参数
                    if (caseExecuteChain.getContentType() == 0) {
                        res = httpUtils.postByForm(caseExecuteChain.getHeaderData(), url, caseExecuteChain.getParamData());
                    }
                    //json格式
                    else if (caseExecuteChain.getContentType() == 1) {
                        //请求接口的返回值
                        res = httpUtils.postByJson(caseExecuteChain.getHeaderData(), url, caseExecuteChain.getParamData());
                    }
                    //text格式
                    else if (caseExecuteChain.getContentType() == 2) {
                        res = httpUtils.postByText(caseExecuteChain.getHeaderData(), url, caseExecuteChain.getParamData());
                    }
                    //无参
                    else {
                        res = httpUtils.postByJson(url);
                    }
                }
                sqlUtils.updateCaseExecuteResult(caseExecuteChain.getId(), res, 2);
            } catch (Exception e) {
                log.error(e.toString());
                return new JsonResult(MessageEnum.ERROR_CASE_EXECUTE.getCode(),e.toString());
            }
        }
        return new JsonResult(0, "用例执行成功!",caseList.size());
    }
}
