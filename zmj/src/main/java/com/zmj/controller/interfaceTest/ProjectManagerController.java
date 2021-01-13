package com.zmj.controller.interfaceTest;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swetake.util.Qrcode;
import com.zmj.domain.interfaceTest.ProjectApiChain;
import com.zmj.domain.interfaceTest.ProjectCaseChain;
import com.zmj.domain.interfaceTest.ProjectChain;
import com.zmj.domain.JsonResult;
import com.zmj.service.ProjectManagementService;
import com.zmj.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class ProjectManagerController {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Autowired
    private RedisService redisService;


    /**
     * 添加项目
     * @param httpServletRequest
     * @param projectChain
     * @return
     */
    @RequestMapping(value = "/project/platform/add",method = RequestMethod.POST)
    public JsonResult add_test(HttpServletRequest httpServletRequest, @RequestBody ProjectChain projectChain){
        System.out.println(projectChain);
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.addNewProject(projectChain,name);
            return new JsonResult(0,"添加成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"添加失败，请查看是否重复添加！");
        }
    }

    @RequestMapping(value = "/project/platform/list",method = RequestMethod.POST)
    public JsonResult list(@RequestBody JSONObject jsonObject){
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = projectManagementService.acount(jsonObject);
        PageInfo<List<ProjectChain>> pageInfo = new PageInfo(projectManagementService.list(jsonObject));
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }

    @RequestMapping(value = "/project/platform/delete",method = RequestMethod.POST)
    public JsonResult delete(@RequestBody JSONObject jsonObject){
        int flag = projectManagementService.deleteData(jsonObject.getInteger("id"));
        if (flag > 0){
            return new JsonResult(0,"删除成功");
        }else{
            return new JsonResult(101,"操作失败，请重试！");
        }
    }

    @RequestMapping(value = "/project/platform/edit" ,method = RequestMethod.POST)
    public JsonResult edit(HttpServletRequest httpServletRequest, @RequestBody ProjectChain projectChain){
        System.out.println(projectChain);
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.edit(projectChain,name);
            return new JsonResult(0,"修改成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"修改失败，请重新添加！");
        }
    }


    /**
     * 添加接口
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/project/api/list",method = RequestMethod.POST)
    public JsonResult apiList(@RequestBody JSONObject jsonObject){
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = projectManagementService.apiAcount(jsonObject);
        PageInfo<List<ProjectApiChain>> pageInfo = new PageInfo(projectManagementService.apiList(jsonObject));
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }

    @RequestMapping(value = "/project/api/list/platform",method = RequestMethod.GET)
    public JsonResult apiListPlatform(){
        return new JsonResult(projectManagementService.apiListPlatform());
    }

    @RequestMapping(value = "/project/api/list/project",method = RequestMethod.GET)
    public JsonResult apiListProject(@RequestParam(value = "platform") String platform){
        return new JsonResult(projectManagementService.apiListProject(platform));
    }

    @RequestMapping(value = "/project/api/list/module",method = RequestMethod.GET)
    public JsonResult apiListModule(@RequestParam(value = "platform") String platform,
                                    @RequestParam(value = "project") String project){
        return new JsonResult(projectManagementService.apiListModule(platform,project));
    }
    @RequestMapping(value = "/project/api/add",method = RequestMethod.POST)
    public JsonResult apiAdd(HttpServletRequest httpServletRequest,@RequestBody ProjectApiChain projectApiChain){
        System.out.println(projectApiChain);
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.addNewApi(projectApiChain,name);
            return new JsonResult(0,"添加成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"添加失败，请查看是否重复添加！");
        }
    }

    @RequestMapping(value = "/project/api/delete",method = RequestMethod.POST)
    public JsonResult deleteApi(@RequestBody JSONObject jsonObject){
        int flag = projectManagementService.deleteApiData(jsonObject.getInteger("id"));
        if (flag > 0){
            return new JsonResult(0,"删除成功");
        }else{
            return new JsonResult(101,"操作失败，请重试！");
        }
    }

    @RequestMapping(value = "/project/api/edit" ,method = RequestMethod.POST)
    public JsonResult edit(HttpServletRequest httpServletRequest, @RequestBody ProjectApiChain projectApiChain){
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.editApi(projectApiChain,name);
            return new JsonResult(0,"修改成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"修改失败，请重新添加！");
        }
    }


    /**
     * 添加case
     */
    @RequestMapping(value = "/project/case/list",method = RequestMethod.POST)
    public JsonResult caseList(@RequestBody JSONObject jsonObject){
        Integer page = jsonObject.getInteger("page");
        Integer limit = jsonObject.getInteger("limit");
        PageHelper.startPage(page, limit);
        //获取总条数
        int count = projectManagementService.caseAcount(jsonObject);
        PageInfo<List<ProjectApiChain>> pageInfo = new PageInfo(projectManagementService.caseList(jsonObject));
        log.info("{}",pageInfo.getList());
        return new JsonResult(pageInfo.getList(),"success",count);
    }

    @RequestMapping(value = "/project/case/list/name",method = RequestMethod.GET)
    public JsonResult caseListName(@RequestParam(value = "platform") String platform,
                                    @RequestParam(value = "project") String project,
                                   @RequestParam(value = "module") String module){
        return new JsonResult(projectManagementService.caseListName(platform,project,module));
    }

    @RequestMapping(value = "/project/case/add",method = RequestMethod.POST)
    public JsonResult caseAdd(HttpServletRequest httpServletRequest,@RequestBody ProjectCaseChain projectCaseChain){
        System.out.println(projectCaseChain);
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.addNewCase(projectCaseChain,name);
            return new JsonResult(0,"添加成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"添加失败，请查看是否重复添加！");
        }
    }

    @RequestMapping(value = "/project/case/delete",method = RequestMethod.POST)
    public JsonResult deleteCase(@RequestBody JSONObject jsonObject){
        int flag = projectManagementService.deleteCaseData(jsonObject.getInteger("id"));
        if (flag > 0){
            return new JsonResult(0,"删除成功");
        }else{
            return new JsonResult(101,"操作失败，请重试！");
        }
    }

    @RequestMapping(value = "/project/case/edit" ,method = RequestMethod.POST)
    public JsonResult editCase(HttpServletRequest httpServletRequest, @RequestBody ProjectCaseChain projectCaseChain){
        try {
            String name = (String) redisService.getValue(httpServletRequest.getHeader("token"));
            projectManagementService.editCase(projectCaseChain,name);
            return new JsonResult(0,"修改成功");
        }catch (Exception e){
            System.out.println(e);
            return new JsonResult(101,"修改失败，请重新添加！");
        }
    }

    @RequestMapping(value = "/project/case/info",method = RequestMethod.GET)
    public JsonResult caseInfo(@RequestParam(value = "id") int id){
        System.out.println("id:{}"+id);
        List<ProjectCaseChain> list = projectManagementService.caseInfo(id);
        System.out.println(list);
        try{
            return new JsonResult(0,list);
        }catch (Exception e){
            return new JsonResult(101,"获取失败");
        }
    }

    @RequestMapping(value = "/img",method = RequestMethod.GET)
    public void img(@RequestParam(value = "content") String  content,
                          @RequestParam(value = "imgPath") String  imgPath){
        int width=140;
        int height=140;
        //实例化Qrcode
        Qrcode qrcode=new Qrcode();
        //设置二维码的排错率L(7%) M(15%) Q(25%) H(35%)
        qrcode.setQrcodeErrorCorrect('M');
        qrcode.setQrcodeEncodeMode('B');
        //设置二维码尺寸(1~49)
        qrcode.setQrcodeVersion(7);
        //设置图片尺寸
        BufferedImage bufImg=new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //绘制二维码图片
        Graphics2D gs=bufImg.createGraphics();
        //设置二维码背景颜色
        gs.setBackground(Color.WHITE);
        //创建一个矩形区域
        gs.clearRect(0, 0, width, height);
        //设置二维码的图片颜色值 黑色
        gs.setColor(Color.BLACK);

        //获取内容的字节数组,设置编码集
        try {
            byte[] contentBytes=content.getBytes("utf-8");
            int pixoff=2;
            //输出二维码
            if(contentBytes.length>0&&contentBytes.length<120){
                boolean[][] codeOut=qrcode.calQrcode(contentBytes);
                for(int i=0;i<codeOut.length;i++){
                    for(int j=0;j<codeOut.length;j++){
                        if(codeOut[j][i]){
                            gs.fillRect(j*3+pixoff, i*3+pixoff, 3, 3);
                        }
                    }
                }
            }
            gs.dispose();
            bufImg.flush();
            //生成二维码图片
            File imgFile=new File(imgPath);
            ImageIO.write(bufImg, "png", imgFile);

            System.out.println("二维码生成成功！");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
