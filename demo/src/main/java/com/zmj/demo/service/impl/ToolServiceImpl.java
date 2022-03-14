package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import com.zmj.demo.common.SqlUtil;
import com.zmj.demo.common.dev1.*;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.dao.test.SmsEmailCode;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.dev1.*;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import com.zmj.demo.service.ToolService;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private FeeCalc feeCalc;

    @Autowired
    private ProfitCalc profitCalc;

    @Autowired
    private ThroughPositionsCalc throughPositionsCalc;

    @Autowired
    private AccountUtil accountUtil;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private BalanceCalc balanceCalc;

    @Override
    public List<SmsEmailcodeDomain> getList() {
        return smsEmailCode.getList();
    }

    @Override
    public String userCheck(String userId,String money,String time) {
        StringBuffer stringBuffer = new StringBuffer();
        time = time == null ? Config.startTime : time;
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        try {
            //redis获取对账前的金额
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
            JSONObject userBalanceR = (JSONObject) redisService.get("user_balance:"+userId);
            if (userBalanceR == null){
                return "请先同步user_balance账户余额！";
            }
            String balanceRes = balanceCalc.balanceCalc(userBalanceR,userId,time);
            if(balanceRes.contains("user_balance账号总额不正确") ) {
                error.append(balanceRes);
            }
            stringBuffer.append(balanceRes);
            //如果user_partner_balance表无用户数据，则不计算流水的变化
            if (accountDao.getUserPartnerBalanceByUser(userId).size() == 0){
                log.info("用户:{}，user_partner_balance表无数据，不做流水校验！",userId);
                stringBuffer.append("用户:"+userId+"，user_partner_balance表无数据，不做流水校验！").append("</br>");
            }else {
//                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
                JSONObject userPartnerBalanceR = (JSONObject) redisService.get("user_partner_balance:"+userId);
                if (userPartnerBalanceR == null){
                    return "请先同步user_partner_balance账户余额！";
                }
                String partnerBalanceRes = balanceCalc.partnerBalanceCalc(userPartnerBalanceR,userId,time);
                if(partnerBalanceRes.contains("账号总额不正确") ) {
                    error.append(partnerBalanceRes);
                }
                stringBuffer.append(partnerBalanceRes);
            }

            //用户及合伙人列表
            List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);
            //查询用户的每条流水
            List<UserBillChain> userBillRes = accountDao.getUserBillByUser(userId, time) ;
            log.info("用户:{},流水:{}",userId,userBillRes);
            if (userBillRes.size() == 0){
                return stringBuffer.append("用户暂无流水！").toString();
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
            String[] s ;
            for (UserBillChain userBillChain : userBillRes) {
                log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                stringBuffer.append("<font color=\"#67C23A\">类型:</font> " + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
                //交易类型
                int type = userBillChain.getType();
                //平仓盈亏
                if (type == 4){
                    List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(),userBillChain.getSourceId(),null);
                    BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                    BigDecimal profit = BigDecimal.ZERO;
                    //多仓
                    if (positionAction.get(0).getDirection().equalsIgnoreCase("LONG")){
                        //(平仓价格-开仓价格)*数量*面值
                        profit = (positionAction.get(0).getClosePrice().subtract(positionAction.get(0).getOpenPrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                    }else {
                        //(开仓价格-平仓价格)*数量*面值
                        profit = (positionAction.get(0).getOpenPrice().subtract(positionAction.get(0).getClosePrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                    }
                    //全仓
                    if (positionAction.get(0).getMarginType().equalsIgnoreCase("CROSSED")){
                        if (userBillChain.getSize().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                            log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                            error.append("盈亏计算不正确--->,用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+profit.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }else {
                        //盈亏+保证金,逐仓流水，把保证金和盈亏合到一起计算了
                        profit = profit.add(positionAction.get(0).getMargin());
                        if (userBillChain.getSize().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                            log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                            error.append("盈亏计算不正确--->,用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+profit.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }
                    //如果合伙人的列表长度为2，表示该用户只有默认合伙人
                    if (userPartner.size() == 2){
                        s = profitCalc.defaultPartnerProfit(userPartner,userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length ==2){
                            error.append(s[1]);
                        }
                    }else {//该用户有多级合伙人
                        s = profitCalc.morePartnerProfit(userPartner,userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length ==2){
                            error.append(s[1]);
                        }
                    }
                    //开平仓的手续费
                }else if (type == 51 || type == 52){
                    List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(),userBillChain.getSourceId(),null);
                    BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                    BigDecimal fee = BigDecimal.ZERO;
                    //先计算手续费
                    if (type == 51){
                        fee = positionAction.get(0).getOpenPrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                        if ( userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                            log.info("手续费计算不正确--->用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                            error.append("手续费计算不正确--->用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }else {
                        fee = positionAction.get(0).getClosePrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                        if ( userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                            log.info("手续费计算不正确--->用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                            error.append("手续费计算不正确--->用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }
                    s= feeCalc.partnerFee(userPartner,userBillChain);
                    stringBuffer.append(s[0]);
                    if (s.length ==2){
                        error.append(s[1]);
                    }
                    //穿仓回补
                }else if (type == 54){
                    //先计算穿仓回补的钱是不是正确
                    throughPositionsCalc.partnerBack(userBillChain);

                    //手续费返佣
                }else if (type == 16){
                    log.info("分佣金额余额加减--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getPreBalance(),userBillChain.getSize(),userBillChain.getPostBalance());
                    stringBuffer.append("分佣金额余额加减--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",之前余额:"+userBillChain.getPreBalance()+",分佣金额:"+userBillChain.getSize()+",之后余额:"+userBillChain.getPostBalance()).append("</br>");
                    if (userBillChain.getPreBalance().add(userBillChain.getSize()).compareTo(userBillChain.getPostBalance()) != 0){
                        log.info("分佣金额余额加减不正确--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getPreBalance(),userBillChain.getSize(),userBillChain.getPostBalance());
                        error.append("分佣金额余额加减不正确--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",之前余额:"+userBillChain.getPreBalance()+",分佣金额:"+userBillChain.getSize()+",之后余额:"+userBillChain.getPostBalance()).append("</br>");
                    }
                    //爆仓剩余金额，返回给默认合伙人
                }else if (type == 59){
                    UserBillChain partnerBillJb = accountDao.getUserBill( Config.high_partner, userBillChain.getSourceId(), 59);
                    log.info("爆仓剩余金额，返回给默认合伙人--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getSize(),partnerBillJb.getSize());
                    stringBuffer.append("爆仓剩余金额，返回给默认合伙人--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",爆仓剩余金额:"+userBillChain.getSize()+",返还金额:"+partnerBillJb.getSize()).append("</br>");
                    if (userBillChain.getSize().abs().compareTo(partnerBillJb.getSize().abs()) != 0){
                        log.info("爆仓剩余金额，返回给默认合伙人不正确--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getSize(),partnerBillJb.getSize());
                        error.append("爆仓剩余金额，返回给默认合伙人不正确--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",爆仓剩余金额:"+userBillChain.getSize()+",返还金额:"+partnerBillJb.getSize()).append("</br>");
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
    public String allUserCheck(){
        StringBuffer stringBuffer = new StringBuffer();
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        //获取平台交易前的账户总额
        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userAllBalanceR = (JSONObject) redisService.get("user_balance:all");
        if (userAllBalanceR == null){
            return "请先同步user_balance账户总余额！";
        }
        //操作前，账户金额
        BigDecimal totalPre = userAllBalanceR.getBigDecimal("balance").add(userAllBalanceR.getBigDecimal("hold"));
        //获取流水变化的值
        //数据库流水表聚合统计的值
        BigDecimal userBillTotal = accountDao.getAllUserBillTotal();
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal totalIng = userBillTotal == null ? BigDecimal.ZERO : userBillTotal;
        //操作后，计算剩余的总金额
        BigDecimal totalPostCalc = totalPre.add(totalIng);
        //操作后，查询数据库金额
        UserBalanceChain balanceHoldJbPost = accountDao.getAllUserBalanceTotal();
        BigDecimal balancePost = balanceHoldJbPost.getBalance();
        BigDecimal holdPost = balanceHoldJbPost.getHold();
        //操作后，数据库总的金额
        BigDecimal totalPost = balancePost.add(holdPost);
        log.info("user_balance操作后，数据库金额，balance:{},hold:{}", balancePost, holdPost);
        log.info("user_balance下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}" , totalPre,totalIng,totalPostCalc,totalPost);
        stringBuffer.append("user_balance下单前，账户总金额:" + totalPre+"，balance:"+userAllBalanceR.getBigDecimal("balance")+"，hold:"+userAllBalanceR.getBigDecimal("hold")).append("</br>");
        stringBuffer.append("user_balance操作后，账户变化金额:" + totalIng).append("</br>");
        stringBuffer.append("user_balance操作后，计算剩余总金额:" + totalPostCalc).append("</br>");
        stringBuffer.append("user_balance操作后，数据库总金额:" + totalPost+"，balance:"+balancePost+"，hold:"+holdPost).append("</br>");
        if (totalPost.setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(totalPostCalc.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
            flag = true;
            error.append("账号总额不正确，请检查!").append("</br>").toString();
        }
        stringBuffer.append("-----------").append("</br>");
        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userPartnerAllBalanceR = (JSONObject) redisService.get("user_partner_balance:all");
        if (userPartnerAllBalanceR == null){
            return "请先同步user_partner_balance账户总余额！";
        }
        //操作前，账户金额
        BigDecimal partnerTotalPre = userPartnerAllBalanceR.getBigDecimal("balance").add(userPartnerAllBalanceR.getBigDecimal("hold"));
        //获取流水变化的值
        //数据库流水表聚合统计的值
        BigDecimal userBillPartnerTotal = accountDao.getAllUserBillToPartnerBalance();
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal partnerTotalIng = userBillPartnerTotal == null ? BigDecimal.ZERO : userBillPartnerTotal;
        //操作后，计算剩余的总金额
        BigDecimal partnerTotalPostCalc = partnerTotalPre.add(partnerTotalIng);
        //操作后，查询数据库金额
        UserBalanceChain partnerBalanceHoldJbPost = accountDao.getAllUserPartnerBalanceTotal();
        BigDecimal partnerBalancePost = partnerBalanceHoldJbPost.getBalance();
        BigDecimal partnerHoldPost = partnerBalanceHoldJbPost.getHold();
        //操作后，数据库总的金额
        BigDecimal partnertotalPost = partnerBalancePost.add(partnerHoldPost);
        log.info("user_partner_balance操作后，数据库金额，balance:{},hold:{}", partnerBalancePost, partnerHoldPost);
        log.info("user_partner_balance下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}" , partnerTotalPre,partnerTotalIng,partnerTotalPostCalc,partnertotalPost);
        stringBuffer.append("user_partner_balance下单前，账户总金额:" + partnerTotalPre+"，balance:"+userPartnerAllBalanceR.getBigDecimal("balance")+"，hold:"+userPartnerAllBalanceR.getBigDecimal("hold")).append("</br>");
        stringBuffer.append("user_partner_balance操作后，账户变化金额:" + partnerTotalIng).append("</br>");
        stringBuffer.append("user_partner_balance操作后，计算剩余总金额:" + partnerTotalPostCalc).append("</br>");
        stringBuffer.append("user_partner_balance操作后，数据库总金额:" + partnertotalPost+"，balance:"+partnerBalancePost+"，hold:"+partnerHoldPost).append("</br>");
        if (partnertotalPost.setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(partnerTotalPostCalc.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
            flag = true;
            error.append("user_partner_balance账号总额不正确，请检查!").append("</br>").toString();
        }

        //对type类型为16,27,38,50,54,55,56,58,59的流水账单
        int count = accountDao.getUserBillByTypeCount();
        if (count == 0){
            stringBuffer.append("无type类型为16,27,38,50,54,55,56,58,59的流水账单").append("</br>");
            return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
        }
        count = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(1000),0,BigDecimal.ROUND_UP).intValue();
        for (int i = 0; i < count; i++) {
            List<UserBillChain> userBillChains = accountDao.getUserBillByType(1000*i);
            for (UserBillChain u:userBillChains) {
                log.info("用户ID:{},类型:{},size:{},pre_balance:{},post_balance:{},订单ID:{}",u.getUserId(),u.getType(),u.getSize(),u.getPreBalance(),u.getPostBalance(),u.getSourceId());
                if(u.getSize().add(u.getPreBalance()).compareTo(u.getPostBalance()) != 0){
                    log.info("流水未加上，请查看--->用户ID:{},类型:{},size:{},pre_balance:{},post_balance:{},订单ID:{}",u.getUserId(),u.getType(),u.getSize(),u.getPreBalance(),u.getPostBalance(),u.getSourceId());
                    error.append("流水未加上，请查看--->用户ID:"+u.getUserId()+"，类型:"+u.getType()+"，size:"+u.getSize()+"，pre_balance:"+u.getPreBalance()+"，post_balance:"+u.getPostBalance()+"，订单ID:"+u.getSourceId()).append("</br>");
                }
            }
        }
        return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
    }

    @Override
    public JsonResult getUserBalance(String userId, String time) {
        List<UserBalanceChain> resUserBalance = accountDao.getUserBalanceByUser(userId);
        List<UserBalanceChain> resUserParterBalance = accountDao.getUserPartnerBalanceByUser(userId);;
        List<UserBalanceChain> res = new ArrayList<UserBalanceChain>();
        for (UserBalanceChain u : resUserBalance
        ) {
            try {
                JSONObject j = new JSONObject();
                j.put("balance", u.getBalance());
                j.put("hold", u.getHold());
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
                redisService.set("user_balance:"+userId, j);
                res.add(u);
            } catch (Exception e) {
                return new JsonResult<>(0, "账号同步redis失败！");
            }
        }
        res.add(new UserBalanceChain());
        for (UserBalanceChain u : resUserParterBalance) {
            try {
                JSONObject j = new JSONObject();
                j.put("balance", u.getBalance());
                j.put("hold", u.getHold());
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
                redisService.set("user_partner_balance:"+userId, j);
                res.add(u);
            } catch (Exception e) {
                return new JsonResult<>(0, "账号同步redis失败！");
            }
        }

        log.info("用户:{},账户user_balance:{},user_partner_balance:{}", userId, resUserBalance,resUserParterBalance);
        return new JsonResult<>(0, res);
    }

    @Override
    public JsonResult getAllUserBalance() {
        //查询user_balance表全部账户总的balance和hold值
        UserBalanceChain userBalance = accountDao.getAllUserBalanceTotal();
        userBalance.setUserId("user_balance");

        JSONObject userBalanceJ = new JSONObject();
        userBalanceJ.put("balance", userBalance.getBalance());
        userBalanceJ.put("hold", userBalance.getHold());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(userBalanceJ.getClass()));
        redisService.set("user_balance:all", userBalanceJ);
        log.info("user_balance:all--->{}",userBalanceJ);

        //查询user_partner_balance表全部账户总的balance和hold值
        UserBalanceChain userPartnerBalance =  accountDao.getAllUserPartnerBalanceTotal();
        userPartnerBalance.setUserId("user_partner_balance");

        userBalanceJ.put("balance", userPartnerBalance.getBalance());
        userBalanceJ.put("hold", userPartnerBalance.getHold());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(userBalanceJ.getClass()));
        redisService.set("user_partner_balance:all", userBalanceJ);
        log.info("user_partner_balance:all--->{}",userBalanceJ);

        List<UserBalanceChain> userBalanceChains = new ArrayList<UserBalanceChain>();
        userBalanceChains.add(userBalance);
        userBalanceChains.add(new UserBalanceChain());
        userBalanceChains.add(userPartnerBalance);
        return  new JsonResult<>(0, userBalanceChains);
        /**
        List<UserBalanceChain> resUserBalance = accountDao.getUserBalance();
        List<UserBalanceChain> resUserParterBalance = null;
        List<UserBalanceChain> res = new ArrayList<UserBalanceChain>();
        List<String> userList = new ArrayList<String>();


        for (UserBalanceChain u : resUserBalance
        ) {
            try {
                JSONObject j = new JSONObject();
                j.put("balance", u.getBalance());
                j.put("hold", u.getHold());
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
                redisService.set("user_balance:"+u.getUserId(), j);
                res.add(u);
                userList.add(u.getUserId());
                log.info("用户:{},账户user_balance:{}", u.getUserId(), resUserBalance);

            } catch (Exception e) {
                return new JsonResult<>(0, "账号同步redis失败！");
            }
        }
        //把用户列表存入redis
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(userList.getClass()));
        redisService.set("user_list", userList);

        res.add(new UserBalanceChain());
        for (String str: userList
             ) {
            resUserParterBalance = accountDao.getUserPartnerBalanceByUser(str);
            for (UserBalanceChain u : resUserParterBalance
            ) {
                try {
                    JSONObject j = new JSONObject();
                    j.put("balance", u.getBalance());
                    j.put("hold", u.getHold());
                    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
                    redisService.set("user_partner_balance:"+u.getUserId(), j);
                    res.add(u);
                    log.info("用户:{},账户user_partner_balance:{}", u.getUserId(),resUserParterBalance);

                } catch (Exception e) {
                    return new JsonResult<>(0, "账号同步redis失败！");
                }
            }
        }
        return new JsonResult<>(0, res);
         **/
    }

    @Override
    public List<UserDistributorChain> getUserPartner(String userId) {
        List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);
        return userPartner;
    }

    /**
     *
     * @param userId
     * @param money
     * @param marginType 0-全仓;1-逐仓
     * @return
     */
    @Override
    public String throughPositions(String userId,BigDecimal money,int marginType) {
        StringBuffer stringBuffer = new StringBuffer();
        //用户及合伙人列表
        List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);

        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userBalanceR = (JSONObject) redisService.get("user_balance:"+userId);
        JSONObject userPartnerBalanceR = (JSONObject) redisService.get("user_partner_balance:"+userId);
        if (userBalanceR == null || userPartnerBalanceR == null){
            return "请先同步账户余额！";
        }
        //如果未传balance值，则从redis获取
        money = money != null ? money :
                userPartnerBalanceR.getBigDecimal("balance").add(userPartnerBalanceR.getBigDecimal("hold"));
        //用户是否有返佣
        Boolean isBack = accountUtil.userFeeIsBack(userId);

        if (marginType == 0){
            //总的盈亏
            BigDecimal totalProfit = BigDecimal.ZERO;
            //总的手续费
            BigDecimal totalFee = BigDecimal.ZERO;
            //总的返手续费
            BigDecimal totalFeeBack = BigDecimal.ZERO;

            //最后剩余金额
            BigDecimal moneyPost = BigDecimal.ZERO;
            List<PositionFinishChain> positionList = accountDao.positionFinish(userId);
            if (positionList.size() == 0){
                return "用户:"+userId+",position_finish表无历史仓位！";
            }
            for (PositionFinishChain position : positionList) {
                BigDecimal oneLotSize = accountDao.instruments(position.getSymbol());
                //先计算盈亏
                //多仓
                if (position.getDirection().equalsIgnoreCase("long")){
                    totalProfit = totalProfit.add((position.getClosePrice().subtract(position.getOpenPrice())).multiply(position.getQuantity()).multiply(oneLotSize)).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                }else {//空仓
                    totalProfit = totalProfit.add((position.getOpenPrice().subtract(position.getClosePrice())).multiply(position.getQuantity()).multiply(oneLotSize)).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                }
                totalFee = totalFee.add(position.getClosePrice().multiply(position.getQuantity()).multiply(oneLotSize).multiply(Config.taker)).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
            }
            //用户有手续费返回
            if (isBack){
                BigDecimal rebateUserRatio = accountUtil.rebateUserRatio(userId);
                totalFeeBack = totalFee.multiply(rebateUserRatio).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
            }
            moneyPost = money.add(totalFeeBack).add(totalProfit).subtract(totalFee).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
            log.info("账户初始金额:{}，盈亏金额:{}，手续费:{}，返的手续费:{}，最后金额:{}",money,totalProfit,totalFee,totalFeeBack,moneyPost);
            stringBuffer.append("账户初始金额:"+money+"，盈亏金额:"+totalProfit+"，手续费:"+totalFee+"，返的手续费:"+totalFeeBack+"，最后金额:"+moneyPost).append("</br>");
            if (moneyPost.compareTo(BigDecimal.ZERO) == -1){
                String tmp = accountUtil.throughBack(userPartner,moneyPost);
                log.info("用户:{}，合伙人的穿仓回退--->{}",userId,tmp);
                stringBuffer.append("用户:"+userId+"，合伙人的穿仓回退--->"+tmp).append("</br>");
            }
        }else {
            List<PositionActionChain> positionList = accountDao.positionAction(userId,null,"coerceClose");
            if (positionList.size() == 0){
                return "用户:"+userId+",position_action表无仓位！";
            }
            //逐仓是按照单个仓位，计算的穿仓价格，和该仓位的保证金有关
            //获取仓位的保证金
            BigDecimal margin = BigDecimal.ZERO;
            BigDecimal profit = BigDecimal.ZERO;
            BigDecimal fee = BigDecimal.ZERO;
            BigDecimal feeBack = BigDecimal.ZERO;
            BigDecimal marginProfit = BigDecimal.ZERO;
            BigDecimal balance = BigDecimal.ZERO;
            //逐仓获取账户余额
            for (PositionActionChain position : positionList) {
                BigDecimal oneLotSize = accountDao.instruments(position.getSymbol());
                margin =  position.getMargin();
                //计算盈亏
                //多仓
                if (position.getDirection().equalsIgnoreCase("long")){
                    profit = (position.getClosePrice().subtract(position.getOpenPrice())).multiply(position.getQuantity()).multiply(oneLotSize).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                }else {//空仓
                    profit = (position.getOpenPrice().subtract(position.getClosePrice())).multiply(position.getQuantity()).multiply(oneLotSize).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                }
                fee = position.getClosePrice().multiply(position.getQuantity()).multiply(oneLotSize).multiply(Config.taker).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                //用户有手续费返回
                if (isBack){
                    BigDecimal rebateUserRatio = accountUtil.rebateUserRatio(userId);
                    feeBack = fee.multiply(rebateUserRatio).setScale(Config.newScale,BigDecimal.ROUND_DOWN);
                }
                marginProfit = money.add(margin).add(profit).subtract(fee).add(feeBack);
                log.info("账户初始保证金:{},盈亏金额:{},手续费:{},返的手续费:{},最后金额:{}",margin,profit,fee,feeBack,marginProfit);
                stringBuffer.append("账户初始金额:"+margin+",盈亏金额:"+profit+",手续费:"+fee+",返的手续费:"+feeBack+",最后金额:"+marginProfit).append("</br>");
                if (marginProfit.compareTo(BigDecimal.ZERO) == -1){
                    String tmp = accountUtil.throughBack(userPartner,marginProfit);
                    log.info("用户:{},合伙人的穿仓回退:{}",userId,tmp);
                    stringBuffer.append("用户:"+userId+",合伙人的穿仓回退:"+tmp).append("</br>");
                }
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public String updateMarket(String symbol, String price) {
        String url = "http://10.0.50.148:8081/test/market?symbol="+symbol+"&price="+price;
        try {
            httpUtil.get(url);
            return "推送成功!";
        }catch (Exception e){
            return "推送失败!";
        }
    }

    //废弃
    public String destroy_allUserCheck() {
        //序列化
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new ArrayList().getClass()));
        List<String> userListR = (List) redisService.get("user_list");
        log.info("user_list:{}",userListR);
        String userId = "";
        String time = "";
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        if (userListR == null){
            log.info("用户:{},用户列表为空,请先同步用户账单!",userId);
            return "用户列表为空,请先同步用户账单!";
        }
        for (String str:userListR) {
            log.info("--------" + str + "对账,开始--------");
            stringBuffer.append("--------" + str + "对账,开始--------").append("</br>");
            userId = str;
            time = time == null ? Config.startTime : time;
            Boolean flag = false;
            try {
                //redis获取对账前的金额
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
                JSONObject userBalanceR = (JSONObject) redisService.get("user_balance:"+userId);
                JSONObject userPartnerBalanceR = (JSONObject) redisService.get("user_partner_balance:"+userId);
                if (userBalanceR == null || userPartnerBalanceR == null){
                    log.info("用户:{},请先同步账户余额！",userId);
                    return "请先同步账户余额！";
                }
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
                if (totalPost.compareTo(totalPostCalc) != 0){
                    flag = true;
                    error.append("账号总额不正确，请检查!").append("</br>");
                }

                //用户及合伙人列表
                List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);
                //查询用户的每条流水
                List<UserBillChain> userBillRes = accountDao.getUserBillByUser(userId, time) ;
                log.info("用户:{},流水:{}",userId,userBillRes);
                if (userBillRes.size() == 0){
                    return stringBuffer.append("用户暂无流水！").toString();
                }
                log.info("--------" + userId + "流水,开始--------");
                stringBuffer.append("--------" + userId + "流水,开始--------").append("</br>");
                String[] s ;
                for (UserBillChain userBillChain : userBillRes) {
                    log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                    stringBuffer.append("<font color=\"#67C23A\">类型:</font> " + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
                    //交易类型
                    int type = userBillChain.getType();
                    //平仓盈亏
                    if (type == 4){
                        //如果合伙人的列表长度为2，表示该用户只有默认合伙人
                        if (userPartner.size() == 2){
                            s = profitCalc.defaultPartnerProfit(userPartner,userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length ==2){
                                error.append(s[1]);
                            }
                        }else {//该用户有多级合伙人
                            s = profitCalc.morePartnerProfit(userPartner,userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length ==2){
                                error.append(s[1]);
                            }
                        }
                        //开平仓的手续费
                    }else if (type == 51 || type == 52){
                        s= feeCalc.partnerFee(userPartner,userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length ==2){
                            error.append(s[1]);
                        }
                        //穿仓回补
                    }else if (type == 54){
                        //先计算穿仓回补的钱是不是正确
                        throughPositionsCalc.throughBack(userBillChain);
                        if (userPartner.size() == 2) {
                            s = throughPositionsCalc.defaultPartnerBack(userPartner, userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length == 2) {
                                error.append(s[1]);
                            }
                        }else {
                            s = throughPositionsCalc.morePartnerBack(userPartner, userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length == 2) {
                                error.append(s[1]);
                            }
                        }
                    }else {
                        continue;
                    }
                }
                log.info("--------" + userId + "流水,结束--------");
                stringBuffer.append("--------" + userId + "流水,结束--------").append("</br>");
            } catch (Exception e) {
                log.error(e.toString());
                return "账单未更新，请稍后重试!";
            }
            log.info("--------" + str + "对账,结束--------");
            stringBuffer.append("--------" + str + "对账,结束--------").append("</br>");
        }
        return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
    }
}
