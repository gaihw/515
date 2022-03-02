package com.zmj.demo.common;


import com.zmj.demo.config.Config;
import com.zmj.demo.dao.demodata.auto.CaseDao;
import com.zmj.demo.dao.dev1.AccountDao;
import com.zmj.demo.domain.auto.CaseExecuteChain;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SqlUtil {

    @Autowired
    private CaseDao caseDao;

    @Autowired
    private AccountDao accountDao;

    /**
     * 获取执行用例集的详细接口信息
     * @param caseList
     * @return
     */
    public List<CaseExecuteChain> getCaseExecuteList(List<Integer> caseList,Integer index,Integer limit){
        System.out.println(caseList);
        List<CaseExecuteChain> caseExecuteChainList = caseDao.caseAllInfoById(caseList,index,limit);
        log.info("用例集:{},信息集:{}",caseList,caseExecuteChainList);
        return caseExecuteChainList;
    }

    /**
     * 用例执行后，更新执行结果
     * @param caseId
     * @param response
     * @param is_success
     */
    public void updateCaseExecuteResult(Integer caseId,String response,Integer is_success){
        int result = 0;
        try{
            result = caseDao.executeResult(caseId,response,is_success);
            if (result == 1){
                log.info("用例执行后，结果更新成功:::用例id:{},接口响应:{},执行结果:{}",caseId,response,is_success);
            }else {
                log.info("用例执行后，结果更新失败:::用例id:{},接口响应:{},执行结果:{}",caseId,response,is_success);
            }
        }catch (Exception e){
            log.info("用例执行后，结果报异常:::用例id:{},接口响应:{},执行结果:{},异常:{}",caseId,response,is_success,e.toString());
        }
    }

    /**
     * 根据用户ID，查出其所有上级的合伙人列表，列表的第一个为其自己，最后一个为顶级合伙人
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<UserDistributorChain> getUserPartner(String userId) {
        //用户及合伙人列表
        List<UserDistributorChain> user_partner = new ArrayList<UserDistributorChain>();
        UserDistributorChain user_distributor = accountDao.getUserDistributor(userId);
        //如果当前用户为顶级合伙人，直接返回
        if (userId.equalsIgnoreCase(Config.high_partner)){
            user_partner.add(user_distributor);
            return user_partner;
        }
        //列表插入当前查询的用户
        user_partner.add(user_distributor);
        //当前用户，查出合伙人，如果不是顶级合伙人，则根据合伙人id查询信息，并添加到列表，然后再迭代，直到找到顶级合伙人
        while (!user_distributor.getPartnerId().contains(Config.high_partner)
                &&!user_distributor.getParentId().contains(Config.high_partner)){
            user_distributor = accountDao.getUserDistributor(user_distributor.getPartnerId());
            if (user_distributor != null){
                user_partner.add(user_distributor);
            }
        }
        //迭代结束后，查出顶级合伙人的信息，并添加到列表
        user_distributor = accountDao.getUserDistributor(user_distributor.getPartnerId());
        user_partner.add(user_distributor);
        log.info("用户:{},合伙人:{}",userId,user_partner);
        return user_partner;
    }

    /**
     * 查询user_bill表，type值对应的说明
     * @param type
     * @return
     */
    public String getState(int type){
        switch (type){
            case 1:
                return "充值";
            case 2:
                return "提现";
            case 3:
                return "开仓-保证金";
            case 4:
                return "平仓-平仓收益";
            case 5:
                return "转入";
            case 6:
                return "转出";
            case 7:
                return "闪电-过夜费-扣用户";
            case 14:
                return "闪电-合伙人过夜费（得到过夜费）给合伙人";
            case 16:
                return "合伙人手续费  (合伙人收取下面邀请人的交易手续费) 区分普通和默认";
            case 21:
                return "计划委托";
            case 22:
                return "手动撤单 --取消订单解冻保证金";
            case 23:
                return "闪电-跟单跟随者支出返佣 -扣用户";
            case 24:
                return "闪电-跟单leader收入返佣 查询是否默认合伙人是带单老师";
            case 27:
                return "和用户对赌收益(头寸分佣) 区分普通和默认";
            case 30:
                return "无限模式盈利转入(实盘收入) 虚增";
            case 32:
                return "合伙人保证金转入";
            case 33:
                return "合伙人保证金转出";
            case 34:
                return "部分平仓";
            case 35:
                return "追加保证金";
            case 38:
                return "穿仓回退(用户账户不够扣,扣合伙人或者平台的钱)";
            case 39:
                return "资金不足系统撤单(全仓用户账户不够扣的时候取消委托单)";
            case 40:
                return "资金超限系统撤单(全仓用户账户下的单超过了最大限制的时候取消委托单)";
            case 41:
                return "返手续费(给用户) 直接去掉不去返佣";
            case 43:
                return "兑换(收入)";
            case 44:
                return "兑换(支出)";
            case 45:
                return "兑换手续费(合伙人收入)";
            case 47:
                return "限价委托";
            case 48:
                return "执行计划委托";
            case 49:
                return "不购买全仓撤单";
            case 50:
                return "资金费率结算";
            case 51:
                return "开仓手续费(永续合约) -扣用户";
            case 52:
                return "平仓手续费 -扣用户";
            case 54:
                return "穿仓回补（给用户加钱到0）给用户";
            case 55:
                return "逐仓保证金划入";
            case 56:
                return "逐仓保证金划出";
            case 58:
                return "订单成交后，退还订单冻结保证金";
//            case 59:
//                return "订单成交后，退还订单冻结保证金";
            default:
                return "无";
        }
    }
}
