package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.excel.ExcelUtils;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.CaseExcelChain;
import com.zmj.demo.enums.MessageEnum;
import com.zmj.demo.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/auto")
@Slf4j
public class CaseController {

    @Autowired
    private CaseService caseService;

    @RequestMapping(value = "/case/list",method = RequestMethod.GET)
    public JsonResult list(@RequestBody JSONObject jsonObject) {

        try {
            return new JsonResult(0, caseService.list(jsonObject));

        } catch (Exception e) {
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100004.getCode(), e.toString());
        }
    }

    @RequestMapping(value = "/case/add",method = RequestMethod.POST)
    public JsonResult add(@RequestBody CaseChain caseChain){
        String creator = "test";
        int result = 0;
        try {
            result = caseService.add(caseChain,creator);
            if (result == 1) {
                return new JsonResult(0,"添加成功!");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),MessageEnum.ERROR_PLATFORM_100001.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100001.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/case/delete",method = RequestMethod.POST)
    public JsonResult delete(@RequestParam("id") int id){
        String cretor = "test";
        int result = 0 ;
        try {
            result =  caseService.delete(id);
            if (result == 1){
                return new JsonResult(0,"删除成功!");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(),MessageEnum.ERROR_PLATFORM_100002.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100002.getCode(),e.toString());
        }
    }

    @RequestMapping(value = "/case/edit",method = RequestMethod.POST)
    public JsonResult edit(@RequestBody CaseChain caseChain){
        String cretor = "test";
        int result = 0 ;
        try {
            result =  caseService.edit(caseChain,cretor);
            if (result == 1){
                return new JsonResult(0,"修改成功!");
            }else{
                return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),MessageEnum.ERROR_PLATFORM_100003.getDesc());
            }
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100003.getCode(),e.toString());
        }
    }

    /**
     * 下载用例模板
     *
     * @throws Exception
     */
    @RequestMapping(value = "/case/downloadExcel",method = RequestMethod.GET)
    private void export(HttpServletRequest request, HttpServletResponse response)  {

        caseService.downloadExcel(response);

    }

    /**
     * 用例批量上传
     * @param excelFile
     * @return
     */
    @RequestMapping(value = "/case/uploadExcel",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public JsonResult importWatchExcel(@RequestParam("excelFile") MultipartFile excelFile) {
        String creator = "test";
        return caseService.uploadExcel(excelFile,creator);
    }

    /**
     * 用例执行
     * @param caseList
     * @return
     */
    @RequestMapping(value = "/case/execute",method = RequestMethod.POST)
    public JsonResult caseExecute(@RequestBody() JSONObject  caseList){
        try {
            return caseService.caseExecute(JSONArray.parseArray(caseList.getJSONArray("caseList").toJSONString(),Integer.class));
        }catch (Exception e){
            return new JsonResult(MessageEnum.ERROR_CASE_EXECUTE.getCode(),e.toString());
        }
    }

}
