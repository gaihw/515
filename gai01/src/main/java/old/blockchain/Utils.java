package old.blockchain;

import old.Calc.Config;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class Utils {
    static {
        System.setProperty("fileName", "log.txt");
    }
    public static Logger log = LoggerFactory.getLogger(Utils.class);
    public static HttpClient client = new HttpClient();
    public static PostMethod post ;
    public static GetMethod get;

//    public static void main(String[] args) {
////        0.76778900-0.767
////        String []cmds = {"curl",
////                "-u",
////                "btctest:btctest",
////                "--data-binary",
////                "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"sendtoaddress\", \"params\": [\"mkna57VJfnSpbkRL9u6nqRsbXH8CFJ4Dnw\", 0.767, \"donation\", \"seans outpost\"]}",
////                "-H",
////                "content-type: text/plain;",
////                "http://192.168.112.214:18443",
////                "-s"};
//        String []cmds = {"curl",
//                "-u",
//                "btctest:btctest",
//                "--data-binary",
//                "{\"jsonrpc\": \"1.0\", \"id\": \"curltest\", \"method\": \"getbalance\", \"params\": []}",
//                "-H",
//                "content-type: text/plain;",
//                "http://192.168.112.214:9332",
//                "-s"};
//        ProcessBuilder pb=new ProcessBuilder(cmds);
//        pb.redirectErrorStream(true);
//        Process p;
//        try {
//            p = pb.start();
//            BufferedReader br=null;
//            String line=null;
//
//            br=new BufferedReader(new InputStreamReader(p.getInputStream()));
//            while((line=br.readLine())!=null){
//                System.out.println("\t"+line);
////                JSONObject j = JSONObject.parseObject(line);
////                System.out.println(j.getJSONObject("error").getString("message"));
//            }
//            br.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     * get方法
     * @param url
     * @return
     */
    public static String get(String url){
        get = new GetMethod(url) ;
        try {
            int code = client.executeMethod(get);
            InputStream respone = get.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送json格式的数据
     * @param url
     * @param params
     * @return
     */
    public static String postByJson(String url, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","application/json");
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发送带token的json数据
     * @param url
     * @param authorization
     * @param params
     * @return
     */
    public static String postByJson(String url,String authorization, String params){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(params,"application/json" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","application/json");
        post.setRequestHeader("Authorization","Basic "+authorization);
        try {
//            System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
            int code = client.executeMethod(post);
//            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
            InputStream respone = post.getResponseBodyAsStream();
//            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发送json格式的数据
     * @param url
     * @return
     */
    public static String postByJson(String url){
        post = new PostMethod(url) ;
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url
     * @return
     */
    public static String postByText(String url,String text){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(text,"text/plain" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","text/plain");
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }
            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String postByText(String url,String creb,String text){
        post = new PostMethod(url) ;
        RequestEntity se = null;
        try {
            se = new StringRequestEntity(text,"text/plain" ,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setRequestEntity(se);
        post.setRequestHeader("Content-Type","text/plain");
        post.setRequestHeader("Authorization","Basic "+creb);
        try {
            int code = client.executeMethod(post);
            InputStream respone = post.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(respone));
            String tempbf;
            StringBuffer re=new StringBuffer(100);
            while((tempbf=br.readLine())!=null){
                re.append(tempbf);
            }

            return re.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void writerData(String fileName,String text) throws IOException {
        File file = new File("data/btc/"+fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text+"\r\n");
        bw.write("----------------------\r\n");
        bw.flush();
        bw.close();
        fw.close();
    }
    public static void writerData1(String fileName,String text) throws IOException {
        File file = new File("/Users/gaihongwei/Desktop/swap/"+fileName);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text+"\r\n");
        bw.flush();
        bw.close();
        fw.close();
    }

    public static void insertSendRecord(int withdraw_id, String address, BigDecimal amount, String target_address){
        Connection conn = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(old.Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(Config.DB_URL_58WALLET, old.Calc.Config.USER_36, old.Calc.Config.PASS_36);

            String sql="insert into tb_wallet_send_record (withdraw_id,address,currency_id,amount,site_id,fee,actual_fee,multi_sig,status,target_address) values(?,?,?,?,?,?,?,?,?,?)";//sql语句
            PreparedStatement pstmt=conn.prepareStatement(sql);//获得预置对象
            pstmt.setInt(1, withdraw_id);//设置占位符的值
            pstmt.setString(2, address);
            pstmt.setString(3, "201");
            pstmt.setBigDecimal(4, amount);
            pstmt.setInt(5,1);
            pstmt.setBigDecimal(6,BigDecimal.valueOf(0.000));
            pstmt.setBigDecimal(7,BigDecimal.valueOf(0.000));
            pstmt.setInt(8,0);
            pstmt.setInt(9,0);
            pstmt.setString(10,target_address);


            int res=pstmt.executeUpdate();//执行sql语句
            if(res>0){
                System.out.println("数据录入成功");
            }
            pstmt.close();//关闭资源
            conn.close();//关闭资源

        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        int withdraw_id = 1234570202;
//        for (int i = 0; i < 5; i++) {
//            BigDecimal r = BigDecimal.valueOf(Math.random()*100).setScale(8, RoundingMode.HALF_UP);
//            BigDecimal amount = BigDecimal.valueOf(Math.random()).setScale(8, RoundingMode.HALF_UP);
//            if (r.compareTo(BigDecimal.valueOf(25))<0){
//                insertSendRecord(withdraw_id+i,"tmUfe1Soe5UcSbuGWQNs9jJ2TfAgJ5ThDtg",amount,"DM81VtMjpFMXMxaELnFe0lPlDMOeR4R6N7QokD2DuZffjlfXrEOgXNHJ2gJKBo/m");
//            }else if (r.compareTo(BigDecimal.valueOf(25))>=0&&r.compareTo(BigDecimal.valueOf(50))<0){
//                insertSendRecord(withdraw_id+i,"tmRz2Q4zSuwaXHaG4xJZ1STNqSFua2A8Syo",amount,"rf1EXYwpJ/I40X3UCMMnfFZCp2xI3hCnVsTR4JqoEaFlZnq6zJGjPg1BlywMtC9A");
//            }else if (r.compareTo(BigDecimal.valueOf(50))>=0&&r.compareTo(BigDecimal.valueOf(75))<0){
//                insertSendRecord(withdraw_id+i,"tmNPzf6NhPvYmKVgMMArD9xTSBRScPQVF44",amount,"wND1wWGPsv01Je0Nyj021E/qh7A747anpbffTn6oYoTGQTelebIiNWSGlDGU53LJ");
//            }else{
//                insertSendRecord(withdraw_id+i,"tmEc4LaUWGTExaCaYQ6vtbQDqUEJsQ4pUhH",amount,"ElbWU4l04D3E81ddMCVXnyTSgu4TitUlo5GAt2cRhTXVBf+ZZExUUX63Db8ZN3xj");
//            }
//        }
    }


}
