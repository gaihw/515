package com.zmj.demo.domain.dev1;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class UserDistributorChain {
    private String userId;
    private String parentId;
    private String gparentId;
    private String partnerId;
    private String distId;
    private String level;
    private String type;
    private String defaulted;
    private String openBet;
    private String isProfession;
    private String config;
}
