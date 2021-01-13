package com.zmj.scheduled;

import com.zmj.commom.deposit.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DepositTask {
    @Autowired
    private Deposit deposit;

    @Scheduled(cron = "*/5 * * * * ? ")
    public void testTask() {
        deposit.deposit();
    }
}
