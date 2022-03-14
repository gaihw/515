package com.zmj.demo.notice;

import com.zmj.demo.service.impl.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

//@RestController
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @RequestMapping(value = "/kafka",method = RequestMethod.GET)
    public void test_kafka(@RequestParam("data") String data){

        String topic = "test";
//                String data = "kafka发送消息测试...";
//
////        NewTopic
//        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, data);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                log.error("消息发送失败,topic:{},消息:{}",topic,data);
//            }
//
//            @Override
//            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
//                log.info("消息发送成功,topic:{},消息:{}",topic,data);
//            }
//        });


        try {
            kafkaProducerService.sendMessageSync(topic,data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
