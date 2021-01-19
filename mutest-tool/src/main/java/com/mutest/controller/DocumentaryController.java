package com.mutest.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mutest.advice.BusinessErrorException;
import com.mutest.advice.BusinessMsgEnum;
import com.mutest.dao.future.DocumentarySmsDao;
import com.mutest.model.JsonResult;
import com.mutest.model.ShortMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/documentary")
public class DocumentaryController {
    @Resource
    private DocumentarySmsDao documentarySmsDao;

    @RequestMapping(value = "/sms/list", method = RequestMethod.GET)
    public JsonResult getDocumentarySmsList(int pageNum, int pageSize) {

        try {
            return new JsonResult(documentarySmsDao.documentarySmsList(pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }

    @RequestMapping(value = "/sms/search", method = RequestMethod.POST)
    public JsonResult getDocumentarySmsList(@RequestBody JSONObject request) {
        int pageNum = request.getInteger("pageNum");
        int pageSize = request.getInteger("pageSize");
        String mobile = request.getString("mobile");
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<ShortMessage> pageInfo = new PageInfo(documentarySmsDao.searchDocumentarySmsList(mobile));
            return new JsonResult(pageInfo.getList(), "查询成功！", pageInfo.getTotal());
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }
}
