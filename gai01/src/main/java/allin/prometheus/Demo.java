package allin.prometheus;

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

        BigDecimal bigDecimal = new BigDecimal("256379616033271808");
        MongoCollection<Document> service_collection = mds.getCollection("service");
//        FindIterable<Document> metric_document = metric_collection.find(Filters.in("name","redis_rdb_last_bgsave_duration_sec"));
//        FindIterable<Document> metric_document = service_collection.find(Filters.eq("serviceId",Long.valueOf(bigDecimal.toString())));
//        FindIterable<Document> metric_document = service_collection.find(Filters.in("serviceId",Long.valueOf(bigDecimal.toString())));
//        FindIterable<Document> metric_document = service_collection.find(Filters.nin("serviceId",Long.valueOf(bigDecimal.toString())));
//        Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString()), Filters.eq("serviceId", Long.valueOf("256379616033271809")))
//        FindIterable<Document> metric_document = service_collection.find(Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString())),Filters.eq("serviceId", Long.valueOf("256379616033271809"))));
        FindIterable<Document> metric_document = service_collection.find(Filters.or(Filters.eq("serviceId", Long.valueOf(bigDecimal.toString())),Filters.eq("serviceId", Long.valueOf("256379616033271809"))));
        for (Document d:metric_document){
            System.out.println(d.toJson());
        }


        Calendar cal = Calendar.getInstance();
        cal.get(Calendar.DATE);
    }
}
