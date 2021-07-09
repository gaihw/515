package com.zmj.demo.common.excel;

import net.sf.jxls.transformer.XLSTransformer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExcelUtils {

    /**
     * 文件下载
     * @param response
     */
    public void downloadExcel(HttpServletResponse response){
        //获得模版
        File directory = new File("template");
        String templatePath = null;
        try {
            templatePath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String templateFile = templatePath + "\\data.xlsx";

        //导出文件名
//        SimpleDateFormat simpl = new SimpleDateFormat("yyyyMMddHHmmss");
//        String currntTime = simpl.format(new Date());
        String fileName = "测试用例模板";

        //将模板数据源
        Map beans = new HashMap();

        //生成的导出文件
        File destFile = null;
        //文件名称统一编码格式
        try {
            fileName = URLEncoder.encode(fileName, "utf-8");
            destFile = File.createTempFile(fileName, ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //transformer转到Excel
        XLSTransformer transformer = new XLSTransformer();

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //将数据添加到模版中生成新的文件
            //beans参数，可传空的map，因为模板文件中，有样例数据，不用新建数据源
            transformer.transformXLS(templateFile, beans, destFile.getAbsolutePath());
            //将文件输入
            InputStream inputStream = new FileInputStream(destFile);
            //设置response参数，可以打开下载页面
            response.reset();
            //设置响应文本格式
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "utf-8"));
            //将文件输出到页面
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            //读取并写入
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //使用完成后关闭流
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
