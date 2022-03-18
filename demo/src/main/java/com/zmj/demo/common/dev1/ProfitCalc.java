package com.zmj.demo.common.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.config.Config;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.dev1.PositionActionChain;
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
public class ProfitCalc {

    @Autowired
    private AccountDao accountDao;


    /**
     * 只有默认合伙人，计算盈亏对赌
     * @param userPartner
     * @param userBillChain
     * @return
     */
    public String[] defaultPartnerProfit(List<UserDistributorChain> userPartner , UserBillChain userBillChain){
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
        List<PositionActionChain> positionAction = accountDao.positionAction(userId,sourceId,null);
        BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
        //盈亏对赌校验
        //获取订单类型
//        SwapOrderChain swapOrderChain = accountDao.getMarginType(userBillChain.getSourceId());
        BigDecimal userProfit = BigDecimal.ZERO;
        //逐仓单,计算盈亏需要去clearing_transfer中取值，因为盈亏和保证金和在一起计算了
//        if (swapOrderChain.getMarginType().equalsIgnoreCase("FIXED")){
//            userProfit = accountDao.getClearlingTransfer(userBillChain.getUserId(),userBillChain.getSourceId(),swapOrderChain.getMarginType()).getProfit().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
//        }else {//全仓单
//            userProfit = userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
//        }
        //多仓
        if (positionAction.get(0).getDirection().equalsIgnoreCase("LONG")){
            //(平仓价格-开仓价格)*数量*面值
            userProfit = (positionAction.get(0).getClosePrice().subtract(positionAction.get(0).getOpenPrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
        }else {
            //(开仓价格-平仓价格)*数量*面值
            userProfit = (positionAction.get(0).getOpenPrice().subtract(positionAction.get(0).getClosePrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
        }
        //获取该笔订单给合伙人的返佣
        UserBillChain partnerBillJb = accountDao.getUserBill( partnerId, sourceId, 27);
        if (partnerBillJb == null){
            return new String[]{"",stringBuffer.append("--只有默认合伙人--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+partnerId+",订单:"+sourceId+"，类型:27").append("</br>").toString()};
        }
        log.info("defaultPartnerProfit--只有默认合伙人--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},计算得:{}", userId, partnerId, type, sourceId, userProfit);
        stringBuffer.append("--只有默认合伙人--盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId +"数据库得:"+partnerBillJb.getSize()+ ",计算得:" + userProfit ).append("</br>");
            if (userProfit.abs().compareTo(partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                flag = true;
                error.append("--只有默认合伙人--盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId +"数据库得:"+partnerBillJb.getSize()+ ",计算得:" + userProfit).append("</br>");
            }

        return new String[]{stringBuffer.toString(),error.toString()};
    }

    /**
     * 有普通合伙人和默认合伙人，计算盈亏对赌
     * @param userPartner
     * @param userBillChain
     * @return
     */
    public String[] morePartnerProfit(List<UserDistributorChain> userPartner , UserBillChain userBillChain){
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
        List<PositionActionChain> positionAction = accountDao.positionAction(userId,sourceId,null);
        BigDecimal oneLotSize = accountDao.instruments(positionAction.get(0).getSymbol());
        //盈亏对赌校验
        //获取订单类型
//        SwapOrderChain swapOrderChain = accountDao.getMarginType(userBillChain.getSourceId());
        BigDecimal userProfit = BigDecimal.ZERO;
        //逐仓单,计算盈亏需要去clearing_transfer中取值，因为盈亏和保证金和在一起计算了
//        if (swapOrderChain.getMarginType().equalsIgnoreCase("FIXED")){
//            userProfit = accountDao.getClearlingTransfer(userBillChain.getUserId(),userBillChain.getSourceId(),swapOrderChain.getMarginType()).getProfit().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
//        }else {//全仓单
//            userProfit = userBillChain.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN);
//        }
        //多仓
        if (positionAction.get(0).getDirection().equalsIgnoreCase("LONG")){
            //(平仓价格-开仓价格)*数量*面值
            userProfit = (positionAction.get(0).getClosePrice().subtract(positionAction.get(0).getOpenPrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
        }else {
            //(开仓价格-平仓价格)*数量*面值
            userProfit = (positionAction.get(0).getOpenPrice().subtract(positionAction.get(0).getClosePrice())).multiply(positionAction.get(0).getQuantity()).multiply(oneLotSize).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
        }
        BigDecimal partnerTransferOutRatio = BigDecimal.ZERO;
        if (!userPartner.get(1).getConfig().equalsIgnoreCase("")){
            partnerTransferOutRatio = JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio") == null
                    ? BigDecimal.ZERO:JSONObject.parseObject(userPartner.get(1).getConfig()).getBigDecimal("partnerTransferOutRatio");
        }
        //如果有普通合伙人，但是普通合伙人未开启对赌或者合伙人的类型type不是0，那合伙人不参与盈亏的对赌，只和默认合伙人有关
        //普通合伙人是合伙人，并且开启了对赌，但是配置的比例为0，那也不给该普通合伙人对赌
//        if(userPartner.get(1).getOpenBet() == 0 || userPartner.get(1).getType() !=0  || partnerTransferOutRatio.compareTo(BigDecimal.ZERO) == 0) {
        if(userPartner.get(1).getOpenBet() == 0 || partnerTransferOutRatio.compareTo(BigDecimal.ZERO) == 0) {
            partnerId =userPartner.get(userPartner.size()-1).getUserId();
            //获取该笔订单给合伙人的返佣
            UserBillChain partnerBillJb = accountDao.getUserBill(partnerId, sourceId, 27);
            if (partnerBillJb == null) {
                return new String[]{"",stringBuffer.append("--有合伙人(未开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:" + userId + "，合伙人ID:" + partnerId + ",订单:" + sourceId + "，类型:27").append("</br>").toString()};
            }
            log.info("morePartnerProfit--有合伙人(未开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, partnerId, type, sourceId, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN), userProfit);
            stringBuffer.append("--有合伙人(未开启对赌)--盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userProfit).append("</br>");
            if (userProfit.abs().compareTo(partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0) {
                flag = true;
                error.append("--有合伙人(未开启对赌)--盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + partnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + userProfit).append("</br>");
            }
        }else {//如果合伙人列表长度大于2，代表有合伙人，开启了对赌
            //如果合伙人的非合伙人，那么给该合伙人对赌按照默认配置，为0.7
            if (userPartner.get(1).getType() !=0 ){
                partnerTransferOutRatio = Config.partnerTransferOutRatio;
            }
            String tmpPartnerId = userPartner.get(1).getUserId();
            //计算合伙人对赌盈亏值
            BigDecimal partnerProfit = userProfit.multiply(partnerTransferOutRatio).multiply(BigDecimal.valueOf(-1)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            //获取该笔订单给合伙人的返佣
            UserBillChain partnerBillJb = accountDao.getUserBill( tmpPartnerId, sourceId, 27);
            if (partnerBillJb == null){
                return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmpPartnerId+",订单:"+sourceId+"，类型:27").append("</br>").toString()};
            }
            log.info("morePartnerProfit--有合伙人(开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},比例:{},数据库:{},计算得:{}", userId, tmpPartnerId, type, sourceId, partnerTransferOutRatio, partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN),partnerProfit);
            stringBuffer.append("--有合伙人(开启对赌)--盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId+"，比例:"+partnerTransferOutRatio+",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partnerProfit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            if (partnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs().compareTo(partnerProfit.setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0){
                flag = true;
                error.append("--有合伙人(开启对赌)--盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmpPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + partnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + partnerProfit.setScale(8, BigDecimal.ROUND_DOWN)).append("</br>");
            }
            //计算默认合伙人对赌盈亏值
            BigDecimal defaultPartnerProfit = userProfit.multiply(BigDecimal.ONE.subtract(partnerTransferOutRatio)).multiply(BigDecimal.valueOf(-1)).setScale(Config.newScale, BigDecimal.ROUND_DOWN);
            String tmpDefaultPartnerId = userPartner.get(userPartner.size() - 1).getUserId();
            //获取该笔订单给合伙人的返佣
            UserBillChain defaultPartnerBillJb = accountDao.getUserBill(tmpDefaultPartnerId, sourceId, 27);
            if (defaultPartnerBillJb == null){
                return new String[]{"",stringBuffer.append("--有合伙人(开启对赌)--未查到默认合伙人对赌的流水账单，请查看！数据：用户:"+userId+"，合伙人ID:"+tmpDefaultPartnerId+",订单:"+sourceId+"，类型:27").toString()};
            }
            stringBuffer.append("--有合伙人(开启对赌)--盈亏对赌校验--->用户:" + userId + ",默认合伙人:" + tmpDefaultPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" +defaultPartnerProfit ).append("</br>");
            log.info("morePartnerProfit--有合伙人(开启对赌)--盈亏校验--->用户:{},默认合伙人:{},交易类型:{},订单:{},数据库:{},计算得:{}", userId, tmpDefaultPartnerId, type, sourceId, defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN),defaultPartnerProfit);
            if (defaultPartnerProfit.abs().compareTo(defaultPartnerBillJb.getSize().setScale(Config.newScale, BigDecimal.ROUND_DOWN).abs()) != 0){
                flag = true;
                error.append("--有合伙人(开启对赌)--盈亏对赌不正确，请检查--->用户:" + userId + ",默认合伙人:" + tmpDefaultPartnerId + ",交易类型:" + type + ",订单:" + sourceId + ",数据库:" + defaultPartnerBillJb.getSize().setScale(8, BigDecimal.ROUND_DOWN) + ",计算得:" + defaultPartnerProfit).append("</br>");
            }

        }
        return new String[]{stringBuffer.toString(),error.toString()};
    }
}
