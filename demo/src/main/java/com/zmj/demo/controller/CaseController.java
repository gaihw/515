package com.zmj.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.excel.ExcelUtils;
import com.zmj.demo.domain.JsonResult;
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

    @RequestMapping(value = "/case/list")
    public JsonResult list(@RequestBody JSONObject jsonObject) {

        try {
            return new JsonResult(0, caseService.list(jsonObject));

        } catch (Exception e) {
            return new JsonResult(MessageEnum.ERROR_PLATFORM_100004.getCode(), e.toString());
        }
    }

    /**
     * 描述：下载外部案件导入模板
     *
     * @throws Exception
     */
    @RequestMapping(value = "/case/downloadExcel",method = RequestMethod.GET)
    private void export(HttpServletRequest request, HttpServletResponse response)  {

        caseService.downloadExcel(response);

    }

    @RequestMapping(value = "/case/uploadExcel",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public JsonResult importWatchExcel(@RequestParam("excelFile") MultipartFile excelFile) {
        String creator = "test";
        return caseService.uploadExcel(excelFile,creator);
    }
}
