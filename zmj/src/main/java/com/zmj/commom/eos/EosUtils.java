package com.zmj.commom.eos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.commom.HttpUtils;
import com.zmj.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.zmj.config.Config.*;

@Component
@Slf4j
public class EosUtils {


    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据私钥获取公钥
     * @param wallet
     * @param password
     * @param priv
     * @return
     */
    public  String getPubByPriv(String wallet,String password,String priv){
        JSONArray jsonArray = JSONArray.parseArray(list_keys(wallet,password));
        for (int i = 0 ; i < jsonArray.size() ; i++){
            if (jsonArray.getJSONArray(i).getString(1).equals(priv)){
                return jsonArray.getJSONArray(i).getString(0);
            }
        }
        return null;
    }
    /**
     * 创建交易方法
     */
    public  String createTransaction(String fromAcount,String toAcount,String value,String memo){
        //1、转账的账户
//        String acount = "testgai";
        //解锁钱包
        System.out.println(unlock(WALLET,WALLET_PASSWORD));
//        httpUtils.postByText("http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/unlock","[\""+ WALLET+"\", \""+ WALLET_PASSWORD+"\"]");
        //2、abi_json_to_bin 将转账信息由json格式序列化为bin格式字符串，获取binargs
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/abi_json_to_bin";
        String params = "{\"code\":\"eosio.token\",\"action\":\"transfer\",\"args\":{\"from\":\""+fromAcount+"\",\"to\":\""+toAcount+"\",\"quantity\":\""+value+" EOS\", \"memo\":\""+memo+"\"}}";
//        System.out.println(params);
        String binargs = JSONObject.parseObject(httpUtils.postByJson(url,params)).getString("binargs");

        //3、获取当前最新的区块编号，获取到head_block_num和chain_id
        String getInfoResponse = get_info();
        String head_block_num = JSONObject.parseObject(getInfoResponse).getString("head_block_num");
        String chain_id = JSONObject.parseObject(getInfoResponse).getString("chain_id");

        //4、根据区块编号获取区块详情，获取到timestamp 和ref_block_prefix，并且添加过期时间
        String headBlockNumResponee = get_block(head_block_num);
        String[] timestamp1 = JSONObject.parseObject(headBlockNumResponee).getString("timestamp").split("T");
        String[] timestamp2 = timestamp1[1].split(":");
        int hour = Integer.parseInt(timestamp2[0])+1;
        String timestamp= timestamp1[0]+"T"+hour+":"+timestamp2[1]+":"+timestamp2[2];
        String ref_block_prefix = JSONObject.parseObject(headBlockNumResponee).getString("ref_block_prefix");

        //5、筛选出签署交易需要的公钥
//        钱包中可能有很多公钥存在，调用此方法可以筛选出完成此次交易需要的公钥。如果已明确应该使用的公钥，那可以省略此方法。
//        首先调用API打开并解锁钱包，然后获取所有的公钥。这部分内容在EOS开发(八)RPC API都有使用介绍。
//        参数说明：
//        available_keys 钱包中的公钥
//        account 合约名称。这里是转账EOS，使用的是eosio.token
//        actor 调用者。这里相当于转账方
//        permission 使用的权限类型
//        data 之前生成的bin字符串
//        name 调用的合约方法。这里调用的是转账方法transfer
//        expiration 过期时间。这里将timestamp加上了20分钟。可以根据需要来增加时长
//        ref_block_num 前面获取的最新区块号
//        ref_block_prefix 前面获取的ref_block_prefix
        String required_keys = JSONObject.parseObject(get_required_keys(get_public_keys(),fromAcount,binargs,timestamp,head_block_num,ref_block_prefix)).getJSONArray("required_keys").getString(0);

        //6、签署交易
//        参数说明：
//        ref_block_num 前面获取的最新区块号
//        ref_block_prefix 前面获取的ref_block_prefix
//        expiration 过期时间。这里将timestamp加上了20分钟。可以根据需要来增加时长
//        account 合约名称。这里是转账EOS，使用的是eosio.token
//        name 调用的合约方法。这里调用的是转账方法transfer
//        actor 调用者。这里相当于转账方
//        permission 使用的权限类型
//        data 之前生成的bin字符串
//        EOS6Z7mUQeFC2cQTT3xMyZh2wsLQoHih1bTMgRhr3dbichprTi7Rc 签署此交易的公钥。实际上是由钱包中对应的私钥来签
//        038f4b0fc8ff18a4f0842a8f0564611f6e96e8535901dd45e43ac8691a1c4dca 链id。注明当前处于主网/测试网/私有网络。这里显示的为测试网络
        String signatures = JSONObject.parseObject(sign_transaction(head_block_num,ref_block_prefix ,timestamp,fromAcount,binargs ,required_keys,chain_id)).getJSONArray("signatures").getString(0);
//        System.out.println(signatures);
        //7、提交交易
        //参数说明：
        //expiration 过期时间。这里将timestamp加上了20分钟。可以根据需要来增加时长
        //ref_block_num 前面获取的最新区块号
        //ref_block_prefix 前面获取的ref_block_prefix
        //account 合约名称。这里是转账EOS，使用的是eosio.token
        //name 调用的合约方法。这里调用的是转账方法transfer
        //actor 调用者。这里相当于转账方
        //permission 使用的权限类型
        //data 之前生成的bin字符串
        //signatures 签署交易后生成的签名字符串
        return push_transaction(signatures,timestamp,head_block_num,ref_block_prefix,fromAcount,binargs);

    }

