package com.example.demo.utils;


import com.example.demo.rabbitmq.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Deposit {
    @Autowired
    private MsgProducer msgProducer;

    public void deposit(){
        HashMap hashMap = new HashMap();
        hashMap.put("a",1);
        hashMap.put('b',2);
        hashMap.put("c",true);
        msgProducer.send2TopicTestBQueue(hashMap.toString());
    }
}
