package com.zmj.controller.eth;


import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eth.EthChain;
import com.zmj.domain.JsonResult;
import com.zmj.service.EthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class EthController {

    @Autowired
    private EthService ethService;

    @RequestMapping(value= "/eth/getAddPrivPub", method = RequestMethod.POST)
    public JsonResult getPublicKeys(@RequestBody EthChain ethChain){
        JSONObject res = ethService.getAddPrivPub(ethChain);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/eth/getBlockNum", method = RequestMethod.POST)
    public JsonResult getBlockNum(@RequestBody EthChain ethChain){
        BigInteger res = ethService.getBlockNumber(ethChain);
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value= "/eth/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody EthChain ethChain){
        String res = ethService.transaction(ethChain);
        log.info("{}",res);
        return new JsonResult(res);
    }

    /**
     * 文件上传测试接口
     * @return
     */
    @RequestMapping(value = "/eth/upload")
    public JsonResult uploadFileTest(@RequestParam("file") MultipartFile uploadFile) {
        String res = ethService.uploadFile(uploadFile);
        log.info("{}",res);
        return new JsonResult(0,"success");
    }

    @RequestMapping(value = "/eth/getFileList",method = RequestMethod.GET)
    public JsonResult getFileList(){
        List res = ethService.getFileList();
        log.info("{}",res);
        return new JsonResult(res);
    }

    @RequestMapping(value = "/eth/getPubPri",method = RequestMethod.GET)
    public JsonResult getPubPri(@RequestParam(value = "password") String  password,
                                @RequestParam(value = "fileName",required=true) String  fileName){
        try{
            JSONObject res = ethService.getPubPri(password,fileName);
            log.info("{}",res);
            return new JsonResult(res);
        }catch (Exception e){
            return new JsonResult(202,"解析失败");
        }
    }

    @RequestMapping(value = "/eth/getrawtransaction",method = RequestMethod.GET)
    public JsonResult getrawtransaction(@RequestParam(value = "txid") String txid){
        String res = ethService.getrawtransaction(txid);
        JSONObject jsonObject = JSONObject.parseObject(res).getJSONObject("result");
        jsonObject.put("blockNumber",Integer.parseInt(jsonObject.getString("blockNumber").substring(2),16));
        jsonObject.put("from",jsonObject.getString("from"));
        jsonObject.put("to",jsonObject.getString("to"));
        jsonObject.put("value", jsonObject.getString("value"));
        jsonObject.put("nonce",Integer.parseInt(jsonObject.getString("nonce").substring(2),16));
        jsonObject.put("blockHash",jsonObject.getString("blockHash"));

        log.info("{}",res);
        return new JsonResult(jsonObject);
    }
}
