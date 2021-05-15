package old.Calc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KpiQuery {
    public static void main(String[] args) {
        String url = "http://192.168.200.51:10320/risk/tbex-risk-engine-config/report/kpiQuery";
        String params = "{\"businessId\":3,\"userType\":0,\"kpiList\":[{\"kpiKey\":\"\",\"kpiOperate\":\"\",\"kpiValue\":\"\"}],\"pageIndex\":1,\"pageSize\":2000}";
        String auther = "Bearer 38f0f256-f980-1afa-4b85-ab9b-cda907a076cb";

        String response = BaseUtils.postByJson(url,auther,params);
        JSONArray userList = JSONObject.parseObject(response).getJSONObject("data").getJSONArray("list");
        System.out.println("userList==="+userList.size());
        List<String> user = new ArrayList<String>();
        for (int i = 0  ; i < userList.size(); i++){
            user.add("'"+userList.getJSONObject(i).getString("userId")+"'");
        }
        System.out.println(user);
    }
}
