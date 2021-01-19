package com.example.demo.scheduled;

import com.example.demo.utils.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoTask {
    @Autowired
    private Deposit deposit;

    @Scheduled(cron = "*/5 * * * * ? ")
    public void testTask() {
        System.out.println("定时任务执行...");
        deposit.deposit();
    }
}
