package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.test.AccountDao;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Configuration
public class AccountUtil {

    @Autowired
    private AccountDao accountDao;


    /**
     * 判断用户是否有手续费返回
     * @param userId
     * @return
     */
    public Boolean userFeeIsBack(String userId) {
        //获取用户配置
        UserDistributorChain user = accountDao.getUserDistributor(userId);
        if(user.getPartnerId().equalsIgnoreCase("0")){
            return false;
        }
        UserDistributorChain parter = accountDao.getUserDistributor(user.getPartnerId());
        if (parter.getConfig() == null){
            return false;
        }
        //用户的合伙人type=0，代表为合伙人，并且rebateUserRatio>0，则用户有手续费返回
        if (parter.getType() == 0 && JSONObject.parseObject(parter.getConfig()).getBigDecimal("rebateUserRatio").compareTo(BigDecimal.ZERO) == 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取用户返佣比列
     * @param userId
     * @return
     */
    public BigDecimal rebateUserRatio(String userId) {
        //获取用户配置
        UserDistributorChain user = accountDao.getUserDistributor(userId);
        if(user.getPartnerId().equalsIgnoreCase("0")){
            return BigDecimal.ZERO;
        }
        UserDistributorChain parter = accountDao.getUserDistributor(user.getPartnerId());
        if (parter.getConfig() == null){
            return BigDecimal.ZERO;
        }
        //用户的合伙人type=0，代表为合伙人，并且rebateUserRatio>0，则用户有手续费返回
        if (parter.getType() == 0 && JSONObject.parseObject(parter.getConfig()).getBigDecimal("rebateUserRatio").compareTo(BigDecimal.ZERO) == 1){
            return JSONObject.parseObject(parter.getConfig()).getBigDecimal("rebateUserRatio");
        }else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算用户的穿仓回退
     * @param userPartner
     * @param money
     * @return
     */
    public String throughBack(List<UserDistributorChain> userPartner,BigDecimal money) {
        if (userPartner.size() == 2){
            return userPartner.get(1).getUserId()+"穿仓回退金额为:"+money;
        }else {
            BigDecimal partnerTransferOutRatio = BigDecimal.ZERO;
            if (!userPartner.get(1).getConfig().equalsIgnoreCase("")){
                partnerTransferOutRatio = JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio") == null
                        ? BigDecimal.ZERO:JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
            }
            //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与穿仓的回补，只和默认合伙人有关
            //普通合伙人是合伙人，并且开启了对赌，但是配置的比例为0，那该普通合伙人不进行穿仓回补
            if(userPartner.get(1).getOpenBet() == 0 || partnerTransferOutRatio.compareTo(BigDecimal.ZERO) == 0) {
                return userPartner.get(userPartner.size()-1).getUserId()+"穿仓回退金额为:"+money;
            }else {//如果合伙人列表长度大于2，代表有合伙人,开启了对赌
                if (userPartner.get(1).getType() !=0 ){
                    partnerTransferOutRatio = Config.partnerTransferOutRatio;
                }
                //获取合伙人对赌的比列
                //计算合伙人对赌盈亏值
                BigDecimal partnerBack = money.multiply(partnerTransferOutRatio).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                //计算默认合伙人穿仓回补值
                BigDecimal defaultPartnerBack = money.multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
                return userPartner.get(1).getUserId()+"穿仓回退金额为:"+partnerBack+"；"+userPartner.get(userPartner.size()-1).getUserId()+"穿仓回退金额为:"+defaultPartnerBack;
            }
        }
    }
}
