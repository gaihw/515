package com.zmj.demo.dao.demodata;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserDao {
    /**
     * 根据手机号查询用户
     */
    @Select("SELECT count(*) FROM `demo`.`user_base_info` WHERE user_name=#{username}")
    int check(@Param("username") String username);

    @Insert("INSERT INTO `demo`.`user_base_info`( `user_name`,`nick_name`,`login_pwd`,`mobile`,`create_date`) VALUES (  #{username},#{name},#{password},#{mobile},now())")
    int register(@Param("username") String username,@Param("name") String name,@Param("password") String password,@Param("mobile") String mobile);


    @Select("SELECT login_pwd FROM `demo`.`user_base_info` WHERE user_name=#{username}")
    String getPwd(@Param("username") String username);
}
