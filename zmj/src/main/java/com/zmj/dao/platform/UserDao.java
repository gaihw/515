package com.zmj.dao.platform;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserDao {
    /**
     * 根据手机号查询用户
     */
    @Select("SELECT count(*) FROM `sonar`.`user_base_info` WHERE user_name=#{username}")
    int check(@Param("username") String username);

    @Insert("INSERT INTO `sonar`.`user_base_info`( `user_name`,`nick_name`,`login_pwd`,`mobile`) VALUES (  #{username},#{name},#{password},#{mobile})")
    int register(@Param("username") String username,@Param("name") String name,@Param("password") String password,@Param("mobile") String mobile);


    @Select("SELECT login_pwd FROM `sonar`.`user_base_info` WHERE user_name=#{username}")
    String getPwd(@Param("username") String username);
}
