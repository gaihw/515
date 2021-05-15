package newly.yizhichuan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newly.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindMinRenTang {
    static {
        System.setProperty("fileName", "yizhichuan/login.log");
    }
    public static Logger log = LoggerFactory.getLogger(FindMinRenTang.class);
    public static void main(String[] args) {
        String token = "28376368400165f2a1166603c2ea5a20";//Login.getToken();

        //具体信息查询
        String url_find = "https://crm.aboatedu.com/crm-api/Follow/find?pageSize=10&pageIndex=1&studentId=";
        //领取
        String url_linqu = "https://crm.aboatedu.com/crm-api/minRenTangController/linqu?id=";
        //总领取的条数
        int account = 0;

        head:for (int pageIndex = 1; pageIndex < 100; pageIndex++) {
            String url_findMinRenTang = "https://crm.aboatedu.com/crm-api/minRenTangController/findMinRenTang?type=4&pageSize=100&pageIndex="+pageIndex+"&dropPool=0";
            String res_findMinRenTang = BaseUtils.getByForm(url_findMinRenTang,token);
            System.out.println("res_findMinRenTang:"+res_findMinRenTang);
            //获取列表
            JSONArray pageData_findMinRenTang = JSONObject.parseObject(res_findMinRenTang).getJSONObject("data").getJSONArray("pageData");

            for (int i = 0; i < pageData_findMinRenTang.size(); i++) {
                //筛选列表为job的
                if (pageData_findMinRenTang.getJSONObject(i).getString("remark").contains("job放弃")){

                    Integer studentId = pageData_findMinRenTang.getJSONObject(i).getInteger("studentId");
                    Integer aid = pageData_findMinRenTang.getJSONObject(i).getInteger("aid");

                    //请求单条的详情信息
                    String res_pageData_find = BaseUtils.getByForm(url_find+studentId,token);
                    JSONArray pageData_find = JSONObject.parseObject(res_pageData_find).getJSONObject("data").getJSONArray("pageData");

                    //flag为true时，标记该条数据可以领取
                    Boolean flag = true;
                    //查询某一条数据的具体信息，判断其中的内容是否包含关键字
                    for (int j = 0; j < pageData_find.size(); j++) {
                        String planNote = pageData_find.getJSONObject(j).getString("planNote");

                        if(pageData_find.getJSONObject(j).getString("type").contains("销售操作")){
                            if(planNote.contains("拒接") || planNote.contains("不考虑")){
                                flag = false;
                                break;
                            }
                        }
                    }
                    //如果flag=true,即领取
                    if (false && flag){
                        //点击领取
                        String res_linqu = BaseUtils.getByForm(url_linqu+aid,token);
                        if (JSONObject.parseObject(res_linqu).getString("errCode").contains("100318")){
                            System.out.println("已达到上限30条！");
                            break head;
                        }
                        else {
                            System.out.println("已领取studentId->"+studentId);
                            account += 1;
                        }
                    }
                }
            }
            if (account <=30){
                continue;
            }else {
                break head;
            }
        }
        System.out.println("account=="+account);




    }
}
