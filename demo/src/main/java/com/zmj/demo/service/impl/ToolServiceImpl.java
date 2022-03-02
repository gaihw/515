package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.SqlUtil;
import com.zmj.demo.common.dev1.FeeAndProfitCalc;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.dao.test.SmsEmailCode;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.dev1.SwapOrderChain;
import com.zmj.demo.domain.dev1.UserBalanceChain;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import com.zmj.demo.service.ToolService;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private SmsEmailCode smsEmailCode;

    @Autowired
    private ToolService baseService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SqlUtil sqlUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FeeAndProfitCalc feeAndProfitCalc;

    @Override
    public List<SmsEmailcodeDomain> getList() {
        return smsEmailCode.getList();
    }

    @Override
    public String userCheck(String userId,String time) {
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
            BigDecimal total_ing = user_bill_total == null ? BigDecimal.ZERO : user_bill_total.getTotal();
            //操作后，计算剩余的总金额
            BigDecimal total_post_calc = total_pre.add(total_ing);
            //操作后，数据库总的金额
            BigDecimal total_post = balance_post.add(hold_post);
            log.info("下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}" , total_pre,total_ing,total_post_calc,total_post);
            stringBuffer.append("下单前，账户总金额:" + total_pre+"，balance:"+user_bill_total_r.getBigDecimal("balance")+"，hold:"+user_bill_total_r.getBigDecimal("hold")).append("</br>");
            stringBuffer.append("操作后，账户变化金额:" + total_ing).append("</br>");
            stringBuffer.append("操作后，计算剩余总金额:" + total_post_calc).append("</br>");
            stringBuffer.append("操作后，数据库总金额:" + total_post+"，balance:"+balance_post+"，hold:"+hold_post).append("</br>");
            if (total_post.compareTo(total_post_calc) != 0){
                flag = true;
                error.append("账号总额不正确，请检查!").append("</br>");
            }

            //用户及合伙人列表
            List<UserDistributorChain> user_partner = sqlUtils.getUserPartner(userId);

            //查询用户的每条流水
            List<UserBillChain> userBillRes = accountDao.getUserBillByUser(userId, time) ;
            log.info("用户:{},流水:{}",userId,userBillRes);
            if (userBillRes.size() == 0){
                return stringBuffer.append("用户暂无流水！").toString();
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
            for (UserBillChain userBillChain : userBillRes) {
                log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                stringBuffer.append("类型:" + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
                //交易类型
                int type = userBillChain.getType();
                //开平仓的手续费、平仓盈亏
                if (type == 4){
                    //如果合伙人的列表长度为2，表示该用户只有默认合伙人
                    if (user_partner.size() == 2){
                        String[] s = feeAndProfitCalc.defaultPartnerProfit(user_partner,userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length ==2){
                            error.append(s[1]);
                        }
                    }else {//该用户有多级合伙人
                        String[] s = feeAndProfitCalc.morePartnerProfit(user_partner,userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length ==2){
                            error.append(s[1]);
                        }
                    }
                }else if (type == 51 || type == 52){
                    String[] s= feeAndProfitCalc.partnerFee(user_partner,userBillChain);
                    stringBuffer.append(s[0]);
                    if (s.length ==2){
                        error.append(s[1]);
                    }
                }else {
                    continue;
                }
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
        } catch (Exception e) {
            log.error(e.toString());
            return "账单未更新，请稍后重试!";
        }
        return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();

    }

    @Override
    public JsonResult getBalance(String userId, String time) {
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


}
