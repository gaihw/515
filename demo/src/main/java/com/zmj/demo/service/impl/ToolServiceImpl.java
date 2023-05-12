package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.CollectionUtil;
import com.zmj.demo.common.HttpUtil;
import com.zmj.demo.common.SqlUtil;
import com.zmj.demo.common.dev1.*;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev.SmsEmailCode;
import com.zmj.demo.dao.test.AccountDao;
import com.zmj.demo.dao.test.SmsEmailCodeDao;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private SmsEmailCode smsEmailCode;

    @Autowired
    private SmsEmailCodeDao smsEmailCodeDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RedisService redisService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SqlUtil sqlUtils;

    @Autowired
    private FeeCalc feeCalc;

    @Autowired
    private ProfitCalc profitCalc;

    @Autowired
    private PositionsCalc positionsCalc;

    @Autowired
    private AccountUtil accountUtil;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private BalanceCalc balanceCalc;

    @Autowired
    private UserCheckThread userCheckThread;

    @Autowired
    private CollectionUtil collectionUtil;

    /**
     * 获取验证码
     *
     * @param type 0-test;1-dev1
     * @return
     */
    @Override
    public List<SmsEmailcodeDomain> getList(int type) {
        if (type == 0) {
            return smsEmailCodeDao.getSmsList();
        } else {
            return smsEmailCode.getList();
        }

    }

    @Override
    public String userCheck(String userId, String money, String time) {
        StringBuffer stringBuffer = new StringBuffer();
        time = time == null ? Config.startTime : time;
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        try {
            //redis获取对账前的金额
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
            JSONObject userBalanceR = (JSONObject) redisService.get("user_balance:" + userId);
            if (userBalanceR == null) {
                return "请先同步user_balance账户余额！";
            }
            String balanceRes = balanceCalc.balanceCalc(userBalanceR, userId, time);
            if (balanceRes.contains("账号总额不正确")) {
                error.append(balanceRes).append("</br>");
            }
            stringBuffer.append(balanceRes).append("</br>");
            //如果user_partner_balance表无用户数据，则不计算流水的变化
            if (accountDao.getUserPartnerBalanceByUser(userId) == null) {
                log.info("用户:{}，user_partner_balance表无数据，不做流水校验！", userId);
                stringBuffer.append("用户:" + userId + "，user_partner_balance表无数据，不做流水校验！").append("</br></br>");
            } else {
//                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
                JSONObject userPartnerBalanceR = (JSONObject) redisService.get("user_partner_balance:" + userId);
                if (userPartnerBalanceR == null) {
                    return "请先同步user_partner_balance账户余额！";
                }
                String partnerBalanceRes = balanceCalc.partnerBalanceCalc(userPartnerBalanceR, userId, time);
                if (partnerBalanceRes.contains("账号总额不正确")) {
                    error.append(partnerBalanceRes).append("</br>");
                }
                stringBuffer.append(partnerBalanceRes).append("</br>");
            }
            JSONObject otcUserBalanceR = (JSONObject) redisService.get("otc_user_balance:" + userId);
            if (otcUserBalanceR == null) {
                return "请先同步otc_user_balance账户余额！";
            }
            JSONObject assetUserBalanceR = (JSONObject) redisService.get("asset_user_balance:" + userId);
            if (assetUserBalanceR == null) {
                return "请先同步asset_user_balance账户余额！";
            }
            String otcUserBalanceRes = balanceCalc.assertAndOtcBalance(otcUserBalanceR, assetUserBalanceR, userId, time);
            if (otcUserBalanceRes.contains("账号总额不正确")) {
                error.append(otcUserBalanceRes);
            }
            stringBuffer.append(otcUserBalanceRes).append("</br>");

            //查询用户的每条流水
            List<UserBillChain> userBillRes = accountDao.getUserBillByUser(userId, time);
            log.info("用户:{},流水:{}", userId, userBillRes);
            if (userBillRes.size() == 0) {
                return stringBuffer.append("用户暂无流水！").toString();
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");

            if (false) {
                //用户及合伙人列表
                List<UserDistributorChain> userPartner = null;
                String[] s;
                for (UserBillChain userBillChain : userBillRes) {
                    log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                    stringBuffer.append("<font color=\"#67C23A\">类型:</font> " + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
//                    //是否使用多线程执行
//                    if (true) {
//                        //异步执行
//                        CompletableFuture<String[]> userCheckRun = userCheckThread.run(userId, userBillChain, userPartner);
////                    CompletableFuture.allOf(userCheckRun).join();
//                        s = userCheckRun.get();
//                        stringBuffer.append(s[0]);
//                        if (s.length ==2){
//                            error.append(s[1]);
//                        }
//                        continue;
//                    }else {
//
//                    }
                    //交易类型
                    int type = userBillChain.getType();
                    //平仓盈亏
                    if (type == 3) {
                        PositionActionChain positionActionForMargin = accountDao.positionActionForMargin(userBillChain.getUserId(), userBillChain.getSourceId());
                        if (positionActionForMargin == null) {
                            log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                            stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                            continue;
                        }
                        BigDecimal oneLotSize = accountDao.instruments(positionActionForMargin.getSymbol());
                        BigDecimal marginTmp = positionActionForMargin.getOpenPrice().multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()), Config.newScale, BigDecimal.ROUND_DOWN);
                        //先根据position_action表计算保证金
                        if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                            //如果position_action表计算保证金不正确，再根据撮合表中的数据校验
                            MatchResultFingerprintChain matchResultFingerprintChain = accountDao.matchResultFingerprint(userBillChain.getSourceId());
                            if (matchResultFingerprintChain == null) {
                                log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}", userId, userBillChain.getSourceId(), userBillChain.getType(), userBillChain.getSize(), marginTmp);
                                error.append("保证金计算不正确--->,用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + marginTmp).append("</br>");
                            }
                            JSONArray items = JSONObject.parseObject(matchResultFingerprintChain.getFMatchResultContent()).getJSONArray("items");
                            marginTmp = items.getJSONObject(items.size() - 1).getBigDecimal("price")
                                    .multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()), Config.newScale, BigDecimal.ROUND_DOWN);
                            //如果两个计算都不正确，则记录
                            if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}", userId, userBillChain.getSourceId(), userBillChain.getType(), userBillChain.getSize(), marginTmp);
                                error.append("保证金计算不正确--->,用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + marginTmp).append("</br>");
                            }
                        }
                    } else if (type == 4) {
                        userPartner = sqlUtils.getUserPartner(userId);
                        List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(), userBillChain.getSourceId(), null);
                        //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
                        if (positionAction.size() == 0) {
                            log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                            stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                            continue;
                        }
                        BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                        BigDecimal profit = BigDecimal.ZERO;
                        //多仓
                        if (positionAction.get(0).getDirection().equalsIgnoreCase("LONG")) {
                            //(平仓价格-开仓价格)*数量*面值
                            profit = (positionAction.get(0).getClosePrice().subtract(positionAction.get(0).getOpenPrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                        } else {
                            //(开仓价格-平仓价格)*数量*面值
                            profit = (positionAction.get(0).getOpenPrice().subtract(positionAction.get(0).getClosePrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                        }
                        //全仓
                        if (positionAction.get(0).getMarginType().equalsIgnoreCase("CROSSED")) {
                            if (userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                error.append("盈亏计算不正确--->,用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                            }
                        } else {
                            //盈亏+保证金,逐仓流水，把保证金和盈亏合到一起计算了
                            profit = profit.add(positionAction.get(0).getMargin());
                            if (userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                error.append("盈亏计算不正确--->,用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                            }
                        }
                        //如果合伙人的列表长度为2，表示该用户只有默认合伙人
                        if (userPartner.size() == 2) {
                            s = profitCalc.defaultPartnerProfit(userPartner, userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length == 2) {
                                error.append(s[1]);
                            }
                        } else {//该用户有多级合伙人
                            s = profitCalc.morePartnerProfit(userPartner, userBillChain);
                            stringBuffer.append(s[0]);
                            if (s.length == 2) {
                                error.append(s[1]);
                            }
                        }
                        //开平仓的手续费
                    } else if (type == 16) {
                        log.info("分佣金额余额加减--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getPreBalance(), userBillChain.getSize(), userBillChain.getPostBalance());
                        stringBuffer.append("分佣金额余额加减--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",之前余额:" + userBillChain.getPreBalance() + ",分佣金额:" + userBillChain.getSize() + ",之后余额:" + userBillChain.getPostBalance()).append("</br>");
                        if (userBillChain.getPreBalance().add(userBillChain.getSize()).compareTo(userBillChain.getPostBalance()) != 0) {
                            log.info("分佣金额余额加减不正确--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getPreBalance(), userBillChain.getSize(), userBillChain.getPostBalance());
                            error.append("分佣金额余额加减不正确--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",之前余额:" + userBillChain.getPreBalance() + ",分佣金额:" + userBillChain.getSize() + ",之后余额:" + userBillChain.getPostBalance()).append("</br>");
                        }
                        //手续费返佣
                    } else if (type == 51 || type == 52) {
                        userPartner = sqlUtils.getUserPartner(userId);
                        List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(), userBillChain.getSourceId(), null);
                        //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
                        if (positionAction.size() == 0) {
                            log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                            stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                            continue;
                        }
                        BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                        BigDecimal fee = BigDecimal.ZERO;
                        //先计算手续费
                        if (type == 51) {
                            //开仓存在多笔成交的情况
                            if (positionAction.size() == 1) {
                                fee = positionAction.get(0).getOpenPrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                                if (userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                    log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                    error.append("开仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                }
                            } else {
                                //手续费正确标识,true代表手续费计算不正确
                                Boolean feeFlag = true;
                                for (PositionActionChain pa : positionAction) {
                                    fee = pa.getOpenPrice().multiply(pa.getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                                    if (fee.abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0) {
                                        feeFlag = false;
                                        break;
                                    }
                                }
                                if (feeFlag) {
                                    log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                    error.append("开仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                }
                            }
                        } else {
                            fee = positionAction.get(0).getClosePrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                            if (userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                log.info("平仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                error.append("平仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                            }
                        }
                        s = feeCalc.partnerFee(userPartner, userBillChain);
                        stringBuffer.append(s[0]);
                        if (s.length == 2) {
                            error.append(s[1]);
                        }
                        //穿仓回补
                    } else if (type == 54) {
                        //先计算穿仓回补的钱是不是正确
                        positionsCalc.partnerBack(userBillChain);
//                    if (userPartner.size() == 2) {
//                        s = positionsCalc.defaultPartnerBack(userPartner, userBillChain);
//                        stringBuffer.append(s[0]);
//                        if (s.length == 2) {
//                            error.append(s[1]);
//                        }
//                    }else {
//                        s = positionsCalc.morePartnerBack(userPartner, userBillChain);
//                        stringBuffer.append(s[0]);
//                        if (s.length == 2) {
//                            error.append(s[1]);
//                        }
//                    }
                        //爆仓剩余金额，返回给默认合伙人
                    } else if (type == 59) {
                        UserBillChain partnerBillJb = accountDao.getUserBill(Config.high_partner, userBillChain.getSourceId(), 59);
                        log.info("爆仓剩余金额，返回给默认合伙人--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getSize(), partnerBillJb.getSize());
                        stringBuffer.append("爆仓剩余金额，返回给默认合伙人--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",爆仓剩余金额:" + userBillChain.getSize() + ",返还金额:" + partnerBillJb.getSize()).append("</br>");
                        if (userBillChain.getSize().abs().compareTo(partnerBillJb.getSize().abs()) != 0) {
                            log.info("爆仓剩余金额，返回给默认合伙人不正确--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getSize(), partnerBillJb.getSize());
                            error.append("爆仓剩余金额，返回给默认合伙人不正确--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",爆仓剩余金额:" + userBillChain.getSize() + ",返还金额:" + partnerBillJb.getSize()).append("</br>");
                        }
                    } else {
                        continue;
                    }
                }
            } else {
                //多线程执行
                List<List<UserBillChain>> groupList = collectionUtil.partition(userBillRes, 10);
                CountDownLatch countDownLatch = new CountDownLatch(groupList.size());
                ExecutorService executorService = Executors.newFixedThreadPool(groupList.size());
                // 根据分组长度循环处理
                for (int j = 0; j < groupList.size(); j++) {
                    int finalI = j;
                    executorService.execute(() -> {
                        List<UserBillChain> cserBillChainGroup = groupList.get(finalI);
                        List<UserDistributorChain> userPartner = null;
                        String[] s;
                        for (UserBillChain userBillChain : cserBillChainGroup) {
                            log.info("{}:{}", sqlUtils.getState(userBillChain.getType()), userBillChain.getSize());
                            stringBuffer.append("<font color=\"#67C23A\">类型:</font> " + sqlUtils.getState(userBillChain.getType()) + ",金额:" + userBillChain.getSize()).append("</br>");
                            //交易类型
                            int type = userBillChain.getType();
                            //平仓盈亏
                            if (type == 3) {
                                PositionActionChain positionActionForMargin = accountDao.positionActionForMargin(userBillChain.getUserId(), userBillChain.getSourceId());
                                if (positionActionForMargin == null) {
                                    log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                                    stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                                    continue;
                                }
                                BigDecimal oneLotSize = accountDao.instruments(positionActionForMargin.getSymbol());
                                BigDecimal marginTmp = BigDecimal.ZERO;
                                //如果是全仓，margin保证金为0,逐仓需要计算
                                if (positionActionForMargin.getMarginType().equalsIgnoreCase("FIXED")){
                                    marginTmp = positionActionForMargin.getOpenPrice().multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()), Config.newScale, BigDecimal.ROUND_DOWN);
                                }
                                //先根据position_action表计算保证金
                                if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                    //如果position_action表计算保证金不正确，再根据撮合表中的数据校验
                                    MatchResultFingerprintChain matchResultFingerprintChain = accountDao.matchResultFingerprint(userBillChain.getSourceId());
                                    if (matchResultFingerprintChain == null) {
                                        log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}", userId, userBillChain.getSourceId(), userBillChain.getType(), userBillChain.getSize(), marginTmp);
                                        error.append("保证金计算不正确--->,用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + marginTmp).append("</br>");
                                    }
                                    JSONArray items = JSONObject.parseObject(matchResultFingerprintChain.getFMatchResultContent()).getJSONArray("items");
                                    marginTmp = items.getJSONObject(items.size() - 1).getBigDecimal("price")
                                            .multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()), Config.newScale, BigDecimal.ROUND_DOWN);
                                    //如果两个计算都不正确，则记录
                                    if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                        log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}", userId, userBillChain.getSourceId(), userBillChain.getType(), userBillChain.getSize(), marginTmp);
                                        error.append("保证金计算不正确--->,用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + marginTmp).append("</br>");
                                    }
                                }
                            } else if (type == 4) {
                                userPartner = sqlUtils.getUserPartner(userId);
                                List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(), userBillChain.getSourceId(), null);
                                //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
                                if (positionAction.size() == 0) {
                                    log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                                    stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                                    continue;
                                }
                                BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                                BigDecimal profit = BigDecimal.ZERO;
                                //多仓
                                if (positionAction.get(0).getDirection().equalsIgnoreCase("LONG")) {
                                    //(平仓价格-开仓价格)*数量*面值
                                    profit = (positionAction.get(0).getClosePrice().subtract(positionAction.get(0).getOpenPrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                                } else {
                                    //(开仓价格-平仓价格)*数量*面值
                                    profit = (positionAction.get(0).getOpenPrice().subtract(positionAction.get(0).getClosePrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize);
                                }
                                //全仓
                                if (positionAction.get(0).getMarginType().equalsIgnoreCase("CROSSED")) {
                                    if (userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                        log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                        error.append("盈亏计算不正确--->,用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                    }
                                } else {
                                    //盈亏+保证金,逐仓流水，把保证金和盈亏合到一起计算了
                                    profit = profit.add(positionAction.get(0).getMargin());
                                    if (userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                        log.info("盈亏计算不正确--->,用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                        error.append("盈亏计算不正确--->,用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + profit.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                    }
                                }
                                //如果合伙人的列表长度为2，表示该用户只有默认合伙人
                                if (userPartner.size() == 2) {
                                    s = profitCalc.defaultPartnerProfit(userPartner, userBillChain);
                                    stringBuffer.append(s[0]);
                                    if (s.length == 2) {
                                        error.append(s[1]);
                                    }
                                } else {//该用户有多级合伙人
                                    s = profitCalc.morePartnerProfit(userPartner, userBillChain);
                                    stringBuffer.append(s[0]);
                                    if (s.length == 2) {
                                        error.append(s[1]);
                                    }
                                }
                                //开平仓的手续费
                            } else if (type == 16) {
                                log.info("分佣金额余额加减--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getPreBalance(), userBillChain.getSize(), userBillChain.getPostBalance());
                                stringBuffer.append("分佣金额余额加减--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",之前余额:" + userBillChain.getPreBalance() + ",分佣金额:" + userBillChain.getSize() + ",之后余额:" + userBillChain.getPostBalance()).append("</br>");
                                if (userBillChain.getPreBalance().add(userBillChain.getSize()).compareTo(userBillChain.getPostBalance()) != 0) {
                                    log.info("分佣金额余额加减不正确--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getPreBalance(), userBillChain.getSize(), userBillChain.getPostBalance());
                                    error.append("分佣金额余额加减不正确--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",之前余额:" + userBillChain.getPreBalance() + ",分佣金额:" + userBillChain.getSize() + ",之后余额:" + userBillChain.getPostBalance()).append("</br>");
                                }
                                //手续费返佣
                            } else if (type == 51 || type == 52) {
                                userPartner = sqlUtils.getUserPartner(userId);
                                List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(), userBillChain.getSourceId(), null);
                                //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
                                if (positionAction.size() == 0) {
                                    log.info("position_action表无此流水---用户:{},订单:{},类型:{}", userId, userBillChain.getSourceId(), type);
                                    stringBuffer.append("position_action表无此流水---用户:" + userId + ",订单:" + userBillChain.getSourceId() + ",类型:" + type).append("</br>");
                                    continue;
                                }
                                BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
                                BigDecimal fee = BigDecimal.ZERO;
                                //先计算手续费
                                if (type == 51) {
                                    //开仓存在多笔成交的情况
                                    if (positionAction.size() == 1) {
                                        fee = positionAction.get(0).getOpenPrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                                        if (userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                            log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                            error.append("开仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                        }
                                    } else {
                                        //手续费正确标识,true代表手续费计算不正确
                                        Boolean feeFlag = true;
                                        for (PositionActionChain pa : positionAction) {
                                            fee = pa.getOpenPrice().multiply(pa.getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                                            if (fee.abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0) {
                                                feeFlag = false;
                                                break;
                                            }
                                        }
                                        if (feeFlag) {
                                            log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                            error.append("开仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                        }
                                    }
                                } else {
                                    fee = positionAction.get(0).getClosePrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                                    if (userBillChain.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                                        log.info("平仓手续费计算不正确--->用户:{},订单:{},类型:{}", userBillChain.getUserId(), userBillChain.getSourceId(), userBillChain.getType());
                                        error.append("平仓手续费计算不正确--->用户:" + userBillChain.getUserId() + ",订单:" + userBillChain.getSourceId() + ",类型:" + userBillChain.getType() + ",数据库:" + userBillChain.getSize() + ",计算:" + fee.setScale(Config.newScale, BigDecimal.ROUND_DOWN)).append("</br>");
                                    }
                                }
                                s = feeCalc.partnerFee(userPartner, userBillChain);
                                stringBuffer.append(s[0]);
                                if (s.length == 2) {
                                    error.append(s[1]);
                                }
                                //穿仓回补
                            } else if (type == 54) {
                                //先计算穿仓回补的钱是不是正确
                                positionsCalc.partnerBack(userBillChain);
                                //爆仓剩余金额，返回给默认合伙人
                            } else if (type == 59) {
                                UserBillChain partnerBillJb = accountDao.getUserBill(Config.high_partner, userBillChain.getSourceId(), 59);
                                log.info("爆仓剩余金额，返回给默认合伙人--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getSize(), partnerBillJb.getSize());
                                stringBuffer.append("爆仓剩余金额，返回给默认合伙人--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",爆仓剩余金额:" + userBillChain.getSize() + ",返还金额:" + partnerBillJb.getSize()).append("</br>");
                                if (userBillChain.getSize().abs().compareTo(partnerBillJb.getSize().abs()) != 0) {
                                    log.info("爆仓剩余金额，返回给默认合伙人不正确--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}", userBillChain.getUserId(), userBillChain.getType(), userBillChain.getSize(), partnerBillJb.getSize());
                                    error.append("爆仓剩余金额，返回给默认合伙人不正确--->用户:" + userBillChain.getUserId() + ",类型:" + userBillChain.getType() + ",爆仓剩余金额:" + userBillChain.getSize() + ",返还金额:" + partnerBillJb.getSize()).append("</br>");
                                }
                            } else {
                                continue;
                            }
                        }
                        countDownLatch.countDown();
                    });
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdown();   //关闭线程池
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
        } catch (Exception e) {
            log.error("用户对账异常:{}", e.toString());
            return "账单对账异常，请稍后重试!";
        }
        return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).append("</br>").toString();

    }

    @Override
    public String allUserCheck() {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        //获取平台交易前的账户总额
        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userAllBalanceR = (JSONObject) redisService.get("user_balance:all");
        if (userAllBalanceR == null) {
            return "请先同步user_balance账户总余额！";
        }
        //操作前，账户金额
        BigDecimal totalPre = userAllBalanceR.getBigDecimal("balance").add(userAllBalanceR.getBigDecimal("hold"));
        //获取流水变化的值
        //数据库流水表聚合统计的值
        BigDecimal userBillTotal = accountDao.getAllUserBillTotal();
        //查询逐仓，type=4的爆仓盈余流水，需要去掉对应的平仓流水账单
        BigDecimal userBillTotalFixed = accountDao.getAllUserBillTotalForFixed();
        userBillTotalFixed = userBillTotalFixed == null ? BigDecimal.ZERO : userBillTotalFixed;
        //获取type=59类型，所用用户爆仓返给默认合伙人的总值
//        BigDecimal userToPartnerThroughBalance = accountDao.getUserToPartnerThroughBalance(Config.high_partner);
//        userToPartnerThroughBalance = userToPartnerThroughBalance == null ? BigDecimal.ZERO : userToPartnerThroughBalance;
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal totalIng = userBillTotal == null ? BigDecimal.ZERO : userBillTotal.subtract(userBillTotalFixed);//.add(userToPartnerThroughBalance);
        //操作后，计算剩余的总金额
        BigDecimal totalPostCalc = totalPre.add(totalIng);
        //操作后，查询数据库金额
        UserBalanceChain balanceHoldJbPost = accountDao.getAllUserBalanceTotal();
        BigDecimal balancePost = balanceHoldJbPost.getBalance();
        BigDecimal holdPost = balanceHoldJbPost.getHold();
        //操作后，数据库总的金额
        BigDecimal totalPost = balancePost.add(holdPost);
        log.info("user_balance操作后，数据库金额，balance:{},hold:{}", balancePost, holdPost);
        log.info("user_balance下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}", totalPre, totalIng, totalPostCalc, totalPost);
        stringBuffer.append("user_balance下单前，账户总金额:" + totalPre + "，balance:" + userAllBalanceR.getBigDecimal("balance") + "，hold:" + userAllBalanceR.getBigDecimal("hold")).append("</br>");
//        stringBuffer.append("user_balance操作后，账户变化金额:" + totalIng+",逐仓爆仓，不统计type=4对应的平仓手续费的流水账单总额:"+userBillTotalFixed+",type=59类型，所有用户爆仓返给默认合伙人的总额:"+userToPartnerThroughBalance).append("</br>");
        stringBuffer.append("user_balance操作后，账户变化金额:" + totalIng + ",逐仓爆仓，不统计type=4对应的平仓手续费的流水账单总额:" + userBillTotalFixed).append("</br>");
        stringBuffer.append("user_balance操作后，计算剩余总金额:" + totalPostCalc).append("</br>");
        stringBuffer.append("user_balance操作后，数据库总金额:" + totalPost + "，balance:" + balancePost + "，hold:" + holdPost).append("</br>");
        if (totalPost.setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(totalPostCalc.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
            flag = true;
            error.append("账号总额不正确，请检查!").append("</br>").toString();
        }
        stringBuffer.append("-----------").append("</br>");

        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userPartnerAllBalanceR = (JSONObject) redisService.get("user_partner_balance:all");
        if (userPartnerAllBalanceR == null) {
            return "请先同步user_partner_balance账户总余额！";
        }
        //操作前，账户金额
        BigDecimal partnerTotalPre = userPartnerAllBalanceR.getBigDecimal("balance").add(userPartnerAllBalanceR.getBigDecimal("hold")).add(userPartnerAllBalanceR.getBigDecimal("freeze"));
        //获取流水变化的值
        //数据库流水表聚合统计的值
        BigDecimal userBillPartnerTotal = accountDao.getAllUserBillToPartnerBalance();
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal partnerTotalIng = userBillPartnerTotal == null ? BigDecimal.ZERO : userBillPartnerTotal;
        //type=59,用户爆仓返给默认合伙人的总额
        BigDecimal defaultPartnerThroughBalance = accountDao.getDefaultPartnerThroughBalance(Config.high_partner);
        defaultPartnerThroughBalance = defaultPartnerThroughBalance == null ? BigDecimal.ZERO : defaultPartnerThroughBalance;

        //操作后，计算剩余的总金额
        BigDecimal partnerTotalPostCalc = partnerTotalPre.add(partnerTotalIng).add(defaultPartnerThroughBalance);
        //操作后，查询数据库金额
        UserBalanceChain partnerBalanceHoldJbPost = accountDao.getAllUserPartnerBalanceTotal();
        BigDecimal partnerBalancePost = partnerBalanceHoldJbPost.getBalance();
        BigDecimal partnerHoldPost = partnerBalanceHoldJbPost.getHold();
        BigDecimal freezePost = partnerBalanceHoldJbPost.getFreeze();
        //操作后，数据库总的金额
        BigDecimal partnertotalPost = partnerBalancePost.add(partnerHoldPost).add(freezePost);
        log.info("user_partner_balance操作后，数据库金额，balance:{},hold:{}", partnerBalancePost, partnerHoldPost);
        log.info("user_partner_balance下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}", partnerTotalPre, partnerTotalIng, partnerTotalPostCalc, partnertotalPost);
        stringBuffer.append("user_partner_balance下单前，账户总金额:" + partnerTotalPre + "，balance:" + userPartnerAllBalanceR.getBigDecimal("balance") + "，hold:" + userPartnerAllBalanceR.getBigDecimal("hold")).append("</br>");
        stringBuffer.append("user_partner_balance操作后，账户变化金额:" + partnerTotalIng + ",type=59类型，所有用户爆仓返给默认合伙人的总额:" + defaultPartnerThroughBalance).append("</br>");
        stringBuffer.append("user_partner_balance操作后，计算剩余总金额:" + partnerTotalPostCalc).append("</br>");
        stringBuffer.append("user_partner_balance操作后，数据库总金额:" + partnertotalPost + "，balance:" + partnerBalancePost + "，hold:" + partnerHoldPost).append("</br>");
        if (partnertotalPost.setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(partnerTotalPostCalc.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
            flag = true;
            error.append("user_partner_balance账号总额不正确，请检查!").append("</br>").toString();
        }
        stringBuffer.append("-----------").append("</br>");

        //redis获取对账前的金额
        JSONObject otcUserAllBalanceR = (JSONObject) redisService.get("otc_user_balance:all");
        if (otcUserAllBalanceR == null) {
            return "请先同步otc_user_balance账户总余额！";
        }
        //redis获取对账前的金额
        JSONObject assetUserAllBalanceR = (JSONObject) redisService.get("asset_user_balance:all");
        if (assetUserAllBalanceR == null) {
            return "请先同步asset_user_balance账户总余额！";
        }
        //操作前，otc_user_balance账户总额和asset_user_balance总额
        BigDecimal otcAndAssetTotalPre = otcUserAllBalanceR.getBigDecimal("balance").add(assetUserAllBalanceR.getBigDecimal("balance"));
        //获取合约账户转出转入到otc和asset账户的流水总额
        BigDecimal userBillOtcAndAssetTotal = accountDao.getAllUserBillToOtcAndAssetBalance();
        //操作后，账户变化金额,如果无流水账单，默认赋值为0
        BigDecimal userBillOtcAndAssetTotalIng = userBillOtcAndAssetTotal == null ? BigDecimal.ZERO : userBillOtcAndAssetTotal;
        //操作后，计算剩余的总金额
        BigDecimal userBillOtcAndAssetTotalPostCalc = otcAndAssetTotalPre.add(userBillOtcAndAssetTotalIng);
        //操作后，查询数据库金额
        UserBalanceChain otcUserBalancePost = accountDao.getAllOtcUserBalanceTotal();
        UserBalanceChain assetUserBalancePost = accountDao.getAllAssetUserBalanceTotal();
        //操作后，数据库总的金额
        BigDecimal otcAndAssetBalancePost = otcUserBalancePost.getBalance().add(assetUserBalancePost.getBalance());

        log.info("otcAndAsset下单前，账户金额:{},操作后，账户变化金额:{},操作后，计算剩余的金额:{},操作后，数据库存储的总金额:{}", otcAndAssetTotalPre, userBillOtcAndAssetTotalIng, userBillOtcAndAssetTotalPostCalc, otcAndAssetBalancePost);
        stringBuffer.append("otcAndAsset下单前，账户总金额:" + otcAndAssetTotalPre + "，otc-balance:" + otcUserAllBalanceR.getBigDecimal("balance") + "，asset-balance:" + assetUserAllBalanceR.getBigDecimal("balance")).append("</br>");
        stringBuffer.append("otcAndAsset操作后，账户变化金额:" + userBillOtcAndAssetTotalIng).append("</br>");
        stringBuffer.append("otcAndAsset操作后，计算剩余总金额:" + userBillOtcAndAssetTotalPostCalc).append("</br>");
        stringBuffer.append("otcAndAsset操作后，数据库总金额:" + otcAndAssetBalancePost + "，otc-balance:" + otcUserBalancePost.getBalance() + "，asset-balance:" + assetUserBalancePost.getBalance()).append("</br>");
        if (otcAndAssetBalancePost.setScale(Config.newScale, BigDecimal.ROUND_DOWN).compareTo(userBillOtcAndAssetTotalPostCalc.setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
            flag = true;
            error.append("otcAndAsset账号总额不正确，请检查!").append("</br>").toString();
        }


        //对type类型为16,27,38,50,54,55,56,58,59的流水账单
        int count = accountDao.getUserBillByTypeCount();
        if (count == 0) {
            stringBuffer.append("无type类型为3,16,27,35,38,50,54,55,56,58,59的流水账单").append("</br>");
            return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
        }
        count = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_UP).intValue();
        for (int i = 0; i < count; i++) {
            List<UserBillChain> userBillChains = accountDao.getUserBillByType(1000 * i);
            for (UserBillChain u : userBillChains) {
                log.info("用户ID:{},类型:{},size:{},pre_balance:{},post_balance:{},订单ID:{}", u.getUserId(), u.getType(), u.getSize(), u.getPreBalance(), u.getPostBalance(), u.getSourceId());
                if (u.getSize().add(u.getPreBalance()).compareTo(u.getPostBalance()) != 0) {
                    log.info("流水未加上，请查看--->用户ID:{},类型:{},size:{},pre_balance:{},post_balance:{},订单ID:{}", u.getUserId(), u.getType(), u.getSize(), u.getPreBalance(), u.getPostBalance(), u.getSourceId());
                    error.append("流水未加上，请查看--->用户ID:" + u.getUserId() + "，类型:" + u.getType() + "，size:" + u.getSize() + "，pre_balance:" + u.getPreBalance() + "，post_balance:" + u.getPostBalance() + "，订单ID:" + u.getSourceId()).append("</br>");
                }
            }
        }
        return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
    }

    @Override
    public JsonResult getUserBalance(String userId, String time) {
        UserBalanceChain resUserBalance = accountDao.getUserBalanceByUser(userId);
        UserBalanceChain resUserParterBalance = accountDao.getUserPartnerBalanceByUser(userId);
        UserBalanceChain resOtcUserBalance = accountDao.getOtcUserBalanceByUser(userId);
        UserBalanceChain resAssetuserBalance = accountDao.getAssetUserBalanceByUser(userId);
        List<UserBalanceChain> res = new ArrayList<UserBalanceChain>();
        try {
            JSONObject j = new JSONObject();
            j.put("balance", resUserBalance.getBalance());
            j.put("hold", resUserBalance.getHold());
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
            redisService.set("user_balance:" + userId, j);
            resUserBalance.setUserId("user_balance");
            res.add(resUserBalance);
        } catch (Exception e) {
            return new JsonResult<>(0, "user_balance账号同步redis失败！");
        }
        res.add(new UserBalanceChain());
        try {
            JSONObject j = new JSONObject();
            if (resUserParterBalance == null) {
                j.put("balance", BigDecimal.ZERO);
                j.put("hold", BigDecimal.ZERO);
                UserBalanceChain u = new UserBalanceChain();
                u.setUserId("user_partner_balance");
                u.setBalance(BigDecimal.ZERO);
                u.setHold(BigDecimal.ZERO);
                res.add(u);
            } else {
                j.put("balance", resUserParterBalance.getBalance());
                j.put("hold", resUserParterBalance.getHold());
                resUserParterBalance.setUserId("user_partner_balance");
                res.add(resUserParterBalance);

            }
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
            redisService.set("user_partner_balance:" + userId, j);
        } catch (Exception e) {
            return new JsonResult<>(0, "user_partner_balance账号同步redis失败！");
        }

        res.add(new UserBalanceChain());
        try {
            JSONObject j = new JSONObject();
            if (resOtcUserBalance == null) {
                j.put("balance", BigDecimal.ZERO);
                UserBalanceChain u = new UserBalanceChain();
                u.setUserId("otc_user_balance");
                u.setBalance(BigDecimal.ZERO);
                res.add(u);
            } else {
                j.put("balance", resOtcUserBalance.getBalance());
                resOtcUserBalance.setUserId("otc_user_balance");
                res.add(resOtcUserBalance);
            }
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
            redisService.set("otc_user_balance:" + userId, j);
        } catch (Exception e) {
            return new JsonResult<>(0, "otc_user_balance账号同步redis失败！");
        }

        res.add(new UserBalanceChain());
        try {
            JSONObject j = new JSONObject();
            if (resAssetuserBalance == null) {
                j.put("balance", BigDecimal.ZERO);
                UserBalanceChain u = new UserBalanceChain();
                u.setUserId("asset_user_balance");
                u.setBalance(BigDecimal.ZERO);
                res.add(u);
            } else {
                j.put("balance", resAssetuserBalance.getBalance());
                resAssetuserBalance.setUserId("asset_user_balance");
                res.add(resAssetuserBalance);
            }
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(j.getClass()));
            redisService.set("asset_user_balance:" + userId, j);

        } catch (Exception e) {
            return new JsonResult<>(0, "assert_user_balance账号同步redis失败！");
        }

        log.info("用户:{},账户user_balance:{},user_partner_balance:{},otc_user_balance:{},assert_user_balance:{}", userId, resUserBalance, resUserParterBalance, resOtcUserBalance, resAssetuserBalance);
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
        log.info("user_balance:all--->{}", userBalanceJ);

        //查询user_partner_balance表全部账户总的balance和hold值
        UserBalanceChain userPartnerBalance = accountDao.getAllUserPartnerBalanceTotal();
        userPartnerBalance.setUserId("user_partner_balance");
        JSONObject userPartnerBalanceJ = new JSONObject();
        userPartnerBalanceJ.put("balance", userPartnerBalance.getBalance());
        userPartnerBalanceJ.put("hold", userPartnerBalance.getHold());
        userPartnerBalanceJ.put("freeze", userPartnerBalance.getFreeze());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(userPartnerBalanceJ.getClass()));
        redisService.set("user_partner_balance:all", userPartnerBalanceJ);
        log.info("user_partner_balance:all--->{}", userPartnerBalanceJ);

        //查询otc_user_balance表全部账户总的balance和hold值
        UserBalanceChain otcUserBalance = accountDao.getAllOtcUserBalanceTotal();
        otcUserBalance.setUserId("otc_user_balance");
        JSONObject otcUserBalanceJ = new JSONObject();
        otcUserBalanceJ.put("balance", otcUserBalance.getBalance());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(otcUserBalanceJ.getClass()));
        redisService.set("otc_user_balance:all", otcUserBalanceJ);
        log.info("otc_user_balance:all--->{}", otcUserBalanceJ);


        //查询asset_user_balance表全部账户总的balance和hold值
        UserBalanceChain assetuserBalance = accountDao.getAllAssetUserBalanceTotal();
        assetuserBalance.setUserId("asset_user_balance");
        JSONObject assetuserBalanceJ = new JSONObject();
        assetuserBalanceJ.put("balance", assetuserBalance.getBalance());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(assetuserBalanceJ.getClass()));
        redisService.set("asset_user_balance:all", assetuserBalanceJ);
        log.info("asset_user_balance:all--->{}", assetuserBalanceJ);

        List<UserBalanceChain> userBalanceChains = new ArrayList<UserBalanceChain>();
        userBalanceChains.add(userBalance);
        userBalanceChains.add(new UserBalanceChain());
        userBalanceChains.add(userPartnerBalance);
        userBalanceChains.add(new UserBalanceChain());
        userBalanceChains.add(otcUserBalance);
        userBalanceChains.add(new UserBalanceChain());
        userBalanceChains.add(assetuserBalance);
        return new JsonResult<>(0, userBalanceChains);

    }

    @Override
    public List<UserDistributorChain> getUserPartner(String userId) {
        List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);
        return userPartner;
    }

    /**
     * 爆仓计算
     *
     * @param userId
     * @param money
     * @param marginType 0-全仓;1-逐仓
     * @return
     */
    @Override
    public String throughPositions(String userId, BigDecimal money, int marginType) {
        StringBuffer stringBuffer = new StringBuffer();
        //用户及合伙人列表
        List<UserDistributorChain> userPartner = sqlUtils.getUserPartner(userId);

        //redis获取对账前的金额
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new JSONObject().getClass()));
        JSONObject userBalanceR = (JSONObject) redisService.get("user_balance:" + userId);
        JSONObject userPartnerBalanceR = (JSONObject) redisService.get("user_partner_balance:" + userId);
        if (userBalanceR == null) {
            return "请先同步账户余额！";
        }
        //如果未传balance值，则从redis获取
        money = money != null ? money :
                userBalanceR.getBigDecimal("balance").add(userBalanceR.getBigDecimal("hold"));
        //用户是否有返佣
        Boolean isBack = accountUtil.userFeeIsBack(userId);

        if (marginType == 0) {
            //总的盈亏
            BigDecimal totalProfit = BigDecimal.ZERO;
            //总的手续费
            BigDecimal totalFee = BigDecimal.ZERO;
            //总的返手续费
            BigDecimal totalFeeBack = BigDecimal.ZERO;

            //最后剩余金额
            BigDecimal moneyPost = BigDecimal.ZERO;
            List<PositionFinishChain> positionList = accountDao.positionFinish(userId);
            if (positionList.size() == 0) {
                return "用户:" + userId + ",position_finish表无历史仓位！";
            }
            for (PositionFinishChain position : positionList) {
                BigDecimal oneLotSize = accountDao.instruments(position.getSymbol());
                //先计算盈亏
                //多仓
                if (position.getDirection().equalsIgnoreCase("long")) {
                    totalProfit = totalProfit.add((position.getClosePrice().subtract(position.getOpenPrice())).multiply(position.getQuantity()).multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                } else {//空仓
                    totalProfit = totalProfit.add((position.getOpenPrice().subtract(position.getClosePrice())).multiply(position.getQuantity()).multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                }
                totalFee = totalFee.add(position.getClosePrice().multiply(position.getQuantity()).multiply(oneLotSize).multiply(Config.taker)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            }
            //用户有手续费返回
            if (isBack) {
                BigDecimal rebateUserRatio = accountUtil.rebateUserRatio(userId);
                totalFeeBack = totalFee.multiply(rebateUserRatio).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            }
            moneyPost = money.add(totalFeeBack).add(totalProfit).subtract(totalFee).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            log.info("账户初始金额:{}，盈亏金额:{}，手续费:{}，返的手续费:{}，最后金额:{}", money, totalProfit, totalFee, totalFeeBack, moneyPost);
            stringBuffer.append("账户初始金额:" + money + "，盈亏金额:" + totalProfit + "，手续费:" + totalFee + "，返的手续费:" + totalFeeBack + "，最后金额:" + moneyPost).append("</br>");
            if (moneyPost.compareTo(BigDecimal.ZERO) == -1) {
                String tmp = accountUtil.throughBack(userPartner, moneyPost);
                log.info("用户:{}，合伙人的穿仓回退--->{}", userId, tmp);
                stringBuffer.append("用户:" + userId + "，合伙人的穿仓回退--->" + tmp).append("</br>");
            }
        } else {
            List<PositionActionChain> positionList = accountDao.positionAction(userId, null, "coerceClose");
            if (positionList.size() == 0) {
                return "用户:" + userId + ",position_action表无仓位！";
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
                margin = position.getMargin();
                //计算盈亏
                //多仓
                if (position.getDirection().equalsIgnoreCase("long")) {
                    profit = (position.getClosePrice().subtract(position.getOpenPrice())).multiply(position.getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                } else {//空仓
                    profit = (position.getOpenPrice().subtract(position.getClosePrice())).multiply(position.getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                }
                fee = position.getClosePrice().multiply(position.getQuantity()).multiply(oneLotSize).multiply(Config.taker).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                //用户有手续费返回
                if (isBack) {
                    BigDecimal rebateUserRatio = accountUtil.rebateUserRatio(userId);
                    feeBack = fee.multiply(rebateUserRatio).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                }
                marginProfit = money.add(margin).add(profit).subtract(fee).add(feeBack);
                log.info("账户初始保证金:{},盈亏金额:{},手续费:{},返的手续费:{},最后金额:{}", margin, profit, fee, feeBack, marginProfit);
                stringBuffer.append("账户初始金额:" + margin + ",盈亏金额:" + profit + ",手续费:" + fee + ",返的手续费:" + feeBack + ",最后金额:" + marginProfit).append("</br>");
                if (marginProfit.compareTo(BigDecimal.ZERO) == -1) {
                    String tmp = accountUtil.throughBack(userPartner, marginProfit);
                    log.info("用户:{},合伙人的穿仓回退:{}", userId, tmp);
                    stringBuffer.append("用户:" + userId + ",合伙人的穿仓回退:" + tmp).append("</br>");
                }
            }
        }
        return stringBuffer.toString();
    }

    @Override
    public String updateMarket(String symbol, String price) {
        String url = "http://10.0.50.148:8081/test/market?symbol=" + symbol + "&price=" + price;
        try {
            httpUtil.get(url);
            return "推送成功!";
        } catch (Exception e) {
            return "推送失败!";
        }
    }

    /**
     * @param userId
     * @param marginType 0-全仓;1-逐仓
     * @return
     */
    @Override
    public String positions(String userId, int marginType) {
        StringBuffer stringBuffer = new StringBuffer();
        //获取用户仓位
        List<PositionChain> positionChains = accountDao.position(userId);
        if (positionChains.size() == 0) {
            return "用户:" + userId + "无持仓数据,请查看!";
        }
        try {
            //获取账户余额
            UserBalanceChain userBalance = accountDao.getUserBalanceByUser(userId);
            //获取标记价格
            redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(BigDecimal.class));
            JSONObject indexPrice = new JSONObject();
            for (PositionChain pc : positionChains) {
                indexPrice.put(pc.getSymbol(), (BigDecimal) redisService.get(pc.getSymbol().substring(0, 3)));
            }

            if (marginType == 0) {
                //计算仓位的总保证金
                BigDecimal totalMargin = positionsCalc.margin(positionChains);
                //计算仓位的总浮动盈亏
                BigDecimal floatProfitLoss = positionsCalc.floatProfitLoss(positionChains, indexPrice);
                //计算仓位的总手续费
                BigDecimal totalFee = positionsCalc.fee(positionChains);
                //过夜费=保证金*0.0002
                BigDecimal overnightCharge = totalMargin.multiply(BigDecimal.valueOf(0.0002));
                //计算风险率,风险率=净值/已用保证金*100=(账户余额available+ SUM(真实的浮动盈亏)-总的手续费totalFee-总的过夜费totalFundingFee)/总的已用保证金totalMargin
                BigDecimal rate = userBalance.getBalance().add(userBalance.getHold()).add(floatProfitLoss).subtract(totalFee).subtract(overnightCharge).multiply(BigDecimal.valueOf(100)).divide(totalMargin, Config.newScale, BigDecimal.ROUND_UP);
                log.info("风险率:{}", rate);
                stringBuffer.append("风险率:" + rate + "，账户余额:" + userBalance.getBalance().add(userBalance.getHold()) + "，总的浮动盈亏:" + floatProfitLoss.setScale(8, BigDecimal.ROUND_UP) + "，总的保证金:" + totalMargin.setScale(8, BigDecimal.ROUND_UP) + "，总的手续费:" + totalFee.setScale(8, BigDecimal.ROUND_UP)).append("</br>");
                stringBuffer.append("</br>");
                for (PositionChain pc : positionChains) {
                    stringBuffer.append("----------币种:" + pc.getSymbol() + "，方向:" + pc.getDirection() + "，杠杆:" + pc.getLeverage() + "，持仓数量:" + pc.getQuantity().setScale(0) + "，标记价格:" + indexPrice.getBigDecimal(pc.getSymbol()) + "----------").append("</br>");
                    BigDecimal oneLotSize = accountDao.instruments(pc.getSymbol());
                    PositionChain positionChain = accountDao.samePosition(userId, pc.getSymbol(), pc.getDirection().equalsIgnoreCase("long") ? "SHORT" : "LONG", pc.getLeverage());
                    //合约方向：多仓-direction=1;空仓-direction=-1
                    BigDecimal direction = BigDecimal.ONE;
                    //预估强平价
                    BigDecimal estimatedStrongPrice = BigDecimal.ZERO;
                    //浮动盈亏=（标记价 - 开仓均价）* 张数 * 合约面值 * 合约方向
                    BigDecimal profitLoss = BigDecimal.ZERO;
                    //浮动盈亏比=(标记价 - 开仓价)/开仓价*杠杆*100
                    BigDecimal profitLossRate = BigDecimal.ZERO;
                    //保证金= 开仓价 * 张数 * 合约面值  / 杠杆
                    BigDecimal margin = pc.getOpenPrice().multiply(pc.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(pc.getLeverage()), Config.newScale, BigDecimal.ROUND_UP);
                    if (pc.getDirection().equalsIgnoreCase("short")) {
                        direction = BigDecimal.valueOf(-1);
                        profitLoss = (pc.getOpenPrice().subtract(indexPrice.getBigDecimal(pc.getSymbol()))).multiply(pc.getQuantity().multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                        profitLossRate = (pc.getOpenPrice().subtract(indexPrice.getBigDecimal(pc.getSymbol()))).multiply(BigDecimal.valueOf(pc.getLeverage())).multiply(BigDecimal.valueOf(100)).divide(pc.getOpenPrice(), Config.newScale, BigDecimal.ROUND_DOWN);
                    } else {
                        profitLoss = (indexPrice.getBigDecimal(pc.getSymbol()).subtract(pc.getOpenPrice())).multiply(pc.getQuantity().multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                        profitLossRate = (indexPrice.getBigDecimal(pc.getSymbol()).subtract(pc.getOpenPrice())).multiply(BigDecimal.valueOf(pc.getLeverage())).multiply(BigDecimal.valueOf(100)).divide(pc.getOpenPrice(), Config.newScale, BigDecimal.ROUND_DOWN);
                    }
                    log.info("用户ID:{},币种:{},方向:{},杠杆:{},保证金{},浮动盈亏:{},浮动盈亏比:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), margin, profitLoss, profitLossRate);
                    stringBuffer.append("保证金:" + margin + "，浮动盈亏:" + profitLoss + "，浮动盈亏比:" + profitLossRate).append("</br>");
                    //无对冲单,预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （张数 * 合约面值）
                    if (positionChain == null) {
                        estimatedStrongPrice = indexPrice.getBigDecimal(pc.getSymbol()).subtract(direction.multiply(userBalance.getBalance().add(userBalance.getHold()).add(floatProfitLoss).subtract(totalFee).subtract(totalMargin.multiply(Config.rate))).divide(pc.getQuantity().multiply(oneLotSize), Config.newScale, BigDecimal.ROUND_DOWN));
                        log.info("无对冲单--->用户ID:{},币种:{},方向:{},杠杆:{},强平价:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), estimatedStrongPrice);
                        stringBuffer.append("无对冲单--->强平价:" + estimatedStrongPrice).append("</br>");
                    } else {//有对冲单
                        //（适用于有对冲单，但不能完全对冲：方向以大仓位为准，算的是净头寸，公式：
                        //预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （abs（张数之差） * 合约面值）
                        //当前仓位的头寸大
                        if (pc.getQuantity().compareTo(positionChain.getQuantity()) == 1) {
                            estimatedStrongPrice = indexPrice.getBigDecimal(pc.getSymbol()).subtract(direction.multiply(userBalance.getBalance().add(userBalance.getHold()).add(floatProfitLoss).subtract(totalFee).subtract(totalMargin.multiply(Config.rate))).divide((pc.getQuantity().subtract(positionChain.getQuantity()).abs()).multiply(oneLotSize), Config.newScale, BigDecimal.ROUND_DOWN));
                            log.info("有对冲单，当前仓位对冲单头寸大--->用户ID:{},币种:{},方向:{},杠杆:{},强平价:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), estimatedStrongPrice);
                            stringBuffer.append("有对冲单，当前仓位对冲单头寸大--->强平价:" + estimatedStrongPrice).append("</br>");
                        } else if (pc.getQuantity().compareTo(positionChain.getQuantity()) == 0) {//完全对冲
                            log.info("完全对冲--->用户ID:{},币种:{},方向:{},杠杆:{},强平价:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), estimatedStrongPrice);
                            stringBuffer.append("完全对冲--->,强平价:0").append("</br>");
                        } else {//当前仓位的对冲单，头寸大
                            if (positionChain.getDirection().equalsIgnoreCase("short")) {
                                direction = BigDecimal.valueOf(-1);
                            } else {
                                direction = BigDecimal.ONE;
                            }
                            estimatedStrongPrice = indexPrice.getBigDecimal(pc.getSymbol()).subtract(direction.multiply(userBalance.getBalance().add(userBalance.getHold()).add(floatProfitLoss).subtract(totalFee).subtract(totalMargin.multiply(Config.rate))).divide((pc.getQuantity().subtract(positionChain.getQuantity()).abs()).multiply(oneLotSize), Config.newScale, BigDecimal.ROUND_DOWN));
                            log.info("有对冲单--->用户ID:{},币种:{},方向:{},杠杆:{},强平价:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), estimatedStrongPrice);
                            stringBuffer.append("有对冲单--->强平价:" + estimatedStrongPrice).append("</br>");
                        }
                    }
                    stringBuffer.append("</br>");
                }
            } else {
                for (PositionChain pc : positionChains) {
                    stringBuffer.append("----------币种:" + pc.getSymbol() + "，方向:" + pc.getDirection() + "，杠杆:" + pc.getLeverage() + "，持仓数量:" + pc.getQuantity().setScale(0) + "，标记价格:" + indexPrice.getBigDecimal(pc.getSymbol()) + "----------").append("</br>");
                    BigDecimal oneLotSize = accountDao.instruments(pc.getSymbol());
                    //合约方向：多仓-direction=1;空仓-direction=-1
                    BigDecimal direction = BigDecimal.ONE;
                    if (pc.getDirection().equalsIgnoreCase("short")) {
                        direction = BigDecimal.valueOf(-1);
                    }
                    //预估强平价
                    BigDecimal estimatedStrongPrice = BigDecimal.ZERO;
                    //浮动盈亏=（标记价 - 开仓均价）* 张数 * 合约面值 * 合约方向
                    BigDecimal profitLoss = BigDecimal.ZERO;
                    //浮动盈亏比=(标记价 - 开仓价)/开仓价*杠杆*100
                    BigDecimal profitLossRate = BigDecimal.ZERO;
                    //保证金= 开仓价 * 张数 * 合约面值  / 杠杆
//                    BigDecimal margin = pc.getOpenPrice().multiply(pc.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(pc.getLeverage()),Config.newScale,BigDecimal.ROUND_UP);
                    BigDecimal margin = pc.getMargin();
                    if (pc.getDirection().equalsIgnoreCase("short")) {
                        direction = BigDecimal.valueOf(-1);
                        profitLoss = (pc.getOpenPrice().subtract(indexPrice.getBigDecimal(pc.getSymbol()))).multiply(pc.getQuantity().multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                        profitLossRate = (pc.getOpenPrice().subtract(indexPrice.getBigDecimal(pc.getSymbol()))).multiply(BigDecimal.valueOf(pc.getLeverage())).multiply(BigDecimal.valueOf(100)).divide(pc.getOpenPrice(), Config.newScale, BigDecimal.ROUND_DOWN);
                    } else {
                        profitLoss = (indexPrice.getBigDecimal(pc.getSymbol()).subtract(pc.getOpenPrice())).multiply(pc.getQuantity().multiply(oneLotSize)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                        profitLossRate = (indexPrice.getBigDecimal(pc.getSymbol()).subtract(pc.getOpenPrice())).multiply(BigDecimal.valueOf(pc.getLeverage())).multiply(BigDecimal.valueOf(100)).divide(pc.getOpenPrice(), Config.newScale, BigDecimal.ROUND_DOWN);
                    }
                    log.info("用户ID:{},币种:{},方向:{},杠杆:{},浮动盈亏:{},浮动盈亏比:{}", userId, pc.getSymbol(), pc.getDirection(), pc.getLeverage(), profitLoss, profitLossRate);
                    stringBuffer.append("保证金:" + margin + "，浮动盈亏:" + profitLoss + "，浮动盈亏比:" + profitLossRate).append("</br>");
                    //预估平仓手续费 = 手续费率 * 委托数量 * 合约面值 * 预估成交价  或  预估平仓手续费 = 杠杆 * 保证金 * 手续费率
                    BigDecimal estimatedFee = BigDecimal.valueOf(pc.getLeverage()).multiply(margin).multiply(Config.taker);
                    //逐仓预估强平价 =( (风险率* sum(仓位保证金)-sum(仓位保证金) +资金费率计算金额 + 预估平仓手续费 )/(张数 * 面值*合约方向))+开仓价
                    estimatedStrongPrice = pc.getOpenPrice().add(direction.multiply((Config.rate.multiply(margin)).subtract(margin).add(estimatedFee)).divide(pc.getQuantity().multiply(oneLotSize), Config.newScale, BigDecimal.ROUND_DOWN));
                    stringBuffer.append("强平价:" + estimatedStrongPrice).append("</br>");
                    stringBuffer.append("</br>");
                }
            }
        } catch (Exception e) {
            log.info("仓位计算报错:{}", e.toString());
            return "仓位计算报错,请稍后!";
        }
        return stringBuffer.toString();
    }

    /**
     * 获取用户信息
     *
     * @param type 0-手机号;1-用户ID
     * @param data
     * @return
     */
    @Override
    public String userInfo(int type, String data) {
        StringBuffer stringBuffer = new StringBuffer();
        UserInfoDataChain userInfoDataChain;
        if (type == 0) {
            userInfoDataChain = accountDao.getUserInfo(null, data);
        } else {
            userInfoDataChain = accountDao.getUserInfo(data, null);
        }
        if (userInfoDataChain == null) {
            return "无此用户！";
        }
        stringBuffer.append("用户ID:" + userInfoDataChain.getUserId()).append("</br>");
        stringBuffer.append("手机号:" + userInfoDataChain.getMobile()).append("</br>");
        stringBuffer.append("余额:" + userInfoDataChain.getBalance()).append("</br>");
        stringBuffer.append("冻结:" + userInfoDataChain.getHold()).append("</br>");
        stringBuffer.append("otc余额:" + BigDecimal.ZERO).append("</br>");
        stringBuffer.append("asset余额:" + BigDecimal.ZERO).append("</br>");

        return stringBuffer.toString();
    }

    @Override
    public String deposit(String userId, String data) {
        try {
            int result = accountDao.deposit(userId, data);
            if (result == 1) {
                return "充值成功!";
            } else {
                return "充值失败!";
            }
        } catch (Exception e) {
            log.info("充值失败:" + e.toString());
        }
        return "充值成功!";
    }
}
