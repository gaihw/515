package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.test.AccountDao;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class FeeCalc {

    @Autowired
    private AccountDao accountDao;

    /**
     * 只有默认合伙人，计算开平仓手续费返佣
     * @param userPartner
     * @param userBillChain
     * @return
     */
    public String[] partnerFee(List<UserDistributorChain> userPartner , UserBillChain userBillChain){
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partnerId = userPartner.get(1).getUserId();
        //订单
        String sourceId = userBillChain.getSourceId();
        //先获取用户的上一级合伙人是不是合伙人，如果是合伙人，rebateUserRatio大于0，则先给用户返手续费，剩下的手续费，按照比列返给合伙人和用户
        //1、先获取列表中索引为1的合伙人
        BigDecimal rebateUserRatio = BigDecimal.ZERO;
        if (JSONObject.parseObject(userPartner.get(1).getConfig()) != null){
            rebateUserRatio = JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("rebateUserRatio") != null
                    ? JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("rebateUserRatio"):BigDecimal.ZERO;
        }
        //如果该用户直接合伙人的type=0&rebateUserRatio>0，则先给用户返手续费*rebateUserRatio，剩下的手续费*(1-rebateUserRatio)返给合伙人以及每一级合伙人
        if(userPartner.get(1).getType() == 0 && rebateUserRatio.compareTo(BigDecimal.ZERO) == 1){
            //计算先给用户返手续费
            BigDecimal feeToUser = userBillChain.getSize().multiply(rebateUserRatio).setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();
            //获取数据库中给用户返的手续费
            UserBillChain feeUserInDataTmp = accountDao.getUserBill(userId,sourceId,41);
            if (feeUserInDataTmp == null){
                return new String[]{"",stringBuffer.append("--有合伙人--用户有手续费返回--用户手续费返回--未查到给该用户的流水，请查看！数据：用户:"+userId+",订单:"+sourceId+"，类型:41").append("</br>").toString()};
            }
            //一笔订单，多笔成交
            BigDecimal feeUserInData = feeUserInDataTmp.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();
            log.info("partnerFee--有合伙人--用户先返回手续费--用户返手续费校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, type, sourceId, feeUserInData, feeToUser);
            stringBuffer.append("--有合伙人--用户先返回手续费--用户返手续费校验--->用户:" + userId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + feeUserInData + ",计算:" + feeToUser).append("</br>");
            //校验用户的手续费
            if (feeToUser.compareTo(feeUserInData) != 0) {
                flag = true;
                error.append("--有合伙人--用户先返回手续费--用户返手续费不正确--->用户:" + userId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + feeUserInData + ",计算:" + feeToUser).append("</br>");
            }
            //判断用户是不是合伙人，如果是合伙人，需要给用户按照比列返佣
            //先返给用户的手续费后，剩下的手续费返给合伙人
//            BigDecimal feeToPartner = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(rebateUserRatio));
            BigDecimal feeToPartner = userBillChain.getSize().add(feeToUser);
            String[] s = feeToPartnerClac(userPartner,userBillChain,feeToPartner);
            stringBuffer.append(s[0]);
            if (s.length ==2){
                error.append(s[1]);
            }
        }else {//先不给用户返手续费,直接给合伙人人返手续费
            //从数据库获取该笔订单的手续费
            BigDecimal feeToPartner = userBillChain.getSize();
            String[] s = feeToPartnerClac(userPartner,userBillChain,feeToPartner);
            stringBuffer.append(s[0]);
            if (s.length ==2){
                error.append(s[1]);
            }
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }


    /**
     * 计算手续费返佣
     * @param userPartner
     * @param userBillChain
     * @param feeToPartner
     * @return
     */
    private String[] feeToPartnerClac(List<UserDistributorChain> userPartner,UserBillChain userBillChain,BigDecimal feeToPartner) {
        //先看合伙人列表，查询非用户自己和非默认合伙人，user_partner_balance表是否有合伙人数据，如果无数据，表示为普通用户，移除列表，不参与返佣
        for (int i = 1; i < userPartner.size()-1; i++) {
            if (accountDao.getUserPartnerBalanceByUser(userPartner.get(i).getUserId())== null){
                userPartner.remove(i);
                i--;
            }
        }
        //返佣比例
        BigDecimal allotLowerFeeRate0 = BigDecimal.ZERO;
        //临时存储合伙人的变量
        List<UserDistributorChain> userPartnerTmp = new ArrayList<UserDistributorChain>();
        //如果合伙人配置的返佣比例为0，则不给合伙人返佣以及合伙人的下级返佣，如：A的合伙人->B的合伙人->C(0)的合伙人->D的合伙人->E，此时，A、B和C是不返佣的从列表移除
        int partnerIndex = 0;
        for (int i = 1; i < userPartner.size()-1; i++) {
            allotLowerFeeRate0 = JSONObject.parseObject(userPartner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate") == null
                    ? BigDecimal.ZERO:JSONObject.parseObject(userPartner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate");
            //记录合伙人返佣比例为0的最大索引
            if (allotLowerFeeRate0.compareTo(BigDecimal.ZERO) == 0){
                partnerIndex = i;
            }
        }
        //如果partnerIndex大于0，说明有合伙人的返佣比例配置的0，那么需要过滤掉
        if (partnerIndex > 0 ){
            userPartnerTmp.add(userPartner.get(0));
            userPartner = userPartner.subList(partnerIndex+1,userPartner.size());
            for (UserDistributorChain u:userPartner) {
                userPartnerTmp.add(u);
            }
//            userPartner = userPartnerTmp;
        }

        //重置
        userPartner.clear();
        //合伙人的列表长度-2大于配置的层级比例，根据层级返佣
        if (userPartnerTmp.size()-2 > Config.level){
            userPartner.add(userPartnerTmp.get(0));
            for (int i = 1; i <= Config.level; i++) {
                userPartner.add(userPartnerTmp.get(i));
            }
            userPartner.add(userPartnerTmp.get(userPartnerTmp.size()-1));
        }else {
            userPartner = userPartnerTmp;
        }

        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partnerId = userPartner.get(1).getUserId();
        //订单
        String sourceId = userBillChain.getSourceId();

        if (!userPartner.get(0).getConfig().equalsIgnoreCase("")){
            //获取用户config的配置，读取allotLowerFeeRate字段的值
            allotLowerFeeRate0 = JSONObject.parseObject(userPartner.get(0).getConfig()).getBigDecimal("allotLowerFeeRate") == null
                    ? BigDecimal.ZERO:JSONObject.parseObject(userPartner.get(0).getConfig()).getBigDecimal("allotLowerFeeRate");
        }
        //先判断用户是不是合伙人，如果是合伙人，那用户需要给自己分佣,注意：给用户自己返佣时，不根据type=0字段，根据user_partner_balance表判断，如果该表中有数据，则返佣；反之，不返佣
        //如果用户自己是合伙人，并且用户配置allotLowerFeeRate的值大于0，则给用户和合伙人按照比列返佣
        Boolean isBackFeeToUser = accountDao.getUserPartnerBalanceByUser(userId)== null ? false:true;

        if (isBackFeeToUser && allotLowerFeeRate0.compareTo(BigDecimal.ZERO) == 1){
            //如果用户只有一个合伙人，则特殊处理
            if (userPartner.size() == 2){
                //先计算给用户返佣
                BigDecimal feeToUserself = feeToPartner.multiply(allotLowerFeeRate0).abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                //从数据库获取该笔订单给用户的返佣
                List<UserBillChain> userselfBillJb = accountDao.getUserBillFeeBack(userId, sourceId, 16);
                if (userselfBillJb.size() == 0){
                    return new String[]{"",stringBuffer.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--未查到给用户自己的流水账单，请查看！数据：用户:"+userId+",订单:"+sourceId+"，类型:16").append("</br>").toString()};
                }
                if (userselfBillJb.size() == 1) {
                    log.info("--有合伙人--用户自己有返佣--给用户的手续费返佣校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, type, sourceId, userselfBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToUserself);
                    stringBuffer.append("--有合伙人--用户自己有返佣--给用户的手续费返佣校验--->用户:" + userId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + userselfBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToUserself).append("</br>");
                    if (feeToUserself.compareTo(userselfBillJb.get(0).getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) != 0) {
                        flag = true;
                        error.append("--有合伙人--用户自己有返佣--给用户的手续费返佣不正确，请检查--->用户:" + userId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + userselfBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToUserself).append("</br>");
                    }
                }else {
                    //手续费正确标识,true代表手续费计算不正确
                    Boolean feeFlag = true;
                    for (UserBillChain ubc: userselfBillJb) {
                        log.info("--有合伙人--用户自己有返佣--给用户的手续费返佣校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, type, sourceId, ubc.getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToUserself);
                        if (feeToUserself.compareTo(ubc.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0){
                            feeFlag = false;
                            break;
                        }
                    }
                    if (feeFlag){
                        error.append("--有合伙人--用户自己有返佣--给用户的手续费返佣不正确，请检查--->用户:" + userId + ",交易类型:" + type + ",订单:" + sourceId+  ",计算:" + feeToUserself).append("</br>");
                    }
                }
                //计算给合伙人的返佣
                BigDecimal feeToPartnerTmp = feeToPartner.multiply(BigDecimal.ONE.subtract(allotLowerFeeRate0)).abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                //从数据库获取该笔订单给合伙人的返佣
                List<UserBillChain> partnerBillJb = accountDao.getUserBillFeeBack(userPartner.get(1).getUserId(), sourceId, 16);
                if (partnerBillJb.size() == 0){
                    return new String[]{"",stringBuffer.append("--只有默认合伙人--用户自己有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+userPartner.get(1).getUserId()+",订单:"+sourceId+"，类型:16").append("</br>").toString()};
                }
                if (partnerBillJb.size() == 1) {
                    log.info("feeToPartnerClac--有合伙人--用户自己有返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算得:{}", userId, userPartner.get(1).getUserId(), type, sourceId, BigDecimal.ONE.subtract(allotLowerFeeRate0), partnerBillJb.get(0).getSize(),feeToPartnerTmp);
                    stringBuffer.append("--有合伙人--用户自己有返佣--给默认合伙人返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",比列:" + BigDecimal.ONE.subtract(allotLowerFeeRate0) + ",计算:" + feeToPartnerTmp).append("</br>");
                    if (feeToPartnerTmp.compareTo(partnerBillJb.get(0).getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                        flag = true;
                        error.append("--有合伙人--用户自己有返佣--给默认合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                    }
                }else {
                    //手续费正确标识,true代表手续费计算不正确
                    Boolean feeFlag = true;
                    for (UserBillChain ubc: partnerBillJb) {
                        log.info("feeToPartnerClac--有合伙人--用户自己有返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算得:{}", userId, userPartner.get(1).getUserId(), type, sourceId, BigDecimal.ONE.subtract(allotLowerFeeRate0), ubc.getSize(),feeToPartnerTmp);
                        if (feeToPartnerTmp.compareTo(ubc.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0){
                            feeFlag = false;
                            break;
                        }
                    }
                    if (feeFlag){
                        error.append("--有合伙人--用户自己有返佣--给默认合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",计算:" + feeToPartnerTmp).append("</br>");
                    }
                }
            }else {
                //给用户返佣
                for (int i = userPartner.size() - 1; i >= 0; i--) {
                    //计算每个合伙人的手续费
                    //查找user_partner列表中的userId，此userId为合伙人，然后查找合伙人的流水
                    String tmpPartnerId = userPartner.get(i).getUserId();
                    BigDecimal allotLowerFeeRate = BigDecimal.ZERO;
                    if (i == userPartner.size() - 1) {
                        allotLowerFeeRate = BigDecimal.ONE.subtract(JSONObject.parseObject(userPartner.get(userPartner.size() - 2).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    } else if (i == 0) {
                        allotLowerFeeRate = allotLowerFeeRate0;
                    } else {
                        allotLowerFeeRate = JSONObject.parseObject(userPartner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate").subtract(JSONObject.parseObject(userPartner.get(i - 1).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    }
                    //从数据库获取该笔订单给合伙人的返佣
                    List<UserBillChain> partnerBillJb = accountDao.getUserBillFeeBack(tmpPartnerId, sourceId, 16);
                    if (partnerBillJb.size() == 0){
                        return new String[]{"",stringBuffer.append("--有合伙人--用户自己有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmpPartnerId+",订单:"+sourceId+"，类型:16").append("</br>").toString()};
                    }
                    //计算给合伙人的返佣
                    BigDecimal feeToPartnerTmp = feeToPartner.multiply(allotLowerFeeRate).abs().setScale(8, BigDecimal.ROUND_DOWN);
                    if (partnerBillJb.size() == 1) {
                        log.info("feeToPartnerClac--有合伙人--用户自己有返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmpPartnerId, type, sourceId, allotLowerFeeRate, partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartnerTmp);
                        stringBuffer.append("--有合伙人--用户自己有返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",比列:" + allotLowerFeeRate + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                        if (feeToPartnerTmp.compareTo(partnerBillJb.get(0).getSize().abs().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0) {
                            flag = true;
                            error.append("--有合伙人--用户自己有返佣--给合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                        }
                    }else {
                        //手续费正确标识,true代表手续费计算不正确
                        Boolean feeFlag = true;
                        for (UserBillChain ubc: partnerBillJb) {
                            log.info("feeToPartnerClac--有合伙人--用户自己有返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmpPartnerId, type, sourceId, allotLowerFeeRate, ubc.getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartnerTmp);
                            if (feeToPartnerTmp.compareTo(ubc.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN)) == 0){
                                feeFlag = false;
                                break;
                            }
                        }
                        if (feeFlag){
                            error.append("--有合伙人--用户自己有返佣--给合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId  + ",计算:" + feeToPartnerTmp).append("</br>");
                        }
                    }
                }
            }
        }else {//不给用户返佣
            if (userPartner.size() == 2){
                //计算给合伙人的返佣
                BigDecimal feeToPartnerTmp = feeToPartner.setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();
                //从数据库获取该笔订单给合伙人的返佣
                List<UserBillChain> partnerBillJb = accountDao.getUserBillFeeBack(userPartner.get(1).getUserId(), sourceId, 16);
                if (partnerBillJb.size() == 0){
                    return new String[]{"",stringBuffer.append("--有合伙人--用户自己无返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+userPartner.get(1).getUserId()+",订单:"+sourceId+"，类型:16").append("</br>").toString()};
                }
                if (partnerBillJb.size() == 1) {
                    log.info("feeToPartnerClac--有合伙人--用户自己无返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, userPartner.get(1).getUserId(), type, sourceId, BigDecimal.ONE.subtract(allotLowerFeeRate0), feeToPartnerTmp, partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN));
                    stringBuffer.append("--有合伙人--用户自己无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",比列:" + BigDecimal.ONE.subtract(allotLowerFeeRate0) + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                    if (feeToPartnerTmp.compareTo(partnerBillJb.get(0).getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                        flag = true;
                        error.append("--有合伙人--用户自己无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                    }
                }else {
                    //手续费正确标识,true代表手续费计算不正确
                    Boolean feeFlag = true;
                    for (UserBillChain ubc: partnerBillJb) {
                        log.info("feeToPartnerClac--有合伙人--用户自己无返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, userPartner.get(1).getUserId(), type, sourceId, BigDecimal.ONE.subtract(allotLowerFeeRate0), ubc.getSize().setScale(8, BigDecimal.ROUND_DOWN),feeToPartnerTmp);
                        if (feeToPartnerTmp.compareTo(ubc.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0){
                            feeFlag = false;
                            break;
                        }
                    }
                    if (feeFlag){
                        error.append("--有合伙人--用户自己无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + userPartner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + sourceId + ",计算:" + feeToPartnerTmp).append("</br>");
                    }
                }
            }else {
                for (int i = userPartner.size() - 1; i >= 1; i--) {
                    //计算每个合伙人的手续费
                    //查找user_partner列表中的userId，此userId为合伙人，然后查找合伙人的流水
                    String tmpPartnerId = userPartner.get(i).getUserId();
                    BigDecimal allotLowerFeeRate = BigDecimal.ZERO;
                    if (i == userPartner.size() - 1) {
                        allotLowerFeeRate = BigDecimal.ONE.subtract(JSONObject.parseObject(userPartner.get(userPartner.size() - 2).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    } else if (i == 1) {
                        allotLowerFeeRate = JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("allotLowerFeeRate");
                    } else {
                        allotLowerFeeRate = JSONObject.parseObject(userPartner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate").subtract(JSONObject.parseObject(userPartner.get(i - 1).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    }
                    //从数据库获取该笔订单给合伙人的返佣
                    List<UserBillChain> partnerBillJb = accountDao.getUserBillFeeBack( tmpPartnerId, sourceId, 16);
                    if (partnerBillJb.size() == 0) {
                        return new String[]{"",stringBuffer.append("--有合伙人--用户无返佣--未查到给合伙人流水账单，请查看！数据：用户:" + userId + "，合伙人ID:" + tmpPartnerId + ",订单:" + sourceId + "，类型:16").append("</br>").toString()};
                    }
                    //计算给合伙人的返佣
                    BigDecimal feeToPartnerTmp = feeToPartner.multiply(allotLowerFeeRate).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                    if (partnerBillJb.size() == 1) {
                        log.info("feeToPartnerClac--有合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmpPartnerId, type, sourceId, allotLowerFeeRate, partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartnerTmp);
                        stringBuffer.append("--有合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",比列:" + allotLowerFeeRate + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                        if (feeToPartnerTmp.abs().compareTo(partnerBillJb.get(0).getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                            flag = true;
                            error.append("--有合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.get(0).getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartnerTmp).append("</br>");
                        }
                    }else {
                        //手续费正确标识,true代表手续费计算不正确
                        Boolean feeFlag = true;
                        for (UserBillChain ubc: partnerBillJb) {
                            log.info("feeToPartnerClac--有合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmpPartnerId, type, sourceId, allotLowerFeeRate, ubc.getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartnerTmp);
                            if (feeToPartnerTmp.compareTo(ubc.getSize().abs().setScale(Config.newScale, BigDecimal.ROUND_DOWN)) == 0){
                                feeFlag = false;
                                break;
                            }
                        }
                        if (feeFlag){
                            error.append("--有合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId  + ",计算:" + feeToPartnerTmp).append("</br>");
                        }
                    }
                }
            }
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

}
