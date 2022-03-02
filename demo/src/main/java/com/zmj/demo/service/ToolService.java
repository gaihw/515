package com.zmj.demo.service;

import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;

import java.util.List;


public interface ToolService {

    List<SmsEmailcodeDomain> getList();

    String userCheck(String userId,String time);

    JsonResult getBalance(String userId,String time);
}
