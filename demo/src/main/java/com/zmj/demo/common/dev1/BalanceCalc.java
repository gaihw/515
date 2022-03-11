package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.SqlUtil;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.UserBalanceChain;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
public class BalanceCalc {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountDao accountDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 计算user_balance表用户的余额变化
     * @param userId
     * @param time
     */
    public String balanceCalc(JSONObject userBalanceR,String userId, String time) {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        //数据库流水表聚合统计的值
        UserBillChain userBillTotal = accountDao.getUserBillTotalByUser(userId,time);
        //操作后，查询数据库金额
        BigDecimal balancePost = BigDecimal.ZERO;
        BigDecimal holdPost = BigDecimal.ZERO;
        List<UserBalanceChain> balanceHoldJbPost = accountDao.getUserBalanceByUser(userId);
        for (UserBalanceChain j : balanceHoldJbPost
        ) {
            balancePost = j.getBalance();
            holdPost = j.getHold();
        }
        log.info("操作后，数据库金额，balance:{},hold:{}", balancePost, holdPost);
        //操作前，账户金额
        BigDecimal totalPre = userBalanceR.getBigDecimal("balance").add(userBalanceR.getBigDecimal("hold"));
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal totalIng = userBillTotal == null ? BigDecimal.ZERO : userBillTotal.getTotal();
        //操作后，计算剩余的总金额
        BigDecimal totalPostCalc = totalPre.add(totalIng);
        //操作后，数据库总的金额
        BigDecimal totalPost = balancePost.add(holdPost);
        log.info("下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}" , totalPre,totalIng,totalPostCalc,totalPost);
        stringBuffer.append("下单前，账户总金额:" + totalPre+"，balance:"+userBalanceR.getBigDecimal("balance")+"，hold:"+userBalanceR.getBigDecimal("hold")).append("</br>");
        stringBuffer.append("操作后，账户变化金额:" + totalIng).append("</br>");
        stringBuffer.append("操作后，计算剩余总金额:" + totalPostCalc).append("</br>");
        stringBuffer.append("操作后，数据库总金额:" + totalPost+"，balance:"+balancePost+"，hold:"+holdPost).append("</br>");
        if (totalPost.setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(totalPostCalc.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
            flag = true;
            return error.append("账号总额不正确，请检查!").append("</br>").toString();
        }
        return stringBuffer.toString();
    }

    /**
     * 校验user_partner_balance表账户余额变化
     * @param userPartnerBalanceR
     * @param userId
     * @param time
     * @return
     */
    public String partnerBalanceCalc(JSONObject userPartnerBalanceR,String userId, String time){
        StringBuffer stringBuffer = new StringBuffer();
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        //数据库流水表聚合统计的值
        UserBillChain userBillTotal = accountDao.getUserBillTotalToPartnerBalanceByUser(userId,time);
        //操作后，查询数据库金额
        BigDecimal balancePost = BigDecimal.ZERO;
        BigDecimal holdPost = BigDecimal.ZERO;
        List<UserBalanceChain> balanceHoldJbPost = accountDao.getUserPartnerBalanceByUser(userId);
        for (UserBalanceChain j : balanceHoldJbPost
        ) {
            balancePost = j.getBalance();
            holdPost = j.getHold();
        }
        log.info("操作后，user_partner_balance数据库金额，balance:{},hold:{}", balancePost, holdPost);
        //操作前，账户金额
        BigDecimal totalPre = userPartnerBalanceR.getBigDecimal("balance").add(userPartnerBalanceR.getBigDecimal("hold"));
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal totalIng = userBillTotal == null ? BigDecimal.ZERO : userBillTotal.getTotal();
        //操作后，计算剩余的总金额
        BigDecimal totalPostCalc = totalPre.add(totalIng);
        //操作后，数据库总的金额
        BigDecimal totalPost = balancePost.add(holdPost);
        log.info("下单前，user_partner_balance账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}" , totalPre,totalIng,totalPostCalc,totalPost);
        stringBuffer.append("下单前，user_partner_balance账户总金额:" + totalPre+"，balance:"+userPartnerBalanceR.getBigDecimal("balance")+"，hold:"+userPartnerBalanceR.getBigDecimal("hold")).append("</br>");
        stringBuffer.append("操作后，账户变化金额:" + totalIng).append("</br>");
        stringBuffer.append("操作后，user_partner_balance计算剩余总金额:" + totalPostCalc).append("</br>");
        stringBuffer.append("操作后，user_partner_balance数据库总金额:" + totalPost+"，balance:"+balancePost+"，hold:"+holdPost).append("</br>");
        if (totalPost.setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(totalPostCalc.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
            flag = true;
            return error.append("user_partner_balance表账号总额不正确，请检查!").append("</br>").toString();
        }
        return stringBuffer.toString();
    }
}
