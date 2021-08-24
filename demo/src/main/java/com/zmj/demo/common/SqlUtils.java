package com.zmj.demo.common;


import com.zmj.demo.dao.demodata.auto.CaseDao;
import com.zmj.demo.domain.auto.CaseExecuteChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class SqlUtils {

    @Autowired
    private CaseDao caseDao;

    /**
     * 获取执行用例集的详细接口信息
     * @param caseList
     * @return
     */
    public List<CaseExecuteChain> getCaseExecuteList(List<Integer> caseList,Integer index,Integer limit){
        System.out.println(caseList);
        List<CaseExecuteChain> caseExecuteChainList = caseDao.caseAllInfoById(caseList,index,limit);
        log.info("用例集:{},信息集:{}",caseList,caseExecuteChainList);
        return caseExecuteChainList;
    }

    /**
     * 用例执行后，更新执行结果
     * @param caseId
     * @param response
     * @param is_success
     */
    public void updateCaseExecuteResult(Integer caseId,String response,Integer is_success){
        int result = 0;
        try{
            result = caseDao.executeResult(caseId,response,is_success);
            if (result == 1){
                log.info("用例执行后，结果更新成功:::用例id:{},接口响应:{},执行结果:{}",caseId,response,is_success);
            }else {
                log.info("用例执行后，结果更新失败:::用例id:{},接口响应:{},执行结果:{}",caseId,response,is_success);
            }
        }catch (Exception e){
            log.info("用例执行后，结果报异常:::用例id:{},接口响应:{},执行结果:{},异常:{}",caseId,response,is_success,e.toString());
        }
    }
}
