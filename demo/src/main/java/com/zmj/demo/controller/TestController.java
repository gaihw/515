package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import com.zmj.demo.common.dev1.UserCheckThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private UserCheckThread userCheckThread;


    @RequestMapping(value = "/v1/test",method = RequestMethod.GET)
    public JSONObject test(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"accessNumberList\":[1,2,2,1,15000000001],\"levelNameList\":[\"四级\",\"一级\",\"三级\",\"二级\",\"其他\"]}}");
    }
    @RequestMapping(value = "/v1/test1",method = RequestMethod.POST)
    public JSONObject test01(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"accessNumberList\":[24,18,1,1,1],\"userNameList\":[\"陈陈\",\"盖洪伟\",\"实体abc\",\"包岩\",\"敏感数据访问监控001敏感数据访问监控\"]}}");
    }

    @RequestMapping(value = "/v1/test2",method = RequestMethod.POST)
    public JSONObject test02(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"timeList\":[\"2021-09-10\",\"2021-09-11\",\"2021-09-12\",\"2021-09-13\",\"2021-09-14\",\"2021-09-15\",\"2021-09-16\",\"2021-09-17\"],\"dataList\":[{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":0,\"fieldNumber\":0},{\"fieldAccessNumber\":77666666,\"fieldNumber\":18}]}}");
    }

    @RequestMapping(value = "/v1/test3",method = RequestMethod.POST)
    public JSONObject test03(){
        return JSONObject.parseObject("{\"status\":\"success\",\"errorCode\":\"\",\"errorMsg\":\"\",\"data\":{\"content\":[{\"id\":10003575,\"jobName\":\"回归a0003\",\"jobCategoryId\":null,\"createdBy\":\"盖洪伟\",\"updatedDate\":\"2021-09-27 21:22:03\",\"scheduleCycle\":\"MANUAL\",\"lastRunTime\":\"2021-09-27 20:17:11\",\"status\":\"UPDATE_FAIL\",\"open\":true,\"statistics\":35328,\"runDuration\":50000,\"nextRunTime\":null,\"description\":\"\",\"dataSourceName\":null,\"dataSourceType\":null,\"businessType\":null,\"jobId\":null,\"catalogTableId\":10010420,\"repositoryJobNodeDetail\":null,\"tableName\":null,\"tableAlias\":null}],\"pageable\":{\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"pageSize\":10,\"pageNumber\":0,\"offset\":0,\"unpaged\":false,\"paged\":true},\"totalElements\":1,\"totalPages\":1,\"last\":true,\"sort\":{\"unsorted\":true,\"sorted\":false,\"empty\":true},\"numberOfElements\":1,\"first\":true,\"size\":10,\"number\":0,\"empty\":false}}");
    }

    @RequestMapping(value = "/v1/test4",method = RequestMethod.GET)
    public String test04(){
        int count = 10;
        for (int i = 0; i < count; i++) {
            userCheckThread.test("index = " + i);
        }
        return "success";

    }

    @RequestMapping(value = "/v1/test5",method = RequestMethod.GET)
    public void test05() throws IOException {
        File mobile = new File("/Users/mac/Desktop/user.csv");
        FileWriter fw = new FileWriter(mobile,true);
        HttpUtil httpUtil = new HttpUtil();
        String url = "https://www-demo.hpx.today/v1/users/membership/sign-in";
        String param = "";
        for (int i = 0; i < 500; i++) {
            param = "{\"username\":\"16600000"+String.format("%03d",i)+"\",\"password\":\"ghw111111\"}";
            String res = httpUtil.postByJson(url,param);
            String accessToken = JSONObject.parseObject(res).getJSONObject("data").getString("accessToken");
            int rd=Math.random()>0.5?1:0;
            fw.write(accessToken+","+rd+"\n");
            fw.flush();
        }
    }
}