    /**
     * chain
     */
    /**
     * 获取账户余额
     * @param account
     * @return
     */
    public  String get_currency_balance(String account){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/get_currency_balance";

        String params = "{\"code\":\"eosio.token\", \"account\":\""+account+"\", \"symbol\":\"eos\"}";
        return httpUtils.postByJson(url,params).toString();
    }
    /**
     * 获取链信息
     */
    public  String get_info(){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/get_info";
        return httpUtils.postByJson(url).toString();
    }
    /**
     * 获取区块详情信息
     */
    public  String get_block(String blockNnm){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/get_block";
        String params = "{\"block_num_or_id\":\""+blockNnm+"\"}";
        return httpUtils.postByJson(url,params).toString();
    }

    /**
     * 筛选出签署交易需要的公钥
     */
    public  String get_required_keys(String available_keys , String account, String binargs, String timestamp, String ref_block_num, String ref_block_prefix){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/get_required_keys";
        String params = "{\n" +
                "    \"available_keys\": "+available_keys+"," +
                "    \"transaction\": {\n" +
                "        \"actions\": [\n" +
                "            {\n" +
                "                \"account\": \"eosio.token\",\n" +
                "                \"authorization\": [\n" +
                "                    {\n" +
                "                        \"actor\": \""+account+"\",\n" +
                "                        \"permission\": \"active\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"data\": \""+binargs+"\",\n" +
                "                \"name\": \"transfer\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"context_free_actions\": [\n" +
                "        ],\n" +
                "        \"context_free_data\": [\n" +
                "        ],\n" +
                "        \"delay_sec\": 0,\n" +
                "        \"expiration\": \""+timestamp+"\",\n" +
                "        \"max_kcpu_usage\": 0,\n" +
                "        \"max_net_usage_words\": 0,\n" +
                "        \"ref_block_num\": "+ref_block_num+",\n" +
                "        \"ref_block_prefix\": "+ref_block_prefix+",\n" +
                "        \"signatures\": [\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        return httpUtils.postByJson(url,params).toString();
    }

    /**
     * 提交交易
     */
    public  String push_transaction(String signatures,String timestamp,String ref_block_num,String ref_block_prefix,String acount,String binargs ){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT +"/v1/chain/push_transaction";
        String params = "{\n" +
                "  \"compression\": \"none\",\n" +
                "  \"transaction\": {\n" +
                "    \"expiration\": \""+timestamp+"\",\n" +
                "    \"ref_block_num\": "+ref_block_num+",\n" +
                "    \"ref_block_prefix\": "+ref_block_prefix+",\n" +
                "    \"context_free_actions\": [],\n" +
                "    \"actions\": [\n" +
                "        {\n" +
                "            \"account\": \"eosio.token\",\n" +
                "            \"name\": \"transfer\",\n" +
                "            \"authorization\": [\n" +
                "                {\n" +
                "                    \"actor\": \""+acount+"\",\n" +
                "                    \"permission\": \"active\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"data\": \""+binargs+"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"transaction_extensions\": []\n" +
                "  },\n" +
                "  \"signatures\": [\n" +
                "        \""+signatures+"\"\n" +
                "   ]\n" +
                "}";
        return httpUtils.postByJson(url,params).toString();
    }

