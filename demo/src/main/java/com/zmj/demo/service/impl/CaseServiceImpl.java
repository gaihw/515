package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpThread;
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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

    @Override
    public List<CaseChain> list(JSONObject jsonObject) {
        String caseName = jsonObject.getString("caseName");
        String interfaceManageID = jsonObject.getString("interfaceManageID");
        Integer isSuccess = jsonObject.getInteger("isSuccess");
        Integer page = jsonObject.getInteger("page") - 1;
        Integer limit = jsonObject.getInteger("limit");
        return caseDao.list(caseName, interfaceManageID, isSuccess, page, limit);
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
        return caseDao.add(interfaceManageID, caseName, headerData, paramData, assertType, assertData, state, creator);
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
        return caseDao.edit(id, interfaceManageID, caseName, headerData, paramData, assertType, assertData, state, creator);
    }

    @Override
    public int acount(JSONObject jsonObject) {
        Integer interfaceManageID = jsonObject.getInteger("interfaceManageID");
        String caseName = jsonObject.getString("caseName");
        Integer isSuccess = jsonObject.getInteger("isSuccess");
        return caseDao.acount(caseName, interfaceManageID, isSuccess);
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        excelUtils.downloadExcel(response);
    }

    @Override
    public JsonResult uploadExcel(MultipartFile excelFile, String creator) {
        List excel_data = excelUtils.uploadExcel(excelFile, creator);
        if (excel_data.isEmpty()) {
            return new JsonResult(MessageEnum.ERROR_CASE_EXCEL.getCode(), MessageEnum.ERROR_CASE_EXCEL.getDesc());
        } else {
            int count = caseDao.addBatch(excel_data);
            return new JsonResult(0, "上传成功", count);
        }
    }

    @Override
    public JsonResult caseExecute(List<Integer> caseList) {
        //每页显示的条数
        int SIZE = 10;

        //初始线程数量
        int threadPoolNum = 1;

        //执行用例总数，除以每页条数，需要分割多少次
        int caseListSplit = caseList.size()/SIZE;

        if (caseList.size() <=10){
            threadPoolNum = 1;
        }else {
            threadPoolNum = 10;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolNum);

        for (int i = 0; i <= caseListSplit; i++) {
            List<CaseExecuteChain> caseExecuteChainList = sqlUtils.getCaseExecuteList(caseList,SIZE*i,SIZE);

            //遍历用例集合
            for (CaseExecuteChain caseExecuteChain : caseExecuteChainList) {
                //拼接请求的url
                String url = "http://" + caseExecuteChain.getIp() + caseExecuteChain.getPath();
                log.info("用例ID:{},访问路径:{},请求方法:{},请求格式:{},请求头信息:{},请求体信息:{},断言方式:{},断言数据:{}"
                        , caseExecuteChain.getId(), url, caseExecuteChain.getMethod(), caseExecuteChain.getContentType()
                        , caseExecuteChain.getHeaderData(), caseExecuteChain.getParamData(), caseExecuteChain.getAssertType(), caseExecuteChain.getAssertData());
                // 生成所有测试线程
                executorService.execute(new HttpThread(httpUtils,sqlUtils,caseExecuteChain,url));
            }
            executorService.shutdown();
        }

        return new JsonResult(0, "用例执行成功!", caseList.size());
    }
}
