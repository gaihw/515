package com.zmj.controller.orther;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmj.dao.orther.base.MainDao;
import com.zmj.domain.JsonResult;
import com.zmj.domain.orther.ShortMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @date 2020/5/18 19:33
 * @Description: 验证码
 * @modify
 */
@RestController
@RequestMapping(value = "/main")
public class MainController {
    @Resource
    private MainDao mainDao;


    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);

    @RequestMapping(value = "/sms/list", method = RequestMethod.GET)
    public JsonResult mainSmsList(int pageNum, int pageSize) {
        return new JsonResult(mainDao.getMainSmsList(year, pageSize * (pageNum - 1), pageSize), "操作成功！", 100L);
    }

    @RequestMapping(value = "/sms/search", method = RequestMethod.POST)
    public JsonResult searchMainSms(@RequestBody JSONObject request) {
        int pageNum = request.getInteger("pageNum");
        int pageSize = request.getInteger("pageSize");
        String mobile = request.getString("mobile");
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ShortMessage> pageInfo = new PageInfo(mainDao.searchMainSmsList(year, mobile));
        return new JsonResult(pageInfo.getList(), "操作成功！", pageInfo.getTotal());
    }

    @RequestMapping(value = "/mail/list", method = RequestMethod.GET)
    public JsonResult getMainMailList(int pageNum, int pageSize) {
        return new JsonResult(mainDao.getMainMailList(pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
    }

    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    public JsonResult getMainAddressList(int pageNum, int pageSize) {
        return new JsonResult(mainDao.getMainAddressList(pageSize * (pageNum - 1), pageSize), "操作成功！", 100);
    }


    @RequestMapping(value = "/address/search", method = RequestMethod.POST)
    public JsonResult searchMainAddress(@RequestBody JSONObject request) {
        PageHelper.startPage(request.getInteger("pageNum"), request.getInteger("pageSize"));
        int pageNum = request.getInteger("pageNum");
        int pageSize = request.getInteger("pageSize");
        Long userId = request.getLong("userId");
        PageInfo<ShortMessage> pageInfo = new PageInfo(mainDao.searchMainAddressList(pageSize * (pageNum - 1), pageSize,userId));
        return new JsonResult(pageInfo.getList(), "操作成功！", mainDao.MainAddressCount(userId));
    }

    @RequestMapping(value = "/address/update", method = RequestMethod.POST)
    public JsonResult updateMainAddressTrust(@RequestBody JSONObject request) {
        List list = new ArrayList();
        JSONArray jsonArray = request.getJSONArray("id");
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        int res = mainDao.updateMainAddressTrust(list);
        if(res > 0){
            return new JsonResult(0,"操作成功！");
        }else{
            return new JsonResult(201,"信任地址添加失败！");
        }
    }

    /**
     *
     * @param action 1:手机号 2:用户id，3:虚拟id
     * @param id
     * @return
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public JsonResult getMainUserinfo(int action, String id) {
        String sql = "SELECT id,uid,mobile,google_key FROM umc.user_base_info WHERE ";
        if(action == 1){
            sql = sql + "mobile="+id;
        }else if(action == 2){
            sql = sql + "id="+id;
        }else{
            sql = sql + "uid="+id;
        }

        return new JsonResult(mainDao.userinfo(sql),"操作成功");
    }

    @RequestMapping(value = "/mail/search", method = RequestMethod.POST)
    public JsonResult searchMainMail(@RequestBody JSONObject request) {
        PageHelper.startPage(request.getInteger("pageNum"), request.getInteger("pageSize"));
        PageInfo<ShortMessage> pageInfo = new PageInfo(mainDao.searchMainMailList(request.getLong("userId")));
        return new JsonResult(pageInfo.getList(), "操作成功！", pageInfo.getTotal());
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
            String currencyId = mainDao.getCurrencyId(currencyName);

            // 根据用户类型获取用户ID
            if (userType.equals("mobile")) {
                // 用户类型是手机号，则通过手机号查询到相应用户ID
                String userIds = mainDao.getUserInfoByMobile(userVal).getId();
                mainDao.rechargeByUserId("(" + userIds + "," + currencyId + ",'a'," + amount + ",'0', RAND(),'0','0')");
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
                mainDao.rechargeByUserId(value.toString());
            }
        } catch (Exception e) {
            return new JsonResult(500, "充值失败，请联系管理员!");
        }

        long endTime = System.currentTimeMillis();
        return new JsonResult(0, "账号 " + userVal + " 充值 " + currencyName + " " + amount + " 个，用时 " + (endTime - startTime) + " ms");
    }

    /**
     * 获取所有币种名称
     *
     * @return 币种名称列表
     */
    @RequestMapping(value = "/getCurrencyNames", method = RequestMethod.GET)
    public JsonResult getCurrencyNames() {
        return new JsonResult(mainDao.getCurrencyNames());
    }

    /**
     * 获取所有站点
     *
     * @return 站点名称列表
     */
    @RequestMapping(value = "/getSites", method = RequestMethod.GET)
    public List<String> getSites() {
        return mainDao.getSites();
    }
}
