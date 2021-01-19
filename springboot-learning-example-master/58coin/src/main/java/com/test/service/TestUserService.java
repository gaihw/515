package com.test.service;

import com.test.domain.TestUser;
import com.test.domain.User;

/**
 * 业务逻辑
 */

public interface TestUserService {
    /**
     * 根据用户名和密码，查询用户是否存在
     */
    int findTestUser(String userName, String password);
}
