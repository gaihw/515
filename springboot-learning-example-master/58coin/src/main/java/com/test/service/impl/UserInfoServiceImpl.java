package com.test.service.impl;

import com.test.dao.userBaseInfo.UserDao;
import com.test.domain.Sms;
import com.test.domain.User;
import com.test.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserDao userDao;

    public User findUserInfoByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }

    public User findUserInfoByUserId(int id){
        return userDao.findByUserId(id);
    }
    public User findUserInfoByUserIdOrMobile(int id,String mobile){
        return userDao.findUser(id,mobile);
    }

    public Sms sms(String mobile){
        return userDao.sms(mobile);
    }
}
