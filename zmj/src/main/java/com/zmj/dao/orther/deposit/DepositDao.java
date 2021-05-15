package com.zmj.dao.orther.deposit;

import com.zmj.domain.orther.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepositDao {
    @Select("SELECT * FROM `58wallet`.`tb_wallet_receive_record` WHERE `status` = '2'")
    List<DepositChain> getDeposit();

}
