package com.test.service;

import com.test.domain.Sms;
import com.test.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * 业务逻辑
 */

public interface UserInfoService {
    /**
     * 根据用户手机号，查询用户信息
     *
     */
    User findUserInfoByMobile(String mobile);
    /**
     * 根据用户id，查询用户信息
     *
     */
    User findUserInfoByUserId(int id);
    /**
     * 根据用户ID或者手机号，查询用户信息
     */
    User findUserInfoByUserIdOrMobile(int id, String mobile);

    /**
     * 根据手机号查询短信验证码
     * @param mobile
     * @return
     */
    Sms sms(String mobile);
}
