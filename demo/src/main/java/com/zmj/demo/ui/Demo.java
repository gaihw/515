package com.zmj.demo.ui;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Demo {

        @Autowired
        private HttpUtil httpUtils;


        @RequestMapping(value = "/test",method = RequestMethod.GET)
        public void test(){

                System.setProperty("webdriver.chrome.driver","libs\\chromedriver92.exe");

                JSONObject captcha_res = JSONObject.parseObject(httpUtils.postByForm("http://dg-test.allintechinc.com/sso/api/v1/captcha/number",null));

                String login_param = "{\"captcha\":"+captcha_res.getJSONObject("data").getString("captcha")+"," +
                        "\"key\":\""+captcha_res.getJSONObject("data").getString("key")+"\"," +
                        "\"username\":\"13020071928\"," +
                        "\"password\":\"0190621d0efa98a61dc90ebce9d5f822\"}";

                String login_headers = "{\"appId\":\"data-governance\"," +
                        " \"Connection\":\"keep-alive\"," +
                        " \"Host\":\"dg-test.allintechinc.com\", " +
                        "\"Content-Type\":\"application/json\", " +
                        "\"production\":\"DPAs\"}";

                JSONObject login_res = JSONObject.parseObject(httpUtils.postByJson("http://dg-test.allintechinc.com/sso/api/v1/login",login_param,login_headers));

                System.out.println(login_res);
                String uuid = login_res.getJSONObject("data").getString("uuid");
                String token = login_res.getJSONObject("data").getString("token");
                String userinfo_headers = "{\"uid\":\""+uuid+"\"," +
                        "\"token\":\""+token+"\"," +
                        "\"User-Agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36\"," +
                        "\"Accept\":\"application/json, text/plain, */*\"," +
                        "\"appId\":\"data-governance\"," +
                        "\"Connection\":\"keep-alive\"," +
                        "\"Host\":\"dg-test.allintechinc.com\"," +
                        "\"Content-Type\":\"application/json\"," +
                        "\"production\":\"DPAs\" }";

                JSONObject userinfo_res = JSONObject.parseObject(httpUtils.get("http://dg-test.allintechinc.com/dpas/api/v1/user/userinfo",userinfo_headers));
                System.out.println(userinfo_res);

                String userItem = userinfo_res.getJSONObject("data").getString("userItem");
                String actionItems = userinfo_res.getJSONObject("data").getString("actionItems");
                String applicationGroupItems = userinfo_res.getJSONObject("data").getString("applicationGroupItems");

                //实例化一个Chrome浏览器的实例
                WebDriver driver = new ChromeDriver();
                //设置打开的浏览器窗口最大化
                driver.manage().window().maximize();
                //使用get()打开一个网站
                driver.get("http://dg-test.allintechinc.com/dashboard");
                //getTitle()获取当前页面的title，用System.out.println()打印在控制台
                System.out.println("当前打开页面的标题是： "+ driver.getTitle());

                long expire = System.currentTimeMillis()+1*24*60*60*1000;

                uuid = "{\"value\":\""+uuid+"\",\"expire\":"+expire+"}";
                token = "{\"value\":\""+token +"\",\"expire\":"+expire+"}";
                userItem = "{\"value\":"+userItem+",\"expire\":"+expire+"}";
                actionItems = "{\"value\":"+actionItems+",\"expire\":"+expire+"}";
                applicationGroupItems = "{\"value\":"+applicationGroupItems+",\"expire\":"+expire+"}";

                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.localStorage.setItem(\"al__UID\",arguments[0]);",uuid);
                js.executeScript("window.localStorage.setItem(\"al__Access-Token\",arguments[0]);",token);
                js.executeScript("window.localStorage.setItem(\"al__ACTION_ITEMS\",arguments[0]);",actionItems);
                js.executeScript("window.localStorage.setItem(\"al__USER_INFO\",arguments[0]);",userItem);
                js.executeScript("window.localStorage.setItem(\"al__APPLICATION_GROUP_ITEMS\",arguments[0]);",applicationGroupItems);


                driver.navigate().refresh();
                driver.get("http://dg-test.allintechinc.com/dashboard");

        }

}
