package com.zmj.commom.deposit;


import com.alibaba.fastjson.JSON;
import com.zmj.commom.rabbitmq.MsgProducer;
import com.zmj.domain.orther.DepositChain;
import com.zmj.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Deposit {

    @Autowired
    private MsgProducer msgProducer;

    @Autowired
    private DepositService depositService;

    public void deposit(){
        List<DepositChain> list = depositService.getDeposit();
        list.forEach(l -> {
//            msgProducer.send2TopicTestBQueue(JSON.toJSONString(l));
            try {
                msgProducer.send2TopicTestBQueue(l);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
