package com.zmj.demo.dao;

import com.zmj.demo.domain.BaseDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BaseDao {


    @Select("SELECT * FROM `allin_test`.`test_date` LIMIT 0,1000")
    List<BaseDomain> getList();

}
