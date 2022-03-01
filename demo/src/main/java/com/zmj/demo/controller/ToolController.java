package com.zmj.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.SqlUtils;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.dev1.UserBalanceChain;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import com.zmj.demo.service.ToolService;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testng.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/tool")
public class ToolController {

    @Autowired
    private ToolService baseService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SqlUtils sqlUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/base/getList", method = RequestMethod.GET)
    public Map getList() {
        Map map = new HashMap();

        map.put("pageSize", "");
        map.put("pageNo", "");
        map.put("total", "");
        map.put("data", baseService.getList());
        map.put("status", "success");
        map.put("errorCode", "");
        map.put("errorMsg", "");
        return map;
    }

    @RequestMapping(value = "/base/test", method = RequestMethod.GET)
    public String test() {
        return "test...";
    }

    /**
     * 余额同步
     *
     * @param userId
     * @param time
     * @return
     */
    @RequestMapping(value = "/check/getBalance", method = RequestMethod.GET)
    public JsonResult getBalance(@Param(value = "userId") String userId, @Param(value = "time") String time) {
        List<UserBalanceChain> res = accountDao.getUserBalanceHold(userId);
        for (UserBalanceChain u : res
        ) {
            try {
                JSONObject j = new JSONObject();
                j.put("balance", u.getBalance());
                j.put("hold", u.getHold());
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
                redisService.set(userId, j);
            } catch (Exception e) {
                return new JsonResult<>(0, "账号同步redis失败！");
            }
        }
        log.info("用户:{},账户:{}", userId, res);
        return new JsonResult<>(0, res.get(0));
    }

