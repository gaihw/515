package com.zmj.demo.common;

import com.zmj.demo.domain.auto.CaseExecuteChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpThread {

    public void run(HttpUtil httpUtils, SqlUtil sqlUtils, CaseExecuteChain caseExecuteChain, String url) {
        String res = "无";
        try {
//            System.out.println("Thread:" + Thread.currentThread().getName() + "准备完毕,time:" + System.currentTimeMillis());
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
                    res = httpUtils.postByJson(url,null);
                }
            }
            sqlUtils.updateCaseExecuteResult(caseExecuteChain.getId(), res, 2);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

}
