package allin.prometheus;

import allin.BaseUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.*;

public class Test {
    static {
        System.setProperty("fileName", "allin/allin.log");
    }
    public static Logger log = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) throws UnknownHostException {

        //配置linux中mongoDB的端口和地址
        ServerAddress serverAddress = new ServerAddress("mongo-test-01.allintechinc.com", 27017);

        ArrayList<ServerAddress> addres = new ArrayList<>();

        //在地址链表中添加地址和端口信息
        addres.add(serverAddress);

        //配置与mongoDB的验证
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential("admin", "admin", "allin_db&4%".toCharArray());

        List<MongoCredential> credentials = new ArrayList<>();
        //将验证配置加入验证的链表中
        credentials.add(mongoCredential);

        //创建连接的mongoClient客户端
        MongoClient mongoClient = new MongoClient(addres, credentials);

        //进行连接
        final MongoDatabase mds = mongoClient.getDatabase("mds");

        MongoCollection<Document> metric_collection = mds.getCollection("metric");
        FindIterable<Document> metric_document = metric_collection.find();

        MongoCollection<Document> service_collection = mds.getCollection("service");
        FindIterable<Document> service_document = service_collection.find();
        Map<BigDecimal,String> service_map = new HashMap();
        for (Document d:service_document ){
            BigDecimal number = JSONObject.parseObject(d.toJson().toString()).getJSONObject("serviceId").getBigDecimal("$numberLong");
            String englishServiceName = JSONObject.parseObject(d.toJson().toString()).getString("englishServiceName");
            service_map.put(number,englishServiceName);
        }

        BigDecimal metricID = new BigDecimal(0);
        BigDecimal serviceID = new BigDecimal(0);
        String name = "";
        String chineseMetricName="";
        String englishServiceName = "";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyVXVpZCI6IjVkYzFlMWEzLTQ1NjEtNGI3MC1iOTM3LTYyMDc3NmQ1YzVkYiIsImV4cCI6MTYyNzUyNjY2NCwidXNlcklkIjoiMTU5MSIsInRpbWVzdGFtcCI6IjE2Mjc0NDAyNjQ1MzUifQ.bQpmqFr-XjScbDEuJYRhYeibehnqj0-puqTiMVg5U7w";
        String uid = "5dc1e1a3-4561-4b70-b937-620776d5c5db";
        long startIme = 1627446360;
        long endTime = 1627446960;

        String prometheus_url = "http://prometheus-test.allintechinc.com/api/v1/query_range";
        String metric_search_url = "http://mds-test.allintechinc.com/mds/api/v1/service/metric/search";

        Set set = new TreeSet();

        for (Document metric:metric_document){
            JSONObject jsonObject = JSONObject.parseObject(metric.toJson().toString());
            metricID = jsonObject.getJSONObject("metricId").getBigDecimal("$numberLong");
            serviceID = jsonObject.getJSONObject("serviceId").getBigDecimal("$numberLong");
            name = jsonObject.getString("name");
            chineseMetricName = jsonObject.getString("chineseMetricName");


//            FindIterable<Document> service_document1 = service_collection.find(Filters.in("serviceId",Long.valueOf(String.valueOf(serviceID))));
//            for (Document d:service_document1){
//                System.out.println(d.toJson());
//            }

            englishServiceName = service_map.get(serviceID);

            String metric_search_params = "{\"pageSize\":10,\"pageNo\":1,\"filter\":{\"searchType\":\"METRIC_RANGE\",\"metricId\":[\""+metricID+"\"],\"paramMap\":{\""+metricID+"\":{}},\"startTime\":"+startIme+",\"endTime\":"+endTime+",\"step\":\"1m\"}}";
            String metric_search_res = BaseUtils.postByJson(metric_search_url,token,uid,metric_search_params);


            String prometheus_res = BaseUtils.getByForm(prometheus_url+"?query="+name+"&start="+startIme+"&end="+endTime+"&step=60s");
            JSONArray prometheus_result = JSONObject.parseObject(prometheus_res).getJSONObject("data").getJSONArray("result");

            for (int i = 0; i < prometheus_result.size(); i++) {
                if (prometheus_result.getJSONObject(i).getJSONObject("metric").containsKey("english_service_name")) {

                    if (prometheus_result.getJSONObject(i).getJSONObject("metric").getString("english_service_name").equals(englishServiceName)
                            && prometheus_result.getJSONObject(i).getJSONObject("metric").getString("__name__").equals(name)) {
                        for (int j = 0; j < prometheus_result.getJSONObject(i).getJSONArray("values").size(); j++) {

                            BigDecimal prome_value = prometheus_result.getJSONObject(i).getJSONArray("values").getJSONArray(j).getBigDecimal(1);
                            BigDecimal mds_vlues = JSONObject.parseObject(metric_search_res).getJSONArray("data").getJSONObject(0).getJSONArray("value").getJSONObject(0).getJSONArray("value").getJSONArray(j).getBigDecimal(1);

//                            System.out.println("prometheus=" + prome_value + "::::mds=" + mds_vlues);
                            if (prome_value.compareTo(mds_vlues)!=0){
                                set.add(metricID+":::::"+name);
                                System.out.println(metricID+":::::"+name);
                                System.out.println("prometheus=" + prome_value + "::::mds=" + mds_vlues);
                                log.info(metricID+":::::"+name);
                                log.info("prometheus=" + prome_value + "::::mds=" + mds_vlues);
                            }
                        }

                    } else {
                        continue;
                    }
                }
            }

        }
        System.out.println(set);

    }
}
