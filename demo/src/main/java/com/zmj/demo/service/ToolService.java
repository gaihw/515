package com.zmj.demo.service;

import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;

import java.math.BigDecimal;
import java.util.List;


public interface ToolService {

    List<SmsEmailcodeDomain> getList(int type);

    String userCheck(String userId,String money,String time);

    String allUserCheck();

    JsonResult getUserBalance(String userId,String time);

    List<UserDistributorChain> getUserPartner(String userId);

    JsonResult getAllUserBalance();

    String throughPositions(String userId, BigDecimal money,int marginType);

    String updateMarket(String symbol,String price);

    String positions(String userId, int marginType);


}
