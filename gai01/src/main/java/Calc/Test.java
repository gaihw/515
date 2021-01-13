package Calc;

import blockchain.Utils;
import blockchain.btcd.Zec;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    static {
        System.setProperty("fileName", "zec/zec.log");
    }
    public static Logger log = LoggerFactory.getLogger(Zec.class);
    public static void main(String[] args) throws IOException {
//        String url = "http://192.168.200.51:10320/risk/tbex-risk-engine-config/orderRecord/searchAllRecord";
//        String params = "{\"pageIndex\":1,\"pageSize\":100,\"filters\":{\"userId\":\"333385034\",\"currencys\":\"1001,1002\",\"businessId\":2,\"strTime\":\"\",\"endTime\":\"\"}}";
//        String au = "Bearer 396fe35d-7716-2413-48ca-a4a2-5b0d0078e02f";
////        System.out.println(BaseUtils.postByJson(url,au,params));
//        List<BigDecimal> list = new ArrayList<BigDecimal>();
//        list.add(0,new BigDecimal(0));
//        list.add(0,list.get(0).add(new BigDecimal(111)));
//        list.add(0,list.get(0).add(new BigDecimal(111)));
//        System.out.println(list.get(0));
//        System.out.println(list);

//        String fileName = "userId.txt";
//        for (int i= 1061 ; i <=23000 ; i++){
//            insertSendRecord(String.valueOf(i));
//        }


        FileReader fr = null;
        BufferedReader bufr = null;
        try {
            fr = new FileReader("data/user.txt");
            bufr = new BufferedReader(fr);
            String line = null;
            //BufferedReader提供了按行读取文本文件的方法readLine()
            //readLine()返回行有效数据，不包含换行符，未读取到数据返回null
            while((line = bufr.readLine())!=null) {
//                System.out.println(line);
                insertSendRecord(line);
            }
        }catch(IOException e) {
            System.out.println("异常：" + e.toString());
        }finally {
            try {
                if(bufr!=null)
                    bufr.close();
            }catch(IOException e) {
                System.out.println("异常：" + e.toString());
            }
        }

//        System.out.println(0.2*Math.pow(10.0,2));
//        System.out.println(DictEnum.BTC.getCurrency());
//        System.out.println(DictEnum.values().length);
//
//        for (DictEnum e : DictEnum.values()) {
//            System.out.println(e.getCurrency());
//        }

//String address = "TNQR9CgoMNqFjcsQZBYZZB7thj91jZQDcW";
//        System.out.println(stringToAscii(address));

    }
    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * 冷钱包插入地址
     * @param userId
     */
    public static void insertSendRecord(String userId){
        Connection conn = null;

        try{
            // 注册 JDBC 驱动
            Class.forName(Calc.Config.JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection("jdbc:mysql://192.168.111.84:3306/exchange", "root", "MyNewPass4!");

//            String sql="insert into tb_wallet_address_cold_trx (address,user_id,site_id) values(?,?,?)";//sql语句
            String sql="INSERT INTO `exchange`.`account`(`user_id`, `contract_id`, `currency_id`, `hold`, `balance`, `wallet_type`, `wallet_mode`, `version`, `account_status`, `create_time`, `update_time`) VALUES ( ?, 1, 8, 0, 1000000, 1, 1, 0, 0, '2018-12-04 20:50:52', '2020-11-15 14:20:49');";//sql语句
            PreparedStatement pstmt=conn.prepareStatement(sql);//获得预置对象
            pstmt.setString(1, userId);
//            pstmt.setString(2, userId);
//            pstmt.setString(3, "1");


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
}
