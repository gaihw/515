package com.zmj.demo.dao.dev;

import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SmsEmailCode {


    @Select("SELECT id,created_date as createdDate,mobile,email,template_params as templateParams,status " +
            "FROM `bib_intgr`.`intgr_message` " +
            "WHERE `template_code` IN (SELECT DISTINCT template_code FROM `bib_intgr`.`intgr_message` WHERE `template_params` LIKE '%code%' ) " +
            "ORDER BY `id` DESC " +
            "LIMIT 0,10")
    List<SmsEmailcodeDomain> getList();

}
