package com.zmj.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.SqlUtils;
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
    private SqlUtils sqlUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
                //合伙人id
                String partner_id = userBillChain.getPartnerId();
                //单号
                String source_id = userBillChain.getSourceId();
                //开仓手续费、平仓手续费,校验返佣
                if (type == 51 || type == 52) {
                    //如果user_partner长度为2，代表只有一个默认合伙人
                    if (user_partner.size() == 2) {
                        //1、先获取列表中索引为1的合伙人
                        BigDecimal rebateUserRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio") != null
                                ? JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio"):BigDecimal.ZERO;
                        //开仓手续费
                        if (type == 51) {
                            //2、如果该合伙人的type=0&rebateUserRatio>0，则先给用户返手续费*rebateUserRatio，剩下的手续费*(1-rebateUserRatio)返给合伙人以及每一级合伙人
                            if(user_partner.get(1).getType() == 0 && rebateUserRatio.compareTo(BigDecimal.ZERO) == 1){
                                //3、计算先给用户返手续费
                                BigDecimal feeToUser = userBillChain.getSize().multiply(rebateUserRatio).setScale(8, BigDecimal.ROUND_DOWN).abs();
                                //4、获取数据库中给用户返的手续费
                                UserBillChain feeUserInData_tmp = accountDao.getFeeToUser(userId,source_id,41);
                                if (feeUserInData_tmp == null){
                                    return stringBuffer.append("--只有默认合伙人--用户有返佣--未查到给该用户的流水，请查看！数据：用户:"+userId+",订单:"+source_id+"，类型:41").toString();
                                }
                                BigDecimal feeUserInData = feeUserInData_tmp.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
                                log.info("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,type,source_id,feeUserInData,feeToUser);
                                stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                                //校验用户的手续费
                                if (feeToUser.compareTo(feeUserInData) !=0 ){
                                    flag = true;
                                    stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费不正确--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                                }
                                //5、计算给合伙人返的手续费
                                BigDecimal feeToPartner = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(rebateUserRatio)).setScale(8, BigDecimal.ROUND_DOWN).abs();
                                //6、获取数据库中给合伙人返的手续费
                                UserBillChain feePartnerInData_tmp = accountDao.getUserBill(userId, user_partner.get(1).getUserId(), source_id, 16, "asc");
                                if (feeUserInData_tmp == null){
                                    return stringBuffer.append("--只有默认合伙人--用户有返佣--未查到给该合伙人的流水，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString();
                                }
                                BigDecimal feePartnerInData = feePartnerInData_tmp.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
                                log.info("--只有默认合伙人--用户有返佣--用户返默认合伙人手续费校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,user_partner.get(1).getUserId(),type,source_id,feePartnerInData,feeToPartner);
                                stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:"+userId+",默认合伙人:"+user_partner.get(1).getUserId()+",交易类型:"+type+",订单:"+source_id+",数据库:"+feePartnerInData+",计算:"+feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(feePartnerInData) != 0){
                                    flag = true;
                                    stringBuffer.append("--只有默认合伙人--用户有返佣--默认合伙人返手续费不正确--->用户:"+userId+",默认合伙人:"+user_partner.get(1).getUserId()+",交易类型:"+type+",订单:"+source_id+",数据库:"+feePartnerInData+",计算:"+feeToPartner).append("</br>");
                                }
                            }else {//不给用户返手续费
                                //获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, user_partner.get(1).getUserId(), source_id, 16, "asc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--只有默认合伙人--用户无返佣--未查到给默认合伙人的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString();
                                }
                                log.info("--只有默认合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, user_partner.get(1).getUserId(), type, source_id, userBillChain.getSize().abs(), partner_bill_jb.getSize());
                                stringBuffer.append("--只有默认合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                                if (userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--只有默认合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                                }
                            }
                            //平仓手续费
                        } else {
                            //2、如果该合伙人的type=0&rebateUserRatio>0，则先给用户返手续费*rebateUserRatio，剩下的手续费*(1-rebateUserRatio)返给合伙人以及每一级合伙人
                            if(user_partner.get(1).getType() == 0 && rebateUserRatio.compareTo(BigDecimal.ZERO) == 1){
                                //3、计算先给用户返手续费
                                BigDecimal feeToUser = userBillChain.getSize().multiply(rebateUserRatio).setScale(8, BigDecimal.ROUND_DOWN).abs();
                                //4、获取数据库中给用户返的手续费
                                UserBillChain feeUserInData_tmp = accountDao.getFeeToUser(userId,source_id,41);
                                if (feeUserInData_tmp == null){
                                    return stringBuffer.append("--只有默认合伙人--用户有返佣--未查到给该用户的流水，请查看！数据：用户:"+userId+",订单:"+source_id+"，类型:41").toString();
                                }
                                BigDecimal feeUserInData = feeUserInData_tmp.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
                                log.info("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,type,source_id,feeUserInData,feeToUser);
                                stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                                //校验用户的手续费
                                if (feeToUser.compareTo(feeUserInData) !=0 ){
                                    flag = true;
                                    stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费不正确--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                                }
                                //5、计算给合伙人返的手续费
                                BigDecimal feeToPartner = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(rebateUserRatio)).setScale(8, BigDecimal.ROUND_DOWN).abs();
                                //6、获取数据库中给合伙人返的手续费
                                UserBillChain feePartnerInData_tmp = accountDao.getUserBill(userId, user_partner.get(1).getUserId(), source_id, 16, "desc");
                                if (feeUserInData_tmp == null){
                                    return stringBuffer.append("--只有默认合伙人--用户有返佣--未查到给该合伙人的流水，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString();
                                }
                                BigDecimal feePartnerInData = feePartnerInData_tmp.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
                                log.info("--只有默认合伙人--用户有返佣--用户返默认合伙人手续费校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,user_partner.get(1).getUserId(),type,source_id,feePartnerInData,feeToPartner);
                                stringBuffer.append("--只有默认合伙人--用户有返佣--用户返手续费校验--->用户:"+userId+",默认合伙人:"+user_partner.get(1).getUserId()+",交易类型:"+type+",订单:"+source_id+",数据库:"+feePartnerInData+",计算:"+feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(feePartnerInData) != 0){
                                    flag = true;
                                    stringBuffer.append("--只有默认合伙人--用户有返佣--默认合伙人返手续费不正确--->用户:"+userId+",默认合伙人:"+user_partner.get(1).getUserId()+",交易类型:"+type+",订单:"+source_id+",数据库:"+feePartnerInData+",计算:"+feeToPartner).append("</br>");
                                }
                            }else {
                                //获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, user_partner.get(1).getUserId(), source_id, 16, "desc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--只有默认合伙人--用户无返佣--未查到给默认合伙人的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+user_partner.get(1).getUserId()+",订单:"+source_id+"，类型:16").toString();
                                }
                                log.info("--只有默认合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算:{}", userId, user_partner.get(1).getUserId(), type, source_id, userBillChain.getSize().abs(), partner_bill_jb.getSize());
                                stringBuffer.append("--只有默认合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                                if (userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--只有默认合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + user_partner.get(1).getUserId() + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userBillChain.getSize().abs() + ",计算:" + partner_bill_jb.getSize()).append("</br>");
                                }
                            }
                        }
                        continue;
                    }
                    //1、user_partner长度大于1，说明有合伙人，先获取列表中索引为1的合伙人
                    BigDecimal rebateUserRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio") != null
                            ? JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("rebateUserRatio"):BigDecimal.ZERO;
                    //2、如果该合伙人的type=0&rebateUserRatio>0，则先给用户返手续费*rebateUserRatio，剩下的手续费*(1-rebateUserRatio)返给合伙人以及每一级合伙人
                    if(user_partner.get(1).getType() == 0 && rebateUserRatio.compareTo(BigDecimal.ZERO) == 1){
                        //3、计算先给用户返手续费
                        BigDecimal feeToUser = userBillChain.getSize().multiply(rebateUserRatio).setScale(8, BigDecimal.ROUND_DOWN).abs();
                        //4、获取数据库中给用户返的手续费
                        UserBillChain feeToUserInData = accountDao.getFeeToUser(userId,source_id,41);
                        if (feeToUserInData == null){
                            return stringBuffer.append("--有合伙人--用户有返佣--返给用户的手续费，流水账单未查到，请查看！数据：用户:"+userId+",订单:"+source_id+"，类型:41").toString();
                        }
                        BigDecimal feeUserInData = feeToUserInData.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs();
                        log.info("--有合伙人--用户有返佣--用户返手续费校验--->用户:{},交易类型:{},订单:{},数据库:{},计算:{}",userId,type,source_id,feeUserInData,feeToUser);
                        stringBuffer.append("--有合伙人--用户有返佣--用户返手续费校验--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                        //校验用户的手续费
                        if (feeToUser.compareTo(feeUserInData) !=0 ){
                            flag = true;
                            stringBuffer.append("--有合伙人--用户有返佣--用户返手续费不正确--->用户:"+userId+",交易类型:"+type+",订单:"+source_id+",数据库:"+feeUserInData+",计算:"+feeToUser).append("</br>");
                        }
                        //手续费返还给用户后，计算剩余给所有合伙人的总手续费
                        BigDecimal feeToPartnerTotal = userBillChain.getSize().multiply(BigDecimal.ONE.subtract(rebateUserRatio)).setScale(8, BigDecimal.ROUND_DOWN).abs();
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
                                //从数据库获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "asc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--有合伙人--用户有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:16").toString();
                                }
                                //计算给合伙人的返佣
                                BigDecimal feeToPartner = feeToPartnerTotal.multiply(allotLowerFeeRate).setScale(8, BigDecimal.ROUND_DOWN);
                                log.info("--有合伙人--用户有返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id,allotLowerFeeRate,feeToPartner , partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                                stringBuffer.append("--有合伙人--用户有返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id +",比列:"+allotLowerFeeRate+ ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--有合伙人--用户有返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                }
                            } else {
                                //获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "desc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--有合伙人--用户有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:16").toString();
                                }
                                //计算给合伙人的返佣
                                BigDecimal feeToPartner = feeToPartnerTotal.multiply(allotLowerFeeRate).setScale(8, BigDecimal.ROUND_DOWN);
                                log.info("--有合伙人--用户有返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{}, 比例:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id, allotLowerFeeRate,partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartner);
                                stringBuffer.append("--有合伙人--用户有返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" +"，比列:"+allotLowerFeeRate+ source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--有合伙人--用户有返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                }
                            }
                        }
                    }else{//不给用户返手续费
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
                                //从数据库获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "asc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--有合伙人--用户有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:16").toString();
                                }
                                //计算给合伙人的返佣
                                BigDecimal feeToPartner = userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(8, BigDecimal.ROUND_DOWN);
                                log.info("--有合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{},比列:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id,allotLowerFeeRate,feeToPartner , partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                                stringBuffer.append("--有合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id +",比列:"+allotLowerFeeRate+ ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--有合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                }
                            } else {
                                //获取该笔订单给合伙人的返佣
                                UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 16, "desc");
                                if (partner_bill_jb == null){
                                    return stringBuffer.append("--有合伙人--用户有返佣--未查到给合伙人流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:16").toString();
                                }
                                //计算给合伙人的返佣
                                BigDecimal feeToPartner = userBillChain.getSize().abs().multiply(allotLowerFeeRate).setScale(8, BigDecimal.ROUND_DOWN);
                                log.info("--有合伙人--用户无返佣--手续费校验-->用户:{},合伙人:{},交易类型:{},订单:{}, 比例:{},数据库:{},计算:{}", userId, tmp_partner_id, type, source_id, allotLowerFeeRate,partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN), feeToPartner);
                                stringBuffer.append("--有合伙人--用户无返佣--手续费返佣校验-->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" +"，比列:"+allotLowerFeeRate+ source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                if (feeToPartner.compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                                    flag = true;
                                    error.append("--有合伙人--用户无返佣--手续费返佣不正确，请检查--->用户:" + userId + ",合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算:" + feeToPartner).append("</br>");
                                }
                            }
                        }
                    }
                }
                //盈亏对赌校验
                if (type == 4) {
                    //获取订单类型
                    SwapOrderChain swapOrderChain = accountDao.getMarginType(userBillChain.getSourceId());
                    BigDecimal userProfit = BigDecimal.ZERO;
                    //逐仓单,计算盈亏需要去clearing_transfer中取值，因为盈亏和保证金和在一起计算了
                    if (swapOrderChain.getMarginType().equalsIgnoreCase("FIXED")){
                        userProfit = accountDao.getClearlingTransfer(userBillChain.getUserId(),userBillChain.getSourceId(),swapOrderChain.getMarginType()).getProfit().setScale(8, BigDecimal.ROUND_DOWN);
                    }else {//全仓单
                        userProfit = userBillChain.getSize().setScale(8, BigDecimal.ROUND_DOWN);
                    }
                    //如果合伙人列表长度为2，代表只有默认合伙人
                    if (user_partner.size() == 2) {
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 27, "desc");
                        if (partner_bill_jb == null){
                            return stringBuffer.append("--只有默认合伙人--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partner_id+",订单:"+source_id+"，类型:27").toString();
                        }
                        log.info("盈亏校验1--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userProfit, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        stringBuffer.append("盈亏对赌校验1--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (userProfit.abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确1，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                        //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与盈亏的对赌，只和默认合伙人有关
                    } else if((user_partner.size() > 2 && user_partner.get(1).getOpenBet() == 0) || (user_partner.size() > 2 && user_partner.get(1).getType() !=0  )){
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, partner_id, source_id, 27, "desc");
                        if (partner_bill_jb == null){
                            return stringBuffer.append("--有合伙人(未开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partner_id+",订单:"+source_id+"，类型:27").toString();
                        }
                        log.info("盈亏校验2--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partner_id, type, source_id, userProfit, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN));
                        stringBuffer.append("盈亏对赌校验2--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (userProfit.abs().compareTo(partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确2，请检查--->用户:" + userId + ",默认合伙人:" + partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + userProfit + ",计算得:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                    }else {//如果合伙人列表长度大于2，代表有合伙人，并且合伙人的类型是0，开启了对赌
                        //获取合伙人对赌的比列
                        BigDecimal partnerTransferOutRatio = JSONObject.parseObject(user_partner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
                        String tmp_partner_id = user_partner.get(1).getUserId();
                        //计算合伙人对赌盈亏值
                        BigDecimal partner_profit = userProfit.multiply(partnerTransferOutRatio).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                        //获取该笔订单给合伙人的返佣
                        UserBillChain partner_bill_jb = accountDao.getUserBill(userId, tmp_partner_id, source_id, 27, "desc");
                        if (partner_bill_jb == null){
                            return stringBuffer.append("--有合伙人(开启对赌)--未查到合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_partner_id+",订单:"+source_id+"，类型:27").toString();
                        }
                        log.info("盈亏校验3--->用户:{},默认合伙人:{},交易类型:{},订单:{},比例:{},数据库:{},计算得:{}", userId, tmp_partner_id, type, source_id, partnerTransferOutRatio, partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN),partner_profit);
                        stringBuffer.append("盈亏对赌校验3--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id+"，比例:"+partnerTransferOutRatio+",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_profit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        if (partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partner_profit.setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确3，请检查--->用户:" + userId + ",默认合伙人:" + tmp_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partner_profit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
                        }
                        //计算默认合伙人对赌盈亏值
                        BigDecimal default_partner_profit = userProfit.multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).multiply(BigDecimal.valueOf(-1)).setScale(8, BigDecimal.ROUND_DOWN);
                        String tmp_default_partner_id = user_partner.get(user_partner.size() - 1).getUserId();
                        //获取该笔订单给合伙人的返佣
                        UserBillChain default_partner_bill_jb = accountDao.getUserBill(userId, tmp_default_partner_id, source_id, 27, "desc");
                        if (default_partner_bill_jb == null){
                            return stringBuffer.append("--有合伙人(开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmp_default_partner_id+",订单:"+source_id+"，类型:27").toString();
                        }
                        stringBuffer.append("盈亏对赌校验4--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" +default_partner_profit ).append("</br>");
                        log.info("盈亏校验4--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmp_default_partner_id, type, source_id, default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN),default_partner_profit);
                        if (default_partner_profit.abs().compareTo(default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                            flag = true;
                            error.append("盈亏对赌不正确4，请检查--->用户:" + userId + ",默认合伙人:" + tmp_default_partner_id + ",交易类型:" + type + ",订单:" + source_id + ",数据库:" + default_partner_bill_jb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + default_partner_profit).append("</br>");
                        }
                    }
                } else {
                    continue;
                }
            }
            log.info("--------" + userId + "流水--------");
            stringBuffer.append("--------" + userId + "流水--------").append("</br>");
        } catch (Exception e) {
            log.error(e.toString());
            return "账单未更新，请稍后重试!";
        }
        if (flag){
            return stringBuffer.append("<font color=\"#FF0000\">对账异常账单:</font> ").append("</br>").append(error).toString();
        }
        return stringBuffer.toString();
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
