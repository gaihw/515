package com.test.service.impl;

import com.test.dao.test.TestUserDao;
import com.test.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestUserServiceImpl implements TestUserService {

    @Autowired
    private TestUserDao testuserDao;


    public int findTestUser(String userName, String password){
        return testuserDao.findTestUser(userName,password);
    }
}
