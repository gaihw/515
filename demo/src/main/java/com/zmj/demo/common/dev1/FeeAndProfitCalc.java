package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.SwapOrderChain;
import com.zmj.demo.domain.dev1.UserBillChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
public class FeeAndProfitCalc {

    @Autowired
    private AccountDao accountDao;

    /**
     * 只有默认合伙人，计算开平仓手续费返佣
     * @param user_partner
     * @param userBillChain
     * @return
     */
    public String[] partnerFee(List<UserDistributorChain> user_partner , UserBillChain userBillChain){
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partner_id = user_partner.get(1).getUserId();
        //订单
        String source_id = userBillChain.getSourceId();
        //先获取用户的上一级合伙人是不是合伙人，如果是合伙人，rebateUserRatio大于0，则先给用户返手续费，剩下的手续费，按照比列返给合伙人和用户
        //1、先获取列表中索引为1的合伙人
        BigDecimal rebateUserRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio") != null
                ? JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio"):BigDecimal.ZERO;
        //如果该用户直接合伙人的type=0&rebateUserRatio>0，则先给用户返手续费*rebateUserRatio，剩下的手续费*(1-rebateUserRatio)返给合伙人以及每一级合伙人
        if(user_partner.get(1).getType() == 0 && rebateUserRatio.compareTo(BigDecimal.ZERO) == 1){
            //计算先给用户返手续费
            BigDecimal feeToUser = userBillChain.getSize().multiply(rebateUserRatio).setScale(8, BigDecimal.ROUND_DOWN).abs();
            //获取数据库中给用户返的手续费
            UserBillChain feeUserInData_tmp = accountDao.getFeeToUser(userId,source_id,41);
            if (feeUserInData_tmp == null){
                return new String[]{"",stringBuffer.append("--只有默认合伙人--用户有手续费返回--用户手续费返回--未查到给该用户的流水，请查看！数据：用户:"+userId+",订单:"+source_id+"，类型:41").toString()};
            }
            BigDecimal feeUserInData = feeUserInData_tmp.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
            log.info("defaultPartnerFee--只有默认合伙人--用户先返回手续费--用户返手续费校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,type,source_id,feeUserInData,feeToUser);
            stringBuffer.append("--只有默认合伙人--用户先返回手续费--用户返手续费校验--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
            //校验用户的手续费
            if (feeToUser.compareTo(feeUserInData) !=0 ){
                flag = true;
                stringBuffer.append("--只有默认合伙人--用户先返回手续费--用户返手续费不正确--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
            }
            //判断用户是不是合伙人，如果是合伙人，需要给用户按照比列返佣
            //先返给用户的手续费后，剩下的手续费返给合伙人
            BigDecimal feeToPartner = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(rebateUserRatio)).setScale(8, BigDecimal.ROUND_DOWN).abs();
            String[] s = feeToPartnerClac(user_partner,userBillChain,feeToPartner);
            stringBuffer.append(s[0]);
            if (s.length ==2){
                error.append(s[1]);
            }
        }else {//先不给用户返手续费,直接给合伙人人返手续费
            //从数据库获取该笔订单的手续费
            BigDecimal feeToPartner = userBillChain.getSize();
            String[] s = feeToPartnerClac(user_partner,userBillChain,feeToPartner);
            stringBuffer.append(s[0]);
            if (s.length ==2){
                error.append(s[1]);
            }
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }


    /**
     * 计算手续费返佣
     * @param user_partner
     * @param userBillChain
     * @param feeToPartner
     * @return
     */
    private String[] feeToPartnerClac(List<UserDistributorChain> user_partner,UserBillChain userBillChain,BigDecimal feeToPartner) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partner_id = user_partner.get(1).getUserId();
        //订单
        String source_id = userBillChain.getSourceId();
        BigDecimal allotLowerFeeRate0 = BigDecimal.ZERO;
        if (!user_partner.get(0).getConfig().equalsIgnoreCase("")){
            //获取用户config的配置，读取allotLowerFeeRate字段的值
            allotLowerFeeRate0 = JSONObject.parseObject(user_partner.get(0).getConfig()).getBigDecimal("allotLowerFeeRate") == null
                    ? BigDecimal.ZERO:JSONObject.parseObject(user_partner.get(0).getConfig()).getBigDecimal("allotLowerFeeRate");
        }
        //先判断用户是不是合伙人，如果是合伙人，那用户需要给自己分佣
        //如果用户自己是合伙人，并且用户配置allotLowerFeeRate的值大于0，则给用户和合伙人按照比列返佣
        if (user_partner.get(0).getType() == 0 && allotLowerFeeRate0.compareTo(BigDecimal.ZERO) == 1){
            //如果用户只有一个合伙人，则特殊处理
            if (user_partner.size() == 2){
                //先计算给用户返佣
                //计算给合伙人的返佣
                BigDecimal feeToUserself = feeToPartner.multiply(allotLowerFeeRate0).abs().setScale(8, BigDecimal.ROUND_DOWN);
                //从数据库获取该笔订单给用户的返佣
                UserBillChain userself_bill_jb = accountDao.getFeeToUser(userId, source_id, 16);
                if (userself_bill_jb == null){
                    return new String[]{"",stringBuffer.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--未查到给用户自己的流水账单，请查看！数据：用户:"+userId+",订单:"+source_id+"，类型:16").toString()};
                }
                if (feeToUserself.compareTo(userself_bill_jb.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN)) != 0){
                    flag = true;
                    error.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--给用户的手续费返佣不正确，请检查--->用户:" + userId + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userself_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToUserself).append("</br>");
                }
                //计算给合伙人的返佣
                BigDecimal feeToPartner_tmp = feeToPartner.multiply(BigDecimal.ONE.subtract(allotLowerFeeRate0)).abs().setScale(8, BigDecimal.ROUND_DOWN);
                //从数据库获取该笔订单给合伙人的返佣
                UserBillChain partner_bill_jb = accountDao.getUserBill(user_partner.get(1).getUserId(), source_id, 16);
                if (partner_bill_jb == null){
                    return new String[]{"",stringBuffer.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString()};
                }
                log.info("feeToPartnerClac--只有默认合伙人--用户不返回手续费--用户自己有返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, user_partner.get(1).getUserId(), type, source_id,BigDecimal.ONE.subtract(allotLowerFeeRate0),feeToPartner_tmp , partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                stringBuffer.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--给默认合伙人返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id +",比列:"+BigDecimal.ONE.subtract(allotLowerFeeRate0)+ ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner_tmp).append("</br>");
                if (feeToPartner_tmp.compareTo(partner_bill_jb.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                    flag = true;
                    error.append("--只有默认合伙人--用户不返回手续费--用户自己有返佣--给默认合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner_tmp).append("</br>");
                }
            }else {
                //给用户返佣
                for (int i = user_partner.size() - 1; i >= 0; i--) {
                    //计算每个合伙人的手续费
                    //查找user_partner列表中的userId，此userId为合伙人，然后查找合伙人的流水
                    String tmp_partner_id = user_partner.get(i).getUserId();
                    BigDecimal allotLowerFeeRate = BigDecimal.ZERO;
                    if (i == user_partner.size() - 1) {
                        allotLowerFeeRate = BigDecimal.ONE.subtract(JSONObject.parseObject(user_partner.get(user_partner.size() - 2).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    } else if (i == 0) {
                        allotLowerFeeRate = allotLowerFeeRate0;
                    } else {
                        allotLowerFeeRate = JSONObject.parseObject(user_partner.get(i).getConfig()).getBigDecimal("allotLowerFeeRate").subtract(JSONObject.parseObject(user_partner.get(i - 1).getConfig()).getBigDecimal("allotLowerFeeRate"));
                    }
                    //从数据库获取该笔订单给合伙人的返佣
                    UserBillChain partner_bill_jb = accountDao.getUserBill(tmp_partner_id, source_id, 16);
                    if (partner_bill_jb == null){
                        return new String[]{"",stringBuffer.append("--有合伙人--用户自己有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:16").toString()};
                    }
                    //计算给合伙人的返佣
                    BigDecimal feeToPartner_tmp = feeToPartner.multiply(allotLowerFeeRate).abs().setScale(8, BigDecimal.ROUND_DOWN);
                    log.info("feeToPartnerClac--有合伙人--用户自己有返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id,allotLowerFeeRate,feeToPartner , partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                    stringBuffer.append("--有合伙人--用户自己有返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id +",比列:"+allotLowerFeeRate+ ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                    if (feeToPartner.compareTo(partner_bill_jb.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                        flag = true;
                        error.append("--有合伙人--用户自己有返佣--给合伙人手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                    }
                }
            }
        }else {//不给用户返佣
            if (user_partner.size() == 2){
                //计算给合伙人的返佣
                BigDecimal feeToPartner_tmp = feeToPartner.abs();
                //从数据库获取该笔订单给合伙人的返佣
                UserBillChain partner_bill_jb = accountDao.getUserBill(user_partner.get(1).getUserId(), source_id, 16);
                if (partner_bill_jb == null){
                    return new String[]{"",stringBuffer.append("--只有默认合伙人--用户自己无返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString()};
                }
                log.info("feeToPartnerClac--只有默认合伙人--用户自己无返佣--给默认合伙人返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, user_partner.get(1).getUserId(), type, source_id,BigDecimal.ONE.subtract(allotLowerFeeRate0),feeToPartner_tmp , partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                stringBuffer.append("--只有默认合伙人--用户自己无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id +",比列:"+BigDecimal.ONE.subtract(allotLowerFeeRate0)+ ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner_tmp).append("</br>");
                if (feeToPartner_tmp.compareTo(partner_bill_jb.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                    flag = true;
                    error.append("--只有默认合伙人--用户自己无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner_tmp).append("</br>");
                }
            }else {
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
                    //从数据库获取该笔订单给合伙人的返佣
                    UserBillChain partner_bill_jb = accountDao.getUserBill( tmp_partner_id, source_id, 16);
                    if (partner_bill_jb == null) {
                        return new String[]{"",stringBuffer.append("--有合伙人--用户无返佣--未查到给合伙人流水账单，请查看！数据：用户:" + userId + "，合伙人ID:" + tmp_partner_id + ",订单:" + source_id + "，类型:16").toString()};
                    }
                    //计算给合伙人的返佣
                    BigDecimal feeToPartner_tmp = feeToPartner.multiply(allotLowerFeeRate).setScale(8, BigDecimal.ROUND_DOWN);
                    log.info("feeToPartnerClac--有合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id, allotLowerFeeRate, feeToPartner, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                    stringBuffer.append("--有合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",比列:" + allotLowerFeeRate + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                    if (feeToPartner.abs().compareTo(partner_bill_jb.getSize().abs().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0) {
                        flag = true;
                        error.append("--有合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                    }
                }
            }
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 只有默认合伙人，计算盈亏对赌
     * @param user_partner
     * @param userBillChain
     * @return
     */
    public String[] defaultPartnerProfit(List<UserDistributorChain> user_partner , UserBillChain userBillChain){
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partner_id = user_partner.get(1).getUserId();
        //订单
        String source_id = userBillChain.getSourceId();
        //盈亏对赌校验
            //获取订单类型
            SwapOrderChain swapOrderChain = accountDao.getMarginType(userBillChain.getSourceId());
            BigDecimal userProfit = BigDecimal.ZERO;
            //逐仓单,计算盈亏需要去clearing_transfer中取值，因为盈亏和保证金和在一起计算了
            if (swapOrderChain.getMarginType().equalsIgnoreCase("FIXED")){
                userProfit = accountDao.getClearlingTransfer(userBillChain.getUserId(),userBillChain.getSourceId(),swapOrderChain.getMarginType()).getProfit().setScale(8, BigDecimal.ROUND_DOWN);
            }else {//全仓单
                userProfit = userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN);
            }
            //获取该笔订单给合伙人的返佣
            UserBillChain partner_bill_jb = accountDao.getUserBill( partner_id, source_id, 27);
            if (partner_bill_jb == null){
                return new String[]{"",stringBuffer.append("--只有默认合伙人--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partner_id+",订单:"+source_id+"，类型:27").toString()};
            }
            log.info("defaultPartnerFeeAndProfit--只有默认合伙人--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userProfit, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
            stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            if (userProfit.abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                flag = true;
                error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 有普通合伙人和默认合伙人，计算盈亏对赌
     * @param user_partner
     * @param userBillChain
     * @return
     */
    public String[] morePartnerProfit(List<UserDistributorChain> user_partner , UserBillChain userBillChain){
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        Boolean flag = false;
        int type = userBillChain.getType();
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partner_id = user_partner.get(1).getUserId();
        //订单
        String source_id = userBillChain.getSourceId();
        //盈亏对赌校验
            //获取订单类型
            SwapOrderChain swapOrderChain = accountDao.getMarginType(userBillChain.getSourceId());
            BigDecimal userProfit = BigDecimal.ZERO;
            //逐仓单,计算盈亏需要去clearing_transfer中取值，因为盈亏和保证金和在一起计算了
            if (swapOrderChain.getMarginType().equalsIgnoreCase("FIXED")){
                userProfit = accountDao.getClearlingTransfer(userBillChain.getUserId(),userBillChain.getSourceId(),swapOrderChain.getMarginType()).getProfit().setScale(8, BigDecimal.ROUND_DOWN);
            }else {//全仓单
                userProfit = userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN);
            }
            //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与盈亏的对赌，只和默认合伙人有关
            if(user_partner.get(1).getOpenBet() == 0 || user_partner.get(1).getType() !=0  ) {
                //获取该笔订单给合伙人的返佣
                UserBillChain partner_bill_jb = accountDao.getUserBill(partner_id, source_id, 27);
                if (partner_bill_jb == null) {
                    return new String[]{"",stringBuffer.append("--有合伙人(未开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:" + userId + "，合伙人ID:" + partner_id + ",订单:" + source_id + "，类型:27").toString()};
                }
                log.info("morePartnerFeeAndProfit--有合伙人(未开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userProfit, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                if (userProfit.abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0) {
                    flag = true;
                    error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                }
            }else {//如果合伙人列表长度大于2，代表有合伙人，并且合伙人的类型是0，开启了对赌
                //获取合伙人对赌的比列
                BigDecimal partnerTransferOutRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
                String tmp_partner_id = user_partner.get(1).getUserId();
                //计算合伙人对赌盈亏值
                BigDecimal partner_profit = userProfit.multiply(partnerTransferOutRatio).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                //获取该笔订单给合伙人的返佣
                UserBillChain partner_bill_jb = accountDao.getUserBill( tmp_partner_id, source_id, 27);
                if (partner_bill_jb == null){
                    return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:27").toString()};
                }
                log.info("morePartnerFeeAndProfit--有合伙人(开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},比例:{},数据库:{},计算得:{}", userId, tmp_partner_id, type, source_id, partnerTransferOutRatio, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN),partner_profit);
                stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id+"，比例:"+partnerTransferOutRatio+",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_profit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                if (partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_profit.setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                    flag = true;
                    error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_profit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                }
                //计算默认合伙人对赌盈亏值
                BigDecimal default_partner_profit = userProfit.multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                String tmp_default_partner_id = user_partner.get(user_partner.size() - 1).getUserId();
                //获取该笔订单给合伙人的返佣
                UserBillChain default_partner_bill_jb = accountDao.getUserBill(tmp_default_partner_id, source_id, 27);
                if (default_partner_bill_jb == null){
                    return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_default_partner_id+",订单:"+source_id+"，类型:27").toString()};
                }
                stringBuffer.append("盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" +default_partner_profit ).append("</br>");
                log.info("morePartnerFeeAndProfit--有合伙人(开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmp_default_partner_id, type, source_id, default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN),default_partner_profit);
                if (default_partner_profit.abs().compareTo(default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                    flag = true;
                    error.append("盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + default_partner_profit).append("</br>");
                }

            }
        return new String[]{stringBuffer.toString(),error.toString()};
    }
}
