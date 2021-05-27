package newly.yizhichuan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newly.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindMinRenTangBak {
    static {
        System.setProperty("fileName", "yizhichuan/login.log");
    }
    public static Logger log = LoggerFactory.getLogger(FindMinRenTangBak.class);
    public static void main(String[] args) {
        String token = "b6f7e504278033a29c35f2e1adf0f415";//Login.getToken();

        //每条的具体信息查询
        String url_find = "https://crm.aboatedu.com/crm-api/Follow/find?pageSize=40&pageIndex=1&studentId=";
        //领取
        String url_linqu = "https://crm.aboatedu.com/crm-api/minRenTangController/linqu?id=";

        //总领取的条数
        int account = 0;

        head:for (int pageIndex = 1; pageIndex < 3; pageIndex++) {
            String url_findMinRenTang = "https://crm.aboatedu.com/crm-api/minRenTangController/findMinRenTang?type=4&pageSize=100&pageIndex="+pageIndex+"&dropPool=1";
            String res_findMinRenTang = BaseUtils.getByForm(url_findMinRenTang,token);
            System.out.println(pageIndex+"-->res_findMinRenTang:"+res_findMinRenTang);
            //获取列表
            JSONArray pageData_findMinRenTang = JSONObject.parseObject(res_findMinRenTang).getJSONObject("data").getJSONArray("pageData");

            for (int i = 0; i < pageData_findMinRenTang.size(); i++) {
                //array列表
                JSONObject jsonObject_findMinRenTang =  pageData_findMinRenTang.getJSONObject(i);

                //筛选列表为job的
                if (jsonObject_findMinRenTang.getString("remark").contains("job放弃")){

                    //过滤掉
                    if (jsonObject_findMinRenTang.getString("stuName").contains("剩余")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不加入")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不需要")
                            || jsonObject_findMinRenTang.getString("stuName").contains("微信")
                            || jsonObject_findMinRenTang.getString("stuName").contains("投诉")
                            || jsonObject_findMinRenTang.getString("stuName").contains("过")
                            || jsonObject_findMinRenTang.getString("stuName").contains("已升级")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不考虑")
                            || jsonObject_findMinRenTang.getString("stuName").contains("升级了")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不着急考")
                            || jsonObject_findMinRenTang.getString("stuName").contains("没意向")
                            || jsonObject_findMinRenTang.getString("stuName").contains("骗局")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不报")
                            || jsonObject_findMinRenTang.getString("stuName").contains("去年升级了")
                            || jsonObject_findMinRenTang.getString("stuName").contains("直接挂")
                            || jsonObject_findMinRenTang.getString("stuName").contains("停机")
                            || jsonObject_findMinRenTang.getString("stuName").contains("已经报名")
                            || jsonObject_findMinRenTang.getString("stuName").contains("全过了")
                            || jsonObject_findMinRenTang.getString("stuName").contains("投诉")
                            || jsonObject_findMinRenTang.getString("stuName").contains("已经升班")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不升")
                            || jsonObject_findMinRenTang.getString("stuName").contains("补考")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不参加")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不打算加入")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不考")
                            || jsonObject_findMinRenTang.getString("stuName").contains("退费")
                            || jsonObject_findMinRenTang.getString("stuName").contains("拒接")
                            || jsonObject_findMinRenTang.getString("stuName").contains("不想考了")

                    ){
                        continue  ;
                    }

                    Integer studentId = jsonObject_findMinRenTang.getInteger("studentId");
                    Integer aid = jsonObject_findMinRenTang.getInteger("aid");


//                    请求单条的详情信息
                    String res_pageData_find = BaseUtils.getByForm(url_find+studentId,token);
                    JSONArray pageData_find = JSONObject.parseObject(res_pageData_find).getJSONObject("data").getJSONArray("pageData");

//                    System.out.println(res_pageData_find);
                    //flag为true时，标记该条数据可以领取
                    Boolean flag = true;
                    //查询某一条数据的具体信息，判断其中的内容是否包含关键字

                    //销售条数
                    int xiaoshoucount = 1 ;
//                    for (int j = 0; j < pageData_find.size(); j++) {
//                        JSONObject jsonObject_find = pageData_find.getJSONObject(j);
//                        if(jsonObject_find.getString("type").contains("销售操作")){
//                            xiaoshoucount += 1;
//                        }
//                    }

                    if (xiaoshoucount <= 4){
                        for (int j = 0; j < pageData_find.size(); j++) {
                            JSONObject jsonObject_find = pageData_find.getJSONObject(j);
                            String planNote = jsonObject_find.getString("planNote");
                            String empName = jsonObject_find.getString("empName");


//                            if(jsonObject_find.getString("type").contains("销售操作")){
                                //过滤掉
                                if(planNote.contains("拒接")
                                        || planNote.contains("不考虑")
                                        || planNote.contains("朋友推荐，自己不是刚需，不急于拿证")
                                        || planNote.contains("挺自信有时间学")
                                        || planNote.contains("不考了")
                                        || planNote.contains("不需要")
                                        || planNote.contains("不参加不让再打电话")
                                        || planNote.contains("非要自己考")
                                        || planNote.contains("一般意向")
                                        || planNote.contains("骗人")
                                        || planNote.contains("别打电话")
                                        || planNote.contains("意向一般")
                                        || planNote.contains("没钱")
                                        || planNote.contains("不报名")
                                        || empName.contains("孟瑞瑞")
                                        || empName.contains("赵美静")
                                        || empName.contains("韩晶晶")){
                                    flag = false;
                                    break;
                                    //如果不包含敏感信息，且不包括这些人，执行这个操作
                                }
    //                            System.out.println("planNote="+planNote);
//                            }
                        }
                    }else{
                        flag = false;
                    }
                    //如果flag=true,即领取
                    if (flag){
                        //点击领取
                        String res_linqu = BaseUtils.getByForm(url_linqu+aid,token);
                        System.out.println("res_linqu::"+res_linqu);
//                        System.out.println("已领取studentId->"+studentId);

                        System.out.println("res_findMinRenTang::"+res_findMinRenTang);
                        System.out.println("res_pageData_find::"+res_pageData_find);
//                        break head;
                        if (JSONObject.parseObject(res_linqu).getString("errCode").contains("-1")){
                            System.out.println("已达到上限30条！");
                            break head;
                        }else {
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
