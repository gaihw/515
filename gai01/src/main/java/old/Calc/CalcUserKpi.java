package old.Calc;

import java.io.*;
import java.sql.*;

/**
 * tb_bdr_user_kpi指标计算
 */
class CalcUserKpi {
        public static void main(String[] args) throws IOException {
            FileWriter fw = new FileWriter(new File("data/sqlResult_userKpi"));
            Connection conn = null;
            Statement stmt = null;
            String[] columName = {"sum_profit","max_profit","sum_profit_profit","sum_profit_loss"
                    ,"sum_fill_size_profit","sum_fill_size_loss","sum_fee","sum_gaopin"
                    ,"sum_position_time","sum_count","sum_position_time_profit","sum_position_time_loss"
                    ,"sum_potential_profit","sum_potential_loss"};
            String[] business_id = {"future","mix","regular","swaps"};
            String userId = "333387939";
            try{
                // 注册 JDBC 驱动
                Class.forName(Config.JDBC_DRIVER);
                // 打开链接
                conn = DriverManager.getConnection(Config.DB_URL_BIGDATA,Config.USER_36,Config.PASS_36);
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/sqlData_userKpi")),
                            "UTF-8"));
                    String lineTxt = null;
                    int i = 0 ;
                    int j = 0 ;
                    while ((lineTxt = br.readLine()) != null) {
                        if (lineTxt.contains("detail")){
                            // 执行查询
                            stmt = conn.createStatement();
                            String sql = lineTxt + userId;
                            ResultSet rs = stmt.executeQuery(sql);
                            // 展开结果集数据库
                            while(rs.next()){
                                System.out.println(business_id[j]+"_"+columName[i]+"="+rs.getBigDecimal(columName[i]));
                                try {
                                    fw.write(business_id[j]+"_"+columName[i]+"="+(rs.getBigDecimal(columName[i])==null?0:rs.getBigDecimal(columName[i]))+"\r\n");
                                    fw.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } }
                            rs.close();
                            i ++ ;
                        }else if (lineTxt.contains("####################")){
                            i = 0 ;
                            j ++ ;
                            fw.write("#######################################\r\n");
                            fw.flush();
                        }
                    }
                    br.close();
                    fw.close();
                } catch (Exception e) {
                    System.err.println("read errors :" + e);
                }
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
            }finally{
                // 关闭资源
                try{
                    if(stmt!=null) stmt.close();
                }catch(SQLException se2){
                }// 什么都不做
                try{
                    if(conn!=null) conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
            System.out.println("over...");
    }
}
