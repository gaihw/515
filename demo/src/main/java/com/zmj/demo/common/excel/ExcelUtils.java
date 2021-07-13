package com.zmj.demo.common.excel;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.JsonResult;
import com.zmj.demo.domain.auto.CaseExcelChain;
import com.zmj.demo.enums.MessageEnum;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


import static org.apache.poi.ss.usermodel.CellType.*;


@Configuration
@Slf4j
public class ExcelUtils {

    /**
     * 功能名：文件下载
     * @param res
     */
    public void downloadExcel(HttpServletResponse res){

        // 配置文件下载
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");

        String downloadFileName = null;
        try {
            downloadFileName = new String("用例模板下载.xlsx".getBytes("UTF-8"),"iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String headStr ="attachment; filename=\"" + downloadFileName +"\"";
        // 下载文件能正常显示中文
        res.setHeader("Content-Disposition", headStr);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            File file= ResourceUtils.getFile("template\\data.xlsx");
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch ( IOException e ) {
            log.error("download exception\t"+e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("inputsteam close exception\t"+e);
                }
            }
        }
        log.info("Download completed successfully");
    }

    /**
     * 功能：导入
     */
    public List<CaseExcelChain> uploadExcel(MultipartFile excelFile,String creator) {
        log.info("---------开始上传用例---------");
        // contentType
        String contentType = excelFile.getContentType();
        // excel文件名
        String fileName = excelFile.getOriginalFilename();
        //上传文件数据存入excel_data列表中
        List excel_data = new ArrayList();
        if (excelFile.isEmpty()) {
            return excel_data;
        }
        // 根据不同excel创建不同对象,Excel2003版本-->HSSFWorkbook,Excel2007版本-->XSSFWorkbook
        Workbook wb = null;
        InputStream im = null;
        try {
            im = excelFile.getInputStream();
            wb = WorkbookFactory.create(im);
            // 根据页面index 获取sheet页
            Sheet sheet = wb.getSheetAt(0);
            Row row = null;
            // 循环sheet页中数据从第x行开始,例:第3行开始为导入数据
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 获取每一行数据
                row = sheet.getRow(i);
                // 输出表格内容,此处可替换为数据插入操作
                CaseExcelChain caseExcelChain = new CaseExcelChain();

                if (null != row.getCell(0) && "" != row.getCell(0).toString()){
                    row.getCell(0).setCellType(NUMERIC);
                    caseExcelChain.setInterfaceManageID((int) row.getCell(0).getNumericCellValue());
                }else{
                    caseExcelChain.setInterfaceManageID(200000000);
                }

                if (null != row.getCell(1) && "" != row.getCell(1).toString()){
                    row.getCell(1).setCellType(STRING);
                    caseExcelChain.setCaseName(row.getCell(1).getStringCellValue());
                }else{
                    caseExcelChain.setCaseName("无");
                }

                if (null != row.getCell(2) && "" != row.getCell(2).toString()){
                    row.getCell(2).setCellType(STRING);
                    caseExcelChain.setHeaderData(row.getCell(2).getStringCellValue());
                }else{
                    caseExcelChain.setHeaderData("无");
                }

                if (null != row.getCell(3) && "" != row.getCell(3).toString()){
                    row.getCell(3).setCellType(STRING);
                    caseExcelChain.setParamData(row.getCell(3).getStringCellValue());
                }else{
                    caseExcelChain.setParamData("无");
                }
                if (null != row.getCell(4) && "" != row.getCell(4).toString()){
                    row.getCell(4).setCellType(STRING);
                    caseExcelChain.setAssertType(row.getCell(4).getStringCellValue());
                }else{
                    caseExcelChain.setAssertType("无");
                }
                if (null != row.getCell(5) && "" != row.getCell(5).toString()){
                    row.getCell(5).setCellType(STRING);
                    caseExcelChain.setAssertData(row.getCell(5).getStringCellValue());
                }else{
                    caseExcelChain.setAssertData("无");
                }

                if (null != row.getCell(6) && "" != row.getCell(6).toString()){
                    row.getCell(6).setCellType(STRING);
                    caseExcelChain.setState(row.getCell(6).getStringCellValue());
                }else{
                    caseExcelChain.setState("无");
                }

                caseExcelChain.setCreator(creator);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                Date date = new Date();
                caseExcelChain.setCreateDate(simpleDateFormat.format(date));
                caseExcelChain.setUpdateDate(simpleDateFormat.format(date));

                excel_data.add(caseExcelChain);
            }

            log.info("---------结束上传用例---------");
            return excel_data;
        } catch (Exception e1) {
            // 回滚数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e1.printStackTrace();
        } finally {
            try {
                im.close();
                wb.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return excel_data;
    }
}
