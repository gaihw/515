package com.test.dao.userBaseInfo;

import com.test.domain.Sms;
import com.test.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    /**
     * 根据手机号，查询用户信息
     */
    User findByMobile(@Param("mobile" ) String mobile);
    /**
     * 根据用户ID，查询用户信息
     */
    User findByUserId(@Param("id") int id);
    /**
     * 根据用户ID或者手机号，查询用户信息
     */
    User findUser(@Param("id") int id,@Param("mobile") String mobile);

    /**
     * 根据手机号查询短信验证码
     * @param mobile
     * @return
     */
    Sms sms(@Param("mobile") String mobile);
}
