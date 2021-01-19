package com.tbex.future.fake;

import com.tbex.future.back.matching.constant.OrderAction;
import com.tbex.future.back.matching.entity.Order;
import com.tbex.future.back.matching.entity.OrderLiquidationEnum;
import com.tbex.future.back.matching.entity.OrderRequest;
import com.tbex.future.back.matching.util.ProtostuffUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.IdGenerator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class App implements CommandLineRunner {
    List<String> securityGroup = Arrays.asList("future_security_test_new_1", "future_security_test_new_2", "future_security_test_new_3","future_security_test_new_4");
    String rocketNameSpace = "192.168.112.80:9876";//192.168.112.79:9876;192.168.112.81:9876";
    String userKey = "userIdKey";
    final int limit = 100;
    String topic = "future_matching_13";
    final AtomicLong sendSecs = new AtomicLong();
    final AtomicLong recvSecs = new AtomicLong();
    final AtomicLong sendCount = new AtomicLong();
    final AtomicLong recvCount = new AtomicLong();

    void init() throws  InterruptedException, MQClientException {

        int sep = limit / 100;
        int currentUserId = 0;
//        CountDownLatch countDownLatch = new CountDownLatch(securityGroup.size());
        for (final String security : securityGroup) {
            DefaultMQProducer producer = new DefaultMQProducer("fake-security" + "-" + security);
            producer.setNamesrvAddr(rocketNameSpace);
            producer.start();
            int baseUser = currentUserId++;
            System.out.println("Start producer: " + security);

//            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("fake-security-" + security);
//            consumer.setNamesrvAddr(rocketNameSpace);
//            consumer.subscribe(topic,"*");
//            consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
//                long cur = System.currentTimeMillis() / 1000;
//                if (recvSecs.get() < cur) {
//                    synchronized (recvSecs) {
//                        if (recvSecs.get() < cur) {
//                            System.out.println("recv: " + (recvCount.get() / (cur - recvSecs.get())) + "/s");
//                            recvCount.set(0);
//                            recvSecs.set(cur);
//                        }
//                    }
//                }
//                recvCount.incrementAndGet();
//                return ConsumeOrderlyStatus.SUCCESS;
//            });
//            consumer.start();

            new Thread(() -> {
                int c = limit;
                long start = System.currentTimeMillis();
                long last = start;
                long seq = 10000;
                Random random = new Random(System.nanoTime());
                while (true) {
                    try {
                        seq++;
                        Message message = new Message(
                                topic,
                                "88",
                                null,
                                ProtostuffUtils.serialize(OrderRequest.builder()
                                        .requestId(seq)
                                        .action(OrderAction.ORDER.getCode())
                                        .userId(0L)
                                        .order(Order.builder()
                                                .id(System.nanoTime())
                                                .userId(baseUser * seq)
                                                .currencyPairId(1)
                                                .priceType(1)
                                                .tradeDirection(random.nextBoolean() ? 1 : 2)
                                                .price(BigDecimal.valueOf(random.nextInt(100)).toPlainString())
                                                .lot(BigDecimal.valueOf(random.nextInt(100)).toPlainString())
                                                .executedSize(BigDecimal.ZERO.toPlainString())
                                                .timeInForce(0)
                                                .postOnly(0)
                                                .orderType(OrderLiquidationEnum.NORMAL.getCode())
                                                .oc(0)
                                                .createTime(System.currentTimeMillis())
                                                .build())
                                        .build()
                                ));
                        message.putUserProperty("userIdKey", (baseUser * seq) + "");
                        message.putUserProperty("KEY_SECURITY_ID", security);
                        message.putUserProperty("KEY_USER_ID",(baseUser * seq) + "");
                        producer.send(message, (mqs, msg, o) -> mqs.get(0) , null);
                    } catch (MQClientException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemotingException e) {
                        e.printStackTrace();
                    } catch (MQBrokerException e) {
                        e.printStackTrace();
                    }
//                    long cur = System.currentTimeMillis() / 1000;
//                    if (sendSecs.get() < cur) {
//                        synchronized (sendSecs) {
//                            if (sendSecs.get() < cur) {
//                                System.out.println("send: " + (sendCount.get() / (cur - sendSecs.get())) + "/s");
//                                sendCount.set(0);
//                                sendSecs.set(cur);
//                            }
//                        }
//                    }
//                    sendCount.incrementAndGet();
                }
            }).start();
        }
//        countDownLatch.await();
    }

    @Override
    public void run(String... strings) throws Exception {
        init();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
