package com.test.dao.test;

import com.test.domain.TestUser;
import com.test.domain.User;
import org.apache.ibatis.annotations.Param;

public interface TestUserDao {
    /**
     * 根据用户名密码，查询用户是否存在
     */
    int findTestUser(@Param("userName") String userName, @Param("password") String password);
}
