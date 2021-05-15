package com.zmj.controller.orther;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmj.dao.orther.future.DocumentarySmsDao;
import com.zmj.domain.JsonResult;
import com.zmj.domain.orther.ShortMessage;
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
        return new JsonResult(documentarySmsDao.documentarySmsList(pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
    }

    @RequestMapping(value = "/sms/search", method = RequestMethod.POST)
    public JsonResult getDocumentarySmsList(@RequestBody JSONObject request) {
        int pageNum = request.getInteger("pageNum");
        int pageSize = request.getInteger("pageSize");
        String mobile = request.getString("mobile");
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ShortMessage> pageInfo = new PageInfo(documentarySmsDao.searchDocumentarySmsList(mobile));
        return new JsonResult(pageInfo.getList(), "查询成功！", pageInfo.getTotal());
    }
}
