package com.zmj.demo.dao.dev1;

import com.zmj.demo.domain.dev1.UserBalanceChain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountDao {

    @Select("SELECT user_id userId,currency_id currencyId,balance,hold FROM `bib_cfd`.`user_balance` WHERE `user_id` = '#{userId}' LIMIT 0,1")
    List<UserBalanceChain> getUserBalanceHold(@Param("userId") String userId);
}
