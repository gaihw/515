package com.mutest.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mutest.advice.BusinessErrorException;
import com.mutest.advice.BusinessMsgEnum;
import com.mutest.dao.regular.FexDao;
import com.mutest.model.JsonResult;
import com.mutest.model.ShortMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * @author muguozheng
 * @date 2020/7/8 15:33
 * @Description: FEX交易所
 * @modify
 */
@RestController
@RequestMapping(value = "/fex")
public class FexController {
    @Resource
    private FexDao fexDao;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);

    @RequestMapping(value = "/sms/list", method = RequestMethod.GET)
    public JsonResult getFexSmsList(int pageNum, int pageSize) {
        try {
            return new JsonResult(fexDao.getFexSmsList(year, pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }

    @RequestMapping(value = "/sms/search", method = RequestMethod.POST)
    public JsonResult getMainSmsList(@RequestBody JSONObject request) {
        int pageNum = request.getInteger("pageNum");
        int pageSize = request.getInteger("pageSize");
        String mobile = request.getString("mobile");
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<ShortMessage> pageInfo = new PageInfo(fexDao.searchFexSmsList(year, mobile));
            return new JsonResult(pageInfo.getList(), "操作成功！", pageInfo.getTotal());
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public JsonResult rechargeMain(@RequestBody JSONObject request) {
        long startTime = System.currentTimeMillis();
        StringBuilder value = new StringBuilder();

        String currencyName = request.getString("currencyName");
        int amount = request.getInteger("rechargeAmount");
        String userType = request.getString("rechargeUserType");
        String userVal = request.getString("rechargeUserVal");

        try {
            // 根据币种名称获取币种ID
            String currencyId = fexDao.getCurrencyId(currencyName);

            // 根据用户类型获取用户ID
            if (userType.equals("mobile")) {
                // 用户类型是手机号，则通过手机号查询到相应用户ID
                String userIds = fexDao.getUserInfoByMobile(userVal).getId();
                fexDao.rechargeByUserId("(" + userIds + "," + currencyId + ",'a'," + amount + ",'0', RAND(),'0','0')");
            } else {
                String userIds = userVal;
                String[] userIdArr = userIds.split("-");

                int userIdFirst = Integer.parseInt(userIdArr[0]);
                int userIdLast = Integer.parseInt(userIdArr[userIdArr.length - 1]);

                int userIdTemp = userIdFirst;
                // 拼接sql语句
                for (int i = 0; i < userIdLast - userIdFirst; i++) {
                    value.append("(" + userIdTemp + "," + currencyId + ",'a'," + amount + ",'0', RAND(),'0','0'),");
                    userIdTemp++;
                }
                value.append("(" + userIdArr[userIdArr.length - 1] + "," + currencyId + ",'a'," + amount + ",'0', RAND(),'0','0')");
                fexDao.rechargeByUserId(value.toString());
            }
        } catch (Exception e) {
            return new JsonResult("500", "充值失败，请联系管理员!");
        }

        long endTime = System.currentTimeMillis();
        return new JsonResult("0", "账号 " + userVal + " 充值 " + currencyName + " " + amount + " 个，用时 " + (endTime - startTime) + " ms");
    }

    /**
     * 获取所有币种名称
     *
     * @return 币种名称列表
     */
    @RequestMapping(value = "/getCurrencyNames", method = RequestMethod.GET)
    public List<String> getCurrencyNames() {
        try {
            return fexDao.getCurrencyNames();
        } catch (Exception e) {
            throw new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
        }
    }

    /**
     * 获取所有站点
     *
     * @return 站点名称列表
     */
    @RequestMapping(value = "/getSites", method = RequestMethod.GET)
    public List<String> getSites() {
        try {
            return fexDao.getSites();
        } catch (Exception e) {
            throw new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION);
        }
    }

    /**
     * FEX邮箱验证码
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return
     */
    @RequestMapping(value = "/mail/list", method = RequestMethod.GET)
    public JsonResult getMainMailList(int pageNum, int pageSize) {
        try {
            return new JsonResult(fexDao.getFexMailList(pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
        } catch (Exception e) {
            return new JsonResult(new BusinessErrorException(BusinessMsgEnum.UNEXPECTED_EXCEPTION));
        }
    }
}
