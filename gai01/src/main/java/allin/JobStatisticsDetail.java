package allin;

import allin.prometheus.Demo;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class JobStatisticsDetail {
    static {
        System.setProperty("fileName", "allin/job_statistics_detail.log");
    }
    public static Logger log = LoggerFactory.getLogger(JobStatisticsDetail.class);
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
        final MongoDatabase mds = mongoClient.getDatabase("data_statistics");


        MongoCollection<Document> service_collection = mds.getCollection("JOB_STATISTICS_DETAIL");

        for (int i = 10000; i < 100000; i++) {
            Document document = new Document();

            document.put("jobId",Long.valueOf("10000331"+i));
            document.put("jobType","REPOSITORY");
            document.put("processInstanceId",Long.valueOf("40783"+i));
            document.put("batchNumber","2022091316351"+i);
            document.put("createdDate",getISO8601Timestamp(Date.from(Instant.now())));
            document.put("duration",Long.valueOf("7000"));
            document.put("endTime",Long.valueOf("1631517568000"));
            document.put("id",Long.valueOf("10053886"+i));
            document.put("jobName","209510"+i);
            document.put("number",Long.valueOf("29"+i));
            document.put("operatorId",Long.valueOf("1591"));
            document.put("operatorName","盖洪伟");
            document.put("result","SUCCESS");
            document.put("source","DATA_GOVERNANCE");
            document.put("startTime",Long.valueOf("1631517561000"));
            document.put("taskId",Long.valueOf("4783"));
            document.put("timestamp",Long.valueOf("1631517585908"));
            document.put("unit","NUMBER");
            document.put("custom","{\"jobName\":\"29510\",\"processInstanceId\":\"4783\",\"processDefinedId\":\"11332\",\"operatorTag\":\"\",\"taskInstanceId\":\"6400\",\"batchId\":\"20210913151920\",\"operatorName\":\"\",\"userId\":\"1591\",\"tdlJobName\":\"ITD_29510\",\"jobId\":\"10003312\",\"tenantId\":\"1\",\"jobType\":\"REPOSITORY\",\"operatorId\":\"\"}");

            service_collection.insertOne(document);
            System.out.println("添加中>>>"+i);
        }
    }

    public static LocalDateTime getISO8601Timestamp(Date date){
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);

        LocalDateTime localDateTime = date.toInstant().atOffset(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }
}
