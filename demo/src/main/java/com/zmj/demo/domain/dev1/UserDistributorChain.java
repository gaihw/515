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
    private int type;
    private String defaulted;
    private int openBet;
    private String isProfession;
    private String config;

    @Override
    public String toString() {
        return "{" +
                "\"userId\":\"" + userId + "\"," +
                "\"parentId\":\"" + parentId + "\"," +
                "\"gparentId\":\"" + gparentId + "\"," +
                "\"partnerId\":\"" + partnerId + "\"," +
                "\"distId\":\"" + distId + "\"," +
                "\"level\":\"" + level + "\"," +
                "\"type\":" + type +","+
                "\"defaulted\":\"" + defaulted + "\"," +
                "\"openBet\":" + openBet +","+
                "\"isProfession\":\"" + isProfession + "\"," +
                "\"config\":" + config  +
                "}";
    }
}
