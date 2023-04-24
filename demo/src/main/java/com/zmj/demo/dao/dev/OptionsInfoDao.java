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
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/3/13 23:04
 */

@Mapper
public interface OptionsInfoDao {

    @Select("SELECT instrument_name instrumentName  " +
            "FROM `bib_cfd`.`options_info` " +
            "WHERE state = 0 " +
            "ORDER BY `instrument_id` DESC " +
            "LIMIT 0,1000")
    List<OptionsInfoChain> getList();

}
