package allin.prometheus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Demo {
    static {
        System.setProperty("fileName", "allin/allin.log");
    }
    public static Logger log = LoggerFactory.getLogger(Demo.class);
    public static void main(String[] args) {
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


        //查询
//        BigDecimal bigDecimal = new BigDecimal("256379616033271808");
//        MongoCollection<Document> service_collection = mds.getCollection("service");
//        FindIterable<Document> metric_document = metric_collection.find(Filters.in("name","redis_rdb_last_bgsave_duration_sec"));
//        FindIterable<Document> metric_document = service_collection.find(Filters.eq("serviceId",Long.valueOf(bigDecimal.toString())));
//        FindIterable<Document> metric_document = service_collection.find(Filters.in("serviceId",Long.valueOf(bigDecimal.toString())));
//        FindIterable<Document> metric_document = service_collection.find(Filters.nin("serviceId",Long.valueOf(bigDecimal.toString())));
//        Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString()), Filters.eq("serviceId", Long.valueOf("256379616033271809")))
//        FindIterable<Document> metric_document = service_collection.find(Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString())),Filters.eq("serviceId", Long.valueOf("256379616033271809"))));
//        FindIterable<Document> metric_document = service_collection.find(Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString())),Filters.eq("serviceId", Long.valueOf("256379616033271809"))));
//        for (Document d:metric_document){
//            System.out.println(d.toJson());
//        }

        //插入数据
//        MongoCollection<Document> service_collection = mds.getCollection("service");
//        for (int i = 592; i < 1092; i++) {
//            Document document = new Document();
//            document.put("serviceId",Long.valueOf("2581959823767552"+i));
//            document.put("englishServiceName","app"+(i-5));
//            document.put("_class","com.allintechinc.mds.metric.entity.Service");
//            document.put("chineseServiceName","test服务测试"+(i-15));
//            document.put("serviceType","KUBERNETES");
//
//            service_collection.insertOne(document);
//            System.out.println("添加中>>>"+i);
//        }

        MongoCollection<Document> service_collection = mds.getCollection("metric");
        for (int i = 811; i < 1011; i++) {
            Document document = new Document();
            document.put("serviceId",Long.valueOf("2581959823767552999"));
            document.put("metricId",Long.valueOf("2583086252908359"+i));
            document.put("name","upnew"+i);
            document.put("chineseMetricName","服务健康new"+i);
            document.put("chineseServiceName","test服务测试984");
            document.put("englishServiceName","app994");
            document.put("serviceType","KUBERNETES");
            document.put("_class","com.allintechinc.mds.metric.entity.Service");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("service_type", Arrays.asList("KUBERNETES"));
            jsonObject.put("instance", Arrays.asList("10.70.0.126:8108"));
            jsonObject.put("metric_name", Arrays.asList("服务健康new"+i));
            jsonObject.put("__name__", Arrays.asList("upnew"+i));
            jsonObject.put("chinese_service_name", Arrays.asList("新建指标new"+i));
            jsonObject.put("exported_job", Arrays.asList("app994"));
            jsonObject.put("english_service_name", Arrays.asList("app994"));
            jsonObject.put("source", Arrays.asList("promxy"));
            jsonObject.put("job", Arrays.asList("testcustomizeTestnew"+i));

            document.put("property",jsonObject);

            service_collection.insertOne(document);
            System.out.println("添加中>>>"+i);
        }


    }
}