    /**
     * 调用返回指定账号所托管合约的abi描述信息
     * @param account_name
     * @return
     */
    public  String get_abi(String account_name){
        String url = "http://"+ EOS_URL+":"+EOS_NODEOSD_PORT+"/v1/chain/get_abi";
        String params = "{\"account_name\": \""+account_name+"\"}";
        return httpUtils.postByJson(url,params).toString();
    }


    /**
     * wallet
     */
    /**
     * 获取指定钱包的公私钥对
     */
    public  String list_keys(String walletName,String password){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/list_keys";
        String params = "[\""+walletName+"\",\""+password+"\"]";

        return httpUtils.postByText(url,params).toString();
    }
    /**
     * 开启钱包
     */
    public  String open(String testcreate){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/open";
        String params = "\""+testcreate+"\"";
        return httpUtils.postByText(url,params).toString();
    }

    /**
     * 解锁钱包
     * @param wallet
     * @param password
     * @return
     */
    public  String unlock(String wallet,String password){
        //查看钱包列表
        String[] lw = list_wallets().split(",");
        for (int i = 0; i < lw.length; i++) {
            //如果未解锁，解锁
            if (lw[i].contains(WALLET) && !lw[i].contains("*")) {
                String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/unlock";
                String params = "[\""+wallet+"\", \""+password+"\"]";
                return httpUtils.postByText(url,params)+"已解锁！";
            }
        }
        return "已解锁！";
    }
    /**
     * 签署交易
     */
    public  String sign_transaction(String ref_block_num,String ref_block_prefix ,String timestamp,String acount,String binargs ,String required_keys,String chain_id){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/sign_transaction";
        String params = "[{\"ref_block_num\": "+ref_block_num+",\"ref_block_prefix\": "+ref_block_prefix+",\"expiration\": \""+timestamp+"\",\"actions\": [{\"account\": \"eosio.token\",\"name\": \"transfer\",\"authorization\": [{\"actor\": \""+acount+"\",\"permission\": \"active\"}],\"data\":\""+binargs+"\"}],\"signatures\": []},[\""+required_keys+"\"],\""+chain_id+"\"]";

        return httpUtils.postByText(url,params).toString();
    }
    /**
     * 查看钱包列表，查看钱包open+unlock状态
     */
    public  String list_wallets(){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/list_wallets";
        return httpUtils.postByJson(url).toString();
    }
    /**
     * 创建钱包
     */
    public  String create(String acount){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/create";
        String params = acount;
        return httpUtils.postByJson(url,params).toString();
    }
    /**
     * wallet_get_public_keys
     */
    public  String get_public_keys(){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT;
        return httpUtils.postByJson(url+"/v1/wallet/get_public_keys").toString();
    }

    /**
     * 用给定的名字创建一个新的钱包
     * @param walletName
     * @return
     */
    public String getnewaddress(String walletName){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/create";
        String params = "\""+walletName+"\"";
        return httpUtils.postByJson(url,params).toString();
    }

    /**
     * producer
     */
    /**
     * 挖矿
     * @return
     */
    public  String resume(){
        String url = "http://"+ EOS_URL+":"+ EOS_NODEOSD_PORT+"/v1/producer/resume";
        return httpUtils.postByJson(url).toString();
    }
    /**
     * 停止挖矿
     * @return
     */
    public  String pause(){
        String url = "http://"+ EOS_URL+":"+EOS_NODEOSD_PORT+"/v1/producer/pause";
        return httpUtils.postByJson(url).toString();
    }
    /**
     * history
     */
    /**
     * 根据公钥查询账户
     * @param public_key
     * @return
     */
    public  String get_key_accounts(String public_key){
        String url = "http://"+ EOS_URL+":"+EOS_KEOSD_PORT+"/v1/wallet/create";
        String params = "{\"public_key\":\""+public_key+"\"}";
        return httpUtils.postByJson(url,params).toString();
    }
}
