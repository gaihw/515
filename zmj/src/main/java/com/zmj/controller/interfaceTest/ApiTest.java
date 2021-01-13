package com.zmj.controller.interfaceTest;


import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.btcd.BtcChain;
import com.zmj.domain.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiTest {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * post application/x-www-form-urlencoded,无token
     * @param currencyId
     * @param userId
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/allocate/address",method = RequestMethod.POST)
    public JsonResult address(@RequestParam("currencyId") String currencyId,
                              @RequestParam("userId") String userId,
                              @RequestParam("siteId") String siteId){
        String url = "http://localhost:19111/test";
        System.out.println(currencyId+userId+siteId);

        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("currencyId", currencyId);
        postParameters.add("userId", userId);
        postParameters.add("siteId", siteId);

//        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(type);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> r = new HttpEntity<>(postParameters, headers);

        ResponseEntity<String> data= restTemplate.postForEntity(url, r, String.class);
        return new JsonResult(data);
//        return new JsonResult("success");
//        ResponseEntity<Object> responsebody = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                r,
//                Object.class);
//        return new JsonResult(responsebody.getBody());
    }

    /**
     * post 无参数
     * @return
     */
    @RequestMapping(value = "/one",method = RequestMethod.POST)
    public JsonResult one(){
        String url = "http://localhost:19111/one";
        Object res = restTemplate.postForObject(url,null,Object.class);
        return new JsonResult(0,res);
    }
    /**
     * post application/json，无token
     * @param btcChain
     * @return
     */
    @RequestMapping(value= "/transaction", method = RequestMethod.POST)
    public JsonResult transaction(@RequestBody BtcChain btcChain){
        String url = "http://192.168.112.170:19112/btc/transaction";
        String params = "{\"toAddress\": \""+btcChain.getToAddress()+"\",\"amount\": "+btcChain.getAmount()+"}";
        JSONObject paramsJson = JSONObject.parseObject(params);
        JSONObject jsonObject = restTemplate.postForEntity(url,paramsJson,JSONObject.class).getBody();
        return new JsonResult(jsonObject);
    }

    /**
     * post application/json,带token
     * @param btcChain
     * @return
     */
    @RequestMapping(value = "auth/transaction",method = RequestMethod.POST)
    public JsonResult autytransaction(@RequestBody BtcChain btcChain){
        MediaType type = MediaType.parseMediaType("application/json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        headers.add("Accept", MediaType.ALL_VALUE);
        headers.add("token", "01efdd81-f93e-4bdb-9d66-c867eb29202b");
        JSONObject param = new JSONObject();
        param.put("toAddress",btcChain.getToAddress());
        param.put("amount",btcChain.getAmount());
        HttpEntity<Map> formEntity = new HttpEntity<>(param, headers);
        ResponseEntity<Object> responsebody = restTemplate.exchange(
                "http://192.168.112.170:19113/api/btc/transaction",
                HttpMethod.POST,
                formEntity,
                Object.class);
        return new JsonResult(responsebody.getBody());
    }

    /**
     * get
     */
    /**
     * get 无参数,无token
     * @return
     */
    @RequestMapping(value = "/getblockcount",method = RequestMethod.GET)
    public JsonResult getblockcount(){
        String url = "http://192.168.112.170:19111/btc/getblockcount";
        JSONObject btcRestChain = this.restTemplate.getForObject(url,JSONObject.class);
        System.out.println("btcRestChain==="+btcRestChain);
        return new JsonResult(btcRestChain);
    }

    /**
     * get 带参数，无token
     * @param txId
     * @return
     */
    @RequestMapping(value= "/getrawtransaction", method = RequestMethod.GET)
    public JsonResult getrawtransaction(String txId){
        String url = "http://192.168.112.170:19112/btc/getrawtransaction?txId="+txId;
        JSONObject btcRestChain = this.restTemplate.getForObject(url,JSONObject.class);
        System.out.println("btcRestChain==="+btcRestChain);
        return new JsonResult(btcRestChain);
    }

    /**
     * get 带参数，带token
     * @param txId
     * @return
     */
    @RequestMapping(value = "/auth/getrawtransaction",method = RequestMethod.GET)
    public JsonResult authgetrawtransaction(String txId){
        Map<String,Object> param = new HashMap<>();
        param.put("txId", txId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", "31839243-b75f-4cde-acc8-4d150af4fc37");
        ResponseEntity<Map> response = restTemplate.exchange(
                "http://192.168.112.170:19113/api/btc/getrawtransaction?txId={txId}",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                Map.class,
                param);
        Map<Object,Object> body = response.getBody();
        return new JsonResult(body);
    }

    /**
     * get 无参数，带token
     * @return
     */
    @RequestMapping(value = "/auth/getblockcount",method = RequestMethod.GET)
    public JsonResult authgetblockcount(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", "31839243-b75f-4cde-acc8-4d150af4fc37");
        ResponseEntity<Map> response = restTemplate.exchange(
                "http://192.168.112.170:19113/api/btc/getblockcount",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                Map.class);
        Map<Object,Object> body = response.getBody();
        return new JsonResult(body);
    }

}