    @RequestMapping(value = "/check/userCheck", method = RequestMethod.GET)
    public String userCheck(@Param(value = "userId") String userId, @Param(value = "time") String time) {
        StringBuffer stringBuffer = new StringBuffer();
        time = time == null ? Config.startTime : time;
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        try {
            //redis获取对账前的金额
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
            JSONObject user_bill_total_r = (JSONObject) redisService.get(userId);
            //数据库流水表聚合统计的值
            UserBillChain user_bill_total = accountDao.getUserBillTotal(time, userId);
            //操作后，查询数据库金额
            BigDecimal balance_post = BigDecimal.ZERO;
            BigDecimal hold_post = BigDecimal.ZERO;
            List<UserBalanceChain> balance_hold_jb_post = accountDao.getUserBalanceHold(userId);
            for (UserBalanceChain j : balance_hold_jb_post
            ) {
                balance_post = j.getBalance();
                hold_post = j.getHold();
            }
            log.info("操作后，数据库金额，balance:{},hold:{}", balance_post, hold_post);

            //操作前，账户金额
            BigDecimal total_pre = user_bill_total_r.getBigDecimal("balance").add(user_bill_total_r.getBigDecimal("hold"));
            //操作后，账户变化金额,如果无流水账单，默认赋值为0
            BigDecimal total_ing = user_bill_total.getTotal() == null ? BigDecimal.ZERO : user_bill_total.getTotal();
            //操作后，计算剩余的总金额
            BigDecimal total_post_calc = total_pre.add(total_ing);
            //操作后，数据库总的金额
            BigDecimal total_post = balance_post.add(hold_post);
            System.out.println("下单前，账户金额:" + total_pre);
            System.out.println("操作后，账户变化金额:" + total_ing);
            System.out.println("操作后，计算剩余的金额:" + total_post_calc);
            System.out.println("操作后，数据库存储的总金额:" + total_post);
            stringBuffer.append("下单前，账户总金额:" + total_pre).append("</br>");
            stringBuffer.append("操作后，账户变化金额:" + total_ing).append("</br>");
            stringBuffer.append("操作后，计算剩余总金额:" + total_post_calc).append("</br>");
            stringBuffer.append("操作后，数据库总金额:" + total_post).append("</br>");
            if (total_post.compareTo(total_post_calc) != 0){
                flag = true;
                error.append("账号总额不正确，请检查!").append("</br>");
            }

            //用户及合伙人列表
            List<UserDistributorChain> user_partner = sqlUtils.getUserPartner(userId);

            //查询用户的每条流水
            List<UserBillChain> userBillRes = accountDao.getUserBillByUser(userId, time);
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
            for (UserBillChain userBillChain : userBillRes) {
                log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                stringBuffer.append("类型:" + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
                //交易类型
                int type = userBillChain.getType();
                //合伙人id
                String partner_id = userBillChain.getPartnerId();
                //单号
                String source_id = userBillChain.getSourceId();
                //开仓手续费、平仓手续费,校验返佣
                if (type == 51 || type == 52) {
                    //如果user_partner长度为2，代表只有一个默认合伙人
                    if (user_partner.size() == 2) {
                        if (type == 51) {
                            //获取该笔订单给合伙人的返佣
                            UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 16, "asc");
                            log.info("手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, partner_id, type, source_id, userBillChain.getSize().abs(), partner_bill_jb.getSize());
                            stringBuffer.append("手续费返佣校验-->用户:" + userId + ",合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                            if (userBillChain.getSize().abs().compareTo(partner_bill_jb.getSize().abs()) != 0){
                                flag = true;
                                error.append("手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                            }
                        } else {
                            //获取该笔订单给合伙人的返佣
                            UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 16, "desc");
                            log.info("手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, partner_id, type, source_id, userBillChain.getSize().abs(), partner_bill_jb.getSize());
                            stringBuffer.append("手续费返佣校验-->用户:" + userId + ",合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                            if (userBillChain.getSize().abs().compareTo(partner_bill_jb.getSize().abs()) != 0){
                                flag = true;
                                error.append("手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                            }
                        }
                        continue;
                    }
                    //倒序查找user_partner列表
                    for (int i = user_partner.size() - 1; i >= 1; i--) {
                        //计算每个合伙人的手续费
                        //查找user_partner列表中的userId，此userId为合伙人，然后查找合伙人的流水
                        String tmp_partner_id = user_partner.get(i).getUserId();
                        BigDecimal allotLowerFeeRate = BigDecimal.ZERO;
                        if (i == user_partner.size() - 1) {
                            allotLowerFeeRate = BigDecimal.ONE.subtract(JSONObject.parseObject(user_partner.get(user_partner.size() - 2).getConfig()).getBigDecimal("allotLowerFeeRate"));
                        } else if (i == 1) {
                            allotLowerFeeRate = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("allotLowerFeeRate");
                        } else {
                            allotLowerFeeRate = JSONObject.parseObject(user_partner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate").subtract(JSONObject.parseObject(user_partner.get(i - 1).getConfig()).getBigDecimal("allotLowerFeeRate"));
                        }
                        if (type == 51) {
                            //获取该笔订单给合伙人的返佣
                            UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "asc");
                            log.info("手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id, userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN), partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN));
//                        log.info("用户:{},合伙人:{},交易类型:{},订单:{},手续费:{}",userId,tmp_partner_id,type,source_id,userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6,BigDecimal.ROUND_DOWN));
                            stringBuffer.append("手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN) + ",计算:" + partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN)).append("</br>");
                            if (userBillChain.getSize().abs().compareTo(partner_bill_jb.getSize().abs()) != 0){
                                flag = true;
                                error.append("手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN) + ",计算:" + partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN)).append("</br>");
                            }
                        } else {
                            //获取该笔订单给合伙人的返佣
                            UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "desc");
//                        log.info("用户:{},合伙人:{},交易类型:{},订单:{},手续费:{}",userId,tmp_partner_id,type,source_id,userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6,BigDecimal.ROUND_DOWN));
                            log.info("手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id, userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN), partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN));
                            stringBuffer.append("手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN) + ",计算:" + partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN)).append("</br>");
                            if (userBillChain.getSize().abs().compareTo(partner_bill_jb.getSize().abs()) != 0){
                                flag = true;
                                error.append("手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(6, BigDecimal.ROUND_DOWN) + ",计算:" + partner_bill_jb.getSize().setScale(6, BigDecimal.ROUND_DOWN)).append("</br>");
                            }
                        }
                    }
                }

                //盈亏对赌校验
                if (type == 4) {
                    //如果合伙人列表长度为2，代表只有默认合伙人
                    if (user_partner.size() == 2) {
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 27, "desc");
                        log.info("用户:{},合伙人:{},交易类型:{},订单:{},用户盈亏:{},合伙人盈亏:{}", userId, partner_id, type, source_id, userBillChain, partner_bill_jb);
                        log.info("盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN), partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                        //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与盈亏的对赌，只和默认合伙人有关
                    } else if((user_partner.size() > 2 && user_partner.get(1).getOpenBet() == 0) || (user_partner.size() > 2 && user_partner.get(0).getType() !=0  )){
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 27, "desc");
                        log.info("用户:{},合伙人:{},交易类型:{},订单:{},用户盈亏:{},合伙人盈亏:{}", userId, partner_id, type, source_id, userBillChain, partner_bill_jb);
                        log.info("盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN), partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }else {//如果合伙人列表长度大于2，代表有合伙人，并且合伙人的类型是0，开启了对赌
                        //获取合伙人对赌的比列
                        BigDecimal partnerTransferOutRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
                        String tmp_partner_id = user_partner.get(1).getUserId();
                        //计算合伙人对赌盈亏值
                        BigDecimal partner_profit = userBillChain.getSize().multiply(partnerTransferOutRatio).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 27, "desc");
//                    log.info("用户:{},合伙人:{},交易类型:{},订单:{},盈亏:{}",userId,tmp_partner_id,type,source_id,partner_profit);
                        log.info("盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmp_partner_id, type, source_id, partner_profit, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                        //计算默认合伙人对赌盈亏值
                        BigDecimal default_partner_profit = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                        String tmp_default_partner_id = user_partner.get(user_partner.size() - 1).getUserId();
                        //获取该笔订单给合伙人的返佣
                        UserBillChain default_partner_bill_jb = accountDao.getUserBill(userId, tmp_default_partner_id, source_id, 27, "desc");
//                    log.info("用户:{},合伙人:{},交易类型:{},订单:{},盈亏:{}",userId,tmp_default_partner_id,type,source_id,default_partner_profit);
                        stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_profit + ",计算得:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        log.info("盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmp_default_partner_id, type, source_id, default_partner_profit, default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        if (default_partner_profit.abs().compareTo(default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_profit + ",计算得:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }
                } else {
                    continue;
                }
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
        } catch (Exception e) {
            return "账单未更新，请稍后重试!";
        }
        if (flag){
            return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
        }
        return stringBuffer.toString();
    }


}
