package com.zmj.demo.dao.dev;

import com.zmj.demo.domain.dev1.OptionsInfoChain;
import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.sql.DataSourceDefinition;
import java.util.List;

/**
 * 期权币种
 *
 * @author gaihw
 * @date 2023/3/13 23:04
 */

@Mapper
public interface OptionsInfoDao {

    @Select("SELECT instrument_name instrumentName,external_instrument_name externalInstrumentName  " +
            "FROM `bib_cfd`.`options_info` " +
            "WHERE state = 0 and currency = 'btc' and instrument_name like 'BTC-230425`%' " +
            "ORDER BY `instrument_id` DESC " +
            "LIMIT 0,1000")
    List<OptionsInfoChain> getList();

}
