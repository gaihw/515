package com.test.domain;

import lombok.Data;

import java.sql.Time;

/**
 * 用户实体类
 */
@Data
public class User {
    /**
     *用户id
     */
    private int id ;
    /**
     * 虚拟用户id
     */
    private  int uid ;
    /**
     * 用户名，账号
     */
    private String userName;
    /**
     * 国家id
     */
    private int emailNation;
    /**
     * 邮件
     */
    private String email;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 密码
     */
    private String loginPwd;
    /**
     *交易密码
     */
    private String tradePwd ;
    /**
     * 密码强度
     */
    private  int pwdFlag ;
    /**
     * 国家id
     */
    private int nation;
    /**
     * 已绑定的手机号
     */
    private String mobile;
    /**
     * 谷歌验证码密钥
     */
    private String googleKey;
    /**
     * 状态标识
     */
    private int statusFlag;
    /**
     *用户配置标识
     */
    private int configFlag ;
    /**
     * 用户等级
     */
    private  int userLevel ;
    /**
     * 用户渠道来源
     */
    private int channel;
    /**
     * 推荐人id
     */
    private int inviterId;
    /**
     * 注册时间
     */
    private Time registerTime;
    /**
     * 修改时间
     */
    private Time updateTime;
}
