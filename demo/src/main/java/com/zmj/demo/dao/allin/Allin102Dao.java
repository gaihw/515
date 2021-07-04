package com.zmj.demo.dao.allin;

import com.zmj.demo.domain.allin.Allin102Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Allin102Dao {


    @Select("SELECT * FROM `allin_test`.`test_date` LIMIT 0,1000")
    List<Allin102Domain> getList();

}
