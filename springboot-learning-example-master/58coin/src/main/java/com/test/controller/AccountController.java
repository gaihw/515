package com.test.controller;

import com.test.domain.Account;
import com.test.domain.User;
import com.test.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    /**
     * 充钱
     * @param userId
     * @param currencyId
     * @param available
     */
    @RequestMapping(value = "/updateAccount" , method = RequestMethod.POST)
    public void updateAccount(@RequestParam(value = "userId" ,required = true ) int userId,
                              @RequestParam(value = "currencyId" ,required = true ) int currencyId,
                              @RequestParam(value = "available" ,required = true ) BigDecimal available){
        Account account = accountService.findByUserId(userId);
        if (account == null){
            accountService.depositAccount(userId,currencyId,available);
        }else{
            accountService.updateAccount(userId,currencyId,available);
        }
    }
}
