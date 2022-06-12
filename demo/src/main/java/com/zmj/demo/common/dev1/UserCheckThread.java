package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.SqlUtil;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.MatchResultFingerprintChain;
import com.zmj.demo.domain.dev1.PositionActionChain;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Configuration
public class UserCheckThread {

    @Autowired
    private AccountDao accountDao;

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
    private BalanceCalc balanceCalc;

    @Async("asynExecutor")
    public CompletableFuture<String[]> run(String userId, UserBillChain userBillChain, List<UserDistributorChain> userPartner) {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean flag = false;
        StringBuffer error = new StringBuffer();
        //交易类型
        int type = userBillChain.getType();
        String[] s ;
        //平仓盈亏
        if (type == 3){
            PositionActionChain positionActionForMargin = accountDao.positionActionForMargin(userBillChain.getUserId(),userBillChain.getSourceId());
            if (positionActionForMargin == null){
                log.info("position_action表无此流水---用户:{},订单:{},类型:{}",userId,userBillChain.getSourceId(),type);
                stringBuffer.append("position_action表无此流水---用户:"+userId+",订单:"+userBillChain.getSourceId()+",类型:"+type).append("</br>");
                if (error == null)
                    return CompletableFuture.completedFuture(new String[]{stringBuffer.toString()});
                return CompletableFuture.completedFuture(new String[]{stringBuffer.toString(),error.toString()});
            }
            BigDecimal oneLotSize = accountDao.instruments(positionActionForMargin.getSymbol());
            BigDecimal marginTmp = positionActionForMargin.getOpenPrice().multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()),Config.newScale,BigDecimal.ROUND_DOWN);
            //先根据position_action表计算保证金
            if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                //如果position_action表计算保证金不正确，再根据撮合表中的数据校验
                MatchResultFingerprintChain matchResultFingerprintChain = accountDao.matchResultFingerprint(userBillChain.getSourceId());
                if (matchResultFingerprintChain == null){
                    log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}",userId,userBillChain.getSourceId(),userBillChain.getType(),userBillChain.getSize(),marginTmp);
                    error.append("保证金计算不正确--->,用户:"+userId+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+marginTmp).append("</br>");
                }
                JSONArray items = JSONObject.parseObject(matchResultFingerprintChain.getFMatchResultContent()).getJSONArray("items");
                marginTmp = items.getJSONObject(items.size()-1).getBigDecimal("price")
                        .multiply(positionActionForMargin.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(positionActionForMargin.getLeverage()),Config.newScale,BigDecimal.ROUND_DOWN);
                //如果两个计算都不正确，则记录
                if (marginTmp.compareTo(userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                    log.info("保证金计算不正确--->,用户:{},订单:{},类型:{},数据库:{},计算:{}",userId,userBillChain.getSourceId(),userBillChain.getType(),userBillChain.getSize(),marginTmp);
                    error.append("保证金计算不正确--->,用户:"+userId+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+marginTmp).append("</br>");
                }
            }
        }else if (type == 4){
            userPartner = sqlUtils.getUserPartner(userId);
            List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(),userBillChain.getSourceId(),null);
            //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
            if (positionAction.size() == 0){
                log.info("position_action表无此流水---用户:{},订单:{},类型:{}",userId,userBillChain.getSourceId(),type);
                stringBuffer.append("position_action表无此流水---用户:"+userId+",订单:"+userBillChain.getSourceId()+",类型:"+type).append("</br>");
                if (error == null)
                    return CompletableFuture.completedFuture(new String[]{stringBuffer.toString()});
                return CompletableFuture.completedFuture(new String[]{stringBuffer.toString(),error.toString()});
            }
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
                s =  profitCalc.defaultPartnerProfit(userPartner,userBillChain);
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
        }else if (type == 16){
            log.info("分佣金额余额加减--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getPreBalance(),userBillChain.getSize(),userBillChain.getPostBalance());
            stringBuffer.append("分佣金额余额加减--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",之前余额:"+userBillChain.getPreBalance()+",分佣金额:"+userBillChain.getSize()+",之后余额:"+userBillChain.getPostBalance()).append("</br>");
            if (userBillChain.getPreBalance().add(userBillChain.getSize()).compareTo(userBillChain.getPostBalance()) != 0){
                log.info("分佣金额余额加减不正确--->用户:{},类型:{},之前余额:{},分佣金额:{},之后余额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getPreBalance(),userBillChain.getSize(),userBillChain.getPostBalance());
                error.append("分佣金额余额加减不正确--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",之前余额:"+userBillChain.getPreBalance()+",分佣金额:"+userBillChain.getSize()+",之后余额:"+userBillChain.getPostBalance()).append("</br>");
            }
            //手续费返佣
        }else if (type == 51 || type == 52){
            userPartner = sqlUtils.getUserPartner(userId);
            List<PositionActionChain> positionAction = accountDao.positionAction(userBillChain.getUserId(),userBillChain.getSourceId(),null);
            //如果positionAction表无数据，此条流水不是永续合约，为闪电合约
            if (positionAction.size() == 0){
                log.info("position_action表无此流水---用户:{},订单:{},类型:{}",userId,userBillChain.getSourceId(),type);
                stringBuffer.append("position_action表无此流水---用户:"+userId+",订单:"+userBillChain.getSourceId()+",类型:"+type).append("</br>");
                if (error == null)
                    return CompletableFuture.completedFuture(new String[]{stringBuffer.toString()});
                return CompletableFuture.completedFuture(new String[]{stringBuffer.toString(),error.toString()});
            }
            BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
            BigDecimal fee = BigDecimal.ZERO;
            //先计算手续费
            if (type == 51){
                //开仓存在多笔成交的情况
                if (positionAction.size() == 1){
                    fee = positionAction.get(0).getOpenPrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                    if ( userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                        log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                        error.append("开仓手续费计算不正确--->用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                    }
                }else {
                    //手续费正确标识,true代表手续费计算不正确
                    Boolean feeFlag = true;
                    for (PositionActionChain pa: positionAction) {
                        fee = pa.getOpenPrice().multiply(pa.getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                        if (fee.abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN)) == 0){
                            feeFlag = false;
                            break;
                        }
                    }
                    if (feeFlag){
                        log.info("开仓手续费计算不正确--->用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                        error.append("开仓手续费计算不正确--->用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
                    }
                }
            }else {
                fee = positionAction.get(0).getClosePrice().multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).multiply(Config.taker);
                if ( userBillChain.getSize().abs().setScale(Config.newScale,BigDecimal.ROUND_DOWN).compareTo(fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)) != 0){
                    log.info("平仓手续费计算不正确--->用户:{},订单:{},类型:{}",userBillChain.getUserId(),userBillChain.getSourceId(),userBillChain.getType());
                    error.append("平仓手续费计算不正确--->用户:"+userBillChain.getUserId()+",订单:"+userBillChain.getSourceId()+",类型:"+userBillChain.getType()+",数据库:"+userBillChain.getSize()+",计算:"+fee.setScale(Config.newScale,BigDecimal.ROUND_DOWN)).append("</br>");
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
        }else if (type == 59){
            UserBillChain partnerBillJb = accountDao.getUserBill( Config.high_partner, userBillChain.getSourceId(), 59);
            log.info("爆仓剩余金额，返回给默认合伙人--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getSize(),partnerBillJb.getSize());
            stringBuffer.append("爆仓剩余金额，返回给默认合伙人--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",爆仓剩余金额:"+userBillChain.getSize()+",返还金额:"+partnerBillJb.getSize()).append("</br>");
            if (userBillChain.getSize().abs().compareTo(partnerBillJb.getSize().abs()) != 0){
                log.info("爆仓剩余金额，返回给默认合伙人不正确--->用户:{},类型:{},爆仓剩余金额:{},返还金额:{}",userBillChain.getUserId(),userBillChain.getType(),userBillChain.getSize(),partnerBillJb.getSize());
                error.append("爆仓剩余金额，返回给默认合伙人不正确--->用户:"+userBillChain.getUserId()+",类型:"+userBillChain.getType()+",爆仓剩余金额:"+userBillChain.getSize()+",返还金额:"+partnerBillJb.getSize()).append("</br>");
            }
        }
        return CompletableFuture.completedFuture(new String[]{stringBuffer.toString(),error.toString()});
    }


    @Async("asynExecutor")
    public String test(String message) {
        try {
            Thread.sleep(5000);
            log.info("thread run, message={}", message);
        } catch (InterruptedException e) {
            log.error("thread run error: ", e);
        }
        return message;
    }
}
