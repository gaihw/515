package newly.yizhichuan;

import com.alibaba.fastjson.JSONObject;
import newly.Addanno;
import newly.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login {
    static {
        System.setProperty("fileName", "yizhichuan/login.log");
    }
    public static Logger log = LoggerFactory.getLogger(Login.class);
    public static void main(String[] args) {
        String url = "https://crm.aboatedu.com/crm-api/employee/login";
        String params = "{\"account\":\"zhaomeijing\",\"pw\":\"f5e56447b23c84e04f41593ca96fb3d5\"}";
        System.out.println(BaseUtils.postByJson(url,params));
    }

    public static String getToken() {
        String url = "https://crm.aboatedu.com/crm-api/employee/login";
        String params = "{\"account\":\"zhaomeijing\",\"pw\":\"f5e56447b23c84e04f41593ca96fb3d5\"}";
        String res = BaseUtils.postByJson(url,params);
        return JSONObject.parseObject(res).getJSONObject("data").getJSONObject("employee").getString("token");
    }
}
