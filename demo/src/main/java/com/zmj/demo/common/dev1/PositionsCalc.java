package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.PositionChain;
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
public class PositionsCalc {

    @Autowired
    private AccountDao accountDao;

    /**
     * 单校验type=54,对应合伙人的回补金额是否正确，不计算比例
     * @param userBillChain
     * @return
     */
    public String[] partnerBack( UserBillChain userBillChain){
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer error = new StringBuffer();
        int type = userBillChain.getType();
        Boolean flag = false;
        //用户
        String userId = userBillChain.getUserId();
        //合伙人
        String partnerId = userBillChain.getFromUserId();
        //订单
        String sourceId = userBillChain.getSourceId();
        //获取该笔订单合伙人的穿仓回退金额
        UserBillChain partnerBillJb = accountDao.getUserBill( partnerId, sourceId, 38);
        if (partnerBillJb == null){
            return new String[]{"",stringBuffer.append("--合伙人--未查到合伙人穿仓回退的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partnerId+",订单:"+sourceId+"，类型:38").toString()};
        }
        //获取该笔订单用户的穿仓金额
        BigDecimal userThroughBalance = userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();
        log.info("partnerBack--合伙人--穿仓回退--->用户:{},合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partnerId, type, sourceId, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN), userThroughBalance);
        stringBuffer.append("穿仓回补校验--->用户:" + userId + ",合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance ).append("</br>");
        if (userThroughBalance.abs().compareTo(partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0){
            flag = true;
            error.append("穿仓回补校验不正确，请检查--->用户:" + userId + ",合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance).append("</br>");
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 用户只有默认合伙人，校验穿仓回补
     * @param userPartner
     * @param userBillChain
     * @return
     */
    public String[] defaultPartnerBack(List<UserDistributorChain> userPartner , UserBillChain userBillChain){
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
        //获取该笔订单合伙人的穿仓回退金额
        UserBillChain partnerBillJb = accountDao.getUserBill( partnerId, sourceId, 38);
        if (partnerBillJb == null){
            return new String[]{"",stringBuffer.append("--只有默认合伙人--未查到默认合伙人穿仓回退的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partnerId+",订单:"+sourceId+"，类型:38").toString()};
        }
        //获取该笔订单用户的穿仓金额
        BigDecimal userThroughBalance = userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();
        log.info("defaultPartnerBack--只有默认合伙人--穿仓回退--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partnerId, type, sourceId, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN), userThroughBalance);
        stringBuffer.append("穿仓回补校验--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance ).append("</br>");
        if (userThroughBalance.abs().compareTo(partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0){
            flag = true;
            error.append("穿仓回补校验不正确，请检查--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance).append("</br>");
        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 计算合伙人和默认合伙人的穿仓回补数据
     * @param userPartner
     * @param userBillChain
     * @return
     */
    public String[] morePartnerBack(List<UserDistributorChain> userPartner , UserBillChain userBillChain){
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
        //获取该笔订单用户的穿仓金额
        BigDecimal userThroughBalance = userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs();

        BigDecimal partnerTransferOutRatio = BigDecimal.ZERO;
        if (!userPartner.get(1).getConfig().equalsIgnoreCase("")){
            partnerTransferOutRatio = JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio") == null
                    ? BigDecimal.ZERO:JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
        }
        //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与穿仓的回补，只和默认合伙人有关
        //普通合伙人是合伙人，并且开启了对赌，但是配置的比例为0，那该普通合伙人不进行穿仓回补
        if(userPartner.get(1).getOpenBet() == 0 || userPartner.get(1).getType() !=0  || partnerTransferOutRatio.compareTo(BigDecimal.ZERO) == 0) {
            partnerId =userPartner.get(userPartner.size()-1).getUserId();
            //获取该笔订单合伙人的穿仓回补
            UserBillChain partnerBillJb = accountDao.getUserBill(partnerId, sourceId, 38);

            if (partnerBillJb == null) {
                return new String[]{"",stringBuffer.append("--有合伙人(未开启对赌)--未查到默认合伙人穿仓回补的流水账单，请查看！数据：用户:" + userId + "，合伙人ID:" + partnerId + ",订单:" + sourceId + "，类型:38").toString()};
            }
            log.info("morePartnerBack--有合伙人(未开启对赌)--默认合伙人穿仓回补校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partnerId, type, sourceId, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN), userThroughBalance);
            stringBuffer.append("--有合伙人(未开启对赌)--默认合伙人穿仓回补校验--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance).append("</br>");
            if (userThroughBalance.compareTo(partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                flag = true;
                error.append("--有合伙人(未开启对赌)--默认合伙人穿仓回补不正确，请检查--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userThroughBalance).append("</br>");
            }
        }else {//如果合伙人列表长度大于2，代表有合伙人，并且合伙人的类型是0，开启了对赌
            //获取合伙人对赌的比列
            String tmpPartnerId = userPartner.get(1).getUserId();
            //计算合伙人对赌盈亏值
            BigDecimal partnerBack = userThroughBalance.multiply(partnerTransferOutRatio).multiply(BigDecimal.valueOf(-1)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            //获取该笔订单给合伙人的返佣
            UserBillChain partnerBillJb = accountDao.getUserBill( tmpPartnerId, sourceId, 38);
            if (partnerBillJb == null){
                return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到合伙人穿仓回补的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmpPartnerId+",订单:"+sourceId+"，类型:38").toString()};
            }
            log.info("morePartnerProfit--有合伙人(开启对赌)--合伙人穿仓回补校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},比例:{},数据库:{},计算得:{}", userId, tmpPartnerId, type, sourceId, partnerTransferOutRatio, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN),partnerBack);
            stringBuffer.append("--有合伙人(开启对赌)--合伙人穿仓回补校验--->用户:" + userId + ",默认合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId+"，比例:"+partnerTransferOutRatio+",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partnerBack.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            if (partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN).abs().compareTo(partnerBack.setScale(8, BigDecimal.ROUND_DOWN).abs()) != 0){
                flag = true;
                error.append("--有合伙人(开启对赌)--合伙人穿仓回补不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partnerBack.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            }
            //计算默认合伙人穿仓回补值
            BigDecimal defaultPartnerBack = userThroughBalance.multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).multiply(BigDecimal.valueOf(-1)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            String tmpDefaultPartnerId = userPartner.get(userPartner.size() - 1).getUserId();
            //获取该笔订单给合伙人的返佣
            UserBillChain defaultPartnerBillJb = accountDao.getUserBill(tmpDefaultPartnerId, sourceId, 38);
            if (defaultPartnerBillJb == null){
                return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到默认合伙人穿仓回补的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmpDefaultPartnerId+",订单:"+sourceId+"，类型:38").toString()};
            }
            stringBuffer.append("--有合伙人(开启对赌)--未查到默认合伙人穿仓回补的校验--->用户:" + userId + ",默认合伙人:" + tmpDefaultPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" +defaultPartnerBack ).append("</br>");
            log.info("morePartnerProfit--有合伙人(开启对赌)--未查到默认合伙人穿仓回补的校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmpDefaultPartnerId, type, sourceId, defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN),defaultPartnerBack);
            if (defaultPartnerBack.abs().compareTo(defaultPartnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0){
                flag = true;
                error.append("--有合伙人(开启对赌)--未查到默认合伙人穿仓回补的不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmpDefaultPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + defaultPartnerBack).append("</br>");
            }

        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 计算仓位的浮动盈亏
     * 币数量 = 张数 * 合约面值
     * 浮盈亏 = （标记价 - 开仓均价）* 币数量 * 合约方向
     * @param positionChains
     * @param indexPrice
     * @return
     */
    public BigDecimal floatProfitLoss(List<PositionChain> positionChains,JSONObject indexPrice) {
        BigDecimal total = BigDecimal.ZERO;
        for (PositionChain pc:positionChains) {
            BigDecimal oneLotSize = accountDao.instruments(pc.getSymbol());
            if (pc.getDirection().equalsIgnoreCase("long")){
                total = total.add((indexPrice.getBigDecimal(pc.getSymbol()).subtract(pc.getOpenPrice())).multiply(pc.getQuantity().multiply(oneLotSize)));
            }else {
                total = total.add((pc.getOpenPrice().subtract(indexPrice.getBigDecimal(pc.getSymbol()))).multiply(pc.getQuantity().multiply(oneLotSize)));
            }
        }
        return total;
    }

    /**
     * 计算仓位的总手续费
     * 手续费=开仓价格*数量*合约面值*费率
     * @param positionChains
     * @return
     */
    public BigDecimal fee(List<PositionChain> positionChains) {
        BigDecimal total = BigDecimal.ZERO;
        for (PositionChain pc:positionChains) {
            BigDecimal oneLotSize = accountDao.instruments(pc.getSymbol());
            total = total.add(pc.getOpenPrice().multiply(pc.getQuantity()).multiply(oneLotSize).multiply(Config.taker));
        }
        return total;
    }

    /**
     * 计算仓位的总保证金
     * 保证金 = 开仓价 * 张数 * 合约面值  / 杠杆
     * @param positionChains
     * @return
     */
    public BigDecimal margin(List<PositionChain> positionChains) {
        BigDecimal total = BigDecimal.ZERO;
        for (PositionChain pc:positionChains) {
            BigDecimal oneLotSize = accountDao.instruments(pc.getSymbol());
//            total = total.add(pc.getOpenPrice().multiply(pc.getQuantity()).multiply(oneLotSize).divide(BigDecimal.valueOf(pc.getLeverage()),Config.newScale,BigDecimal.ROUND_UP));
            total = total.add(pc.getMargin());
        }
        return total;
    }
}
