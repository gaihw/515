package blockchain.btcd;

import blockchain.Config;
import blockchain.Utils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Base64;

public class Test {
    public static void main(String[] args) {
        String s = "[\n" +
                "  {\n" +
                "    \"txid\": \"1a2048351b9d51fe96e7070ca746ac1bd741df9aaf5be3b3e6f54d755b2e32e4\",\n" +
                "    \"vout\": 2,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 109,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"1b49e0786eb93721cb8ebf3f31712678baf3bfbe65e6b76fbcb61dcc523cdac1\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yX8y2eT1sVFmm3S6pJLUhMiztAZh4k469K\",\n" +
                "    \"scriptPubKey\": \"76a91476ae0047339c855d4da3b6e7f9b7c94d8b6d875e88ac\",\n" +
                "    \"amount\": 0.07134909,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"8d2a1bc7eb4da2c7ea361344fd323c3df6e62f8dae9ce0974436df3a040f81b1\",\n" +
                "    \"vout\": 2,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 129,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"85ec08ff6a7e6153d63caf81b43ada4ec2d600a7f38f8cec846120ea75c7709f\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yaoGvc7UfsLarrSZkqbAWesvQBDoqFSrCk\",\n" +
                "    \"scriptPubKey\": \"76a9149ed51faa78b298a5fe03f2ec0c955746c8b4c8b188ac\",\n" +
                "    \"amount\": 0.01133772,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"28624a348a42a3e054ebd4fc6e10d748e1bddb2ed0910a0566098662f2eb1f8b\",\n" +
                "    \"vout\": 1,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 12,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"b57b36bb95005edf228429ec7100437da1e18782ce0bac6e6953fda74c879c26\",\n" +
                "    \"vout\": 2,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 32,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"e0ff6ff4b2645eda68a9f360d02454acb8cc791edb136d25f0379b9e965cc608\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yWnfoAjesaCHwAGPXhzJBePYEKTV2sJ437\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a91472d74c075fea902fc008dd28b32cbcdc3763548788ac\",\n" +
                "    \"amount\": 0.03150501,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"f13191f8bc709bf61ff1adfa598582e09024809cc123e881535b1345bfb3f95d\",\n" +
                "    \"vout\": 2,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 68,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"1fd697ecfbbf6f88b487a402579587c623eb7bf92025164310d555b0c49d6e68\",\n" +
                "    \"vout\": 1,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 58,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"106d459db0070d63617baf1c311037e004d0a4a40e7ebd1817e5660042307943\",\n" +
                "    \"vout\": 1,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00043243,\n" +
                "    \"confirmations\": 211,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"3fb4f4a1392f595237cb3ec2b85de8fa04a60dbb83d8e4299ce433f5b20c48ce\",\n" +
                "    \"vout\": 1,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 48,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"de3cf845623246499d10b60477fad857e47de234577b0a4b1ec00bf38da34072\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yhuQ7BUD84XYFp6Pd8N7eXUJtwJLi2MZn7\",\n" +
                "    \"scriptPubKey\": \"76a914ecc67ed6686229e3e8ffecd90bbf229a087f599888ac\",\n" +
                "    \"amount\": 0.03222754,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"ee47f0a967d9b622cdd5c3869783649acbf9e0854e2b159482e9126dfb3af887\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yethcBy5YFapEBy515BCw9ryS4G8GQNoRS\",\n" +
                "    \"scriptPubKey\": \"76a914cbbc4f7f2514576e933529da47a7ae699dd353bc88ac\",\n" +
                "    \"amount\": 0.01360200,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"4611a3122cafed074b2a1b72c0c21810e8bda9841db8e26b44a6c63c918574dd\",\n" +
                "    \"vout\": 1,\n" +
                "    \"address\": \"yWYevwX9kaKU8rMP517Ha3cXHLPRbtTEs1\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a9147030c028e8001cc01f789fd142cd4dcdad5407f188ac\",\n" +
                "    \"amount\": 0.00013542,\n" +
                "    \"confirmations\": 22,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -3\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"27e2f8b4246f1ea19f6e6b9e98dd2f49efeddfc4c58e558e29066328e958877e\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yR36PofM9pygfyptrww9Wk6mu7FuKJzQSV\",\n" +
                "    \"scriptPubKey\": \"76a91433c0e6007a6a79f7db3bd16973a5dcbad1484afa88ac\",\n" +
                "    \"amount\": 0.02280682,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"77cfeac7fe68fa0aef148c4e3214d2234e9ad7707334d92c6ad67a74aecba692\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yWHjGqnc3ph9aiwa6mf1FfDk6N114nCNLU\",\n" +
                "    \"scriptPubKey\": \"76a9146d5e22fbc1d6dd6e167d2e0641c54030db4de5eb88ac\",\n" +
                "    \"amount\": 0.02198922,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  },\n" +
                "  {\n" +
                "    \"txid\": \"80fb3d44100b7b41d69786841f9f524aec301dc8078d3f68b97c839d4be37cb9\",\n" +
                "    \"vout\": 0,\n" +
                "    \"address\": \"yWnfoAjesaCHwAGPXhzJBePYEKTV2sJ437\",\n" +
                "    \"account\": \"\",\n" +
                "    \"scriptPubKey\": \"76a91472d74c075fea902fc008dd28b32cbcdc3763548788ac\",\n" +
                "    \"amount\": 0.08392422,\n" +
                "    \"confirmations\": 43,\n" +
                "    \"spendable\": true,\n" +
                "    \"solvable\": true,\n" +
                "    \"safe\": true,\n" +
                "    \"ps_rounds\": -2\n" +
                "  }\n" +
                "]";
        JSONArray jsonArray = JSONArray.parseArray(s);
        //创建交易
        JSONArray jsonArray1 = new JSONArray();
        //签名
        JSONArray jsonArray2 = new JSONArray();
        //私钥
        JSONArray jsonArray3 = new JSONArray();
        //总量
        BigDecimal bigDecimal = BigDecimal.ZERO;

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txid",jsonArray.getJSONObject(i).getString("txid"));
            jsonObject.put("vout",jsonArray.getJSONObject(i).getInteger("vout"));
            jsonArray1.add(i,jsonObject);
            bigDecimal = bigDecimal.add(jsonArray.getJSONObject(i).getBigDecimal("amount"));

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("txid",jsonArray.getJSONObject(i).getString("txid"));
            jsonObject1.put("vout",jsonArray.getJSONObject(i).getInteger("vout"));
            jsonObject1.put("amount",jsonArray.getJSONObject(i).getString("amount"));
            jsonObject1.put("scriptPubKey",jsonArray.getJSONObject(i).getString("scriptPubKey"));
            jsonObject1.put("redeemScript",jsonArray.getJSONObject(i).getString("redeemScript"));
            jsonArray2.add(i,jsonObject1);

            String pri = JSONObject.parseObject(dumpprivkey(jsonArray.getJSONObject(i).getString("address"))).getString("result");
            if (!jsonArray3.contains(pri)){
                jsonArray3.add(pri);
            }
        }
        System.out.println("create="+jsonArray1);
        System.out.println("sign="+jsonArray2);
        System.out.println("priv="+jsonArray3);
        System.out.println("sum="+bigDecimal);


//dash 测试签名
//        0200000011e4322e5b754df5e6b3e35baf9adf41d71bac46a70c07e796fe519d1b3548201a020000006b483045022100d994035b8699a9a9ec0db3d8f8feb1782c830686858aeaae3e998e46bfdee667022057acfafbf914bdb9363fa89186c0c0276dc15288693fd92754038cf834dd3420012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffffc1da3c52cc1db6bc6fb7e665bebff3ba782671313fbf8ecb2137b96e78e0491b000000006a473044022029a3e0813d1739380e9d85204095b2cd7fcfd335af2d21cbdd5a3b4157b96fd1022051a3fcc4be7c72eaf158ded93bf3db40e3ae50fbe378afb7c30dd5adc83a39280121037db5b8f5e5ae79ddff86d7d19b1fc82789deba95426222819ee4137f1e8b2a36ffffffffb1810f043adf364497e09cae8d2fe6f63d3c32fd441336eac7a24debc71b2a8d020000006b483045022100ec5c356c515abae9c37124d7590a3d65bf3ebe58bf19a404a2884ddc587fa87f022077a68e1f6feac045b1253b87206cd3658cccc5202805bd0ee02e1c50db7ef2a3012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff9f70c775ea206184ec8c8ff3a700d6c24eda3ab481af3cd653617e6aff08ec85000000006a47304402207bb8bc2b0dbcbd46d467a3f218ca789406b1a7b89a6323bc35d0ef555554da410220378d9781b37a567a866349a8f9302a601f03c5fe62daa80c5f6868c41e9760ef012102a25c7c078e3d96dd6f9fe6c0b8c8ff21958b7852b92df75d16f605e2d9ed2927ffffffff8b1febf262860966050a91d02edbbde148d7106efcd4eb54e0a3428a344a6228010000006a473044022073e92511d11f869cb8370a411407dc0dee20245d68b73d3537eb21def73d74ae02204d15400ea56171737e0e1087f096016ce867af342764eab81c2f7f1dcf2be616012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff269c874ca7fd53696eac0bce8287e1a17d430071ec298422df5e0095bb367bb5020000006b483045022100b1d3baa541f24b1e0ec7a1c52598b70eb2b128669b244d8a48212d49393abe6302202ce11a4d3eae82adf72201e74e3adc0b5c513a453b80c2b49db0752dd34ae98a012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff08c65c969e9b37f0256d13db1e79ccb8ac5424d060f3a968da5e64b2f46fffe0000000006b483045022100a3cac2b28c4cc36d9f36c58faefcf0d0b36aab785c66a27f65b1a2ff0aa7db91022009d16a90f82bf3cdbe945e8b3d15d1baad56d749d34c18e3b0fc1413129988640121023a853d0162ca8c5bc0d4d2198bf7ccd1002f30cabda68eea5baf8854944e087effffffff5df9b3bf45135b5381e823c19c802490e0828559faadf11ff69b70bcf89131f1020000006a47304402206aa065cbf54e3e5a9b01044908d2821d2d9cc116178a36150ab96fcb79d2fcd80220723c4e854fda9eb4464c99432ea806be4e63cf1c71647c8bc8f5f04be00634c9012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff686e9dc4b055d51043162520f97beb23c687955702a487b4886fbffbec97d61f010000006a473044022001c502925733204ee0b35a064d12a068a30c9d494951aa2da7c138ad7c28254702205b1f3c4be38992d5979f670c49e04b65555202cad67442af95aff91599a5835c012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff437930420066e51718bd7e0ea4a4d004e03710311caf7b61630d07b09d456d10010000006a47304402203147ed6b531cb6a273abecdb7e94275734c37772bff12cafafa827ab740feeea022015ac4f21b2fa3d27fe078e00a085890b3307a1b350e8aef3f39d7641788a8ea6012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffffce480cb2f533e49c29e4d883bb0da604fae85db8c23ecb3752592f39a1f4b43f010000006b483045022100f5d508a2bca1a7d1117257b7883fd0da9f4cb93777bbb82c0d8382b8dbd71bec02207d68512368cad72c0a1089ab50bad7cf3fda7c4a1c0941b6ff54da3545120df6012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff7240a38df30bc01e4b0a7b5734e27de457d8fa7704b6109d4946326245f83cde000000006a473044022072bf85a1749926f0567a6432464c11f406defd33283cd538e3f78e8d970db37e022015941fefbd34e2528a7cbe914715433ec6a7f27ed181f9609bba675142061d83012102de0e02ba71a1a5031a74b5432d324520026b5054e8e4b20dc1a5aadcae68051effffffff87f83afb6d12e98294152b4e85e0f9cb9a64839786c3d5cd22b6d967a9f047ee000000006a4730440220152d8e5e6bbc5e259a7025bfe7355fce97b005e06bb90b51cd1544a70a998190022002c8331a6596df32cc026d84c5d5e4d4a7a00547e78a168361b3583ddc13dfbd0121020af68972454eb67c77a1d6fca862b8a883df74be372a3daae1b210cae3e3e767ffffffffdd7485913cc6a6446be2b81d84a9bde81018c2c0721b2a4b07edaf2c12a31146010000006b4830450221008e7397264f9493f7bf09edd56afe174b26d5596b26ef168bba81219e55d7a62d02205b7352d1740f0c81f0cc1c5966e378216c1be4594f93f47c2ebc39b908112d9e012102cd8e2466b66f3a4065b8e3fda9ee303c7863e334b312b6c33ccbfd60636157d9ffffffff7e8758e9286306298e558ec5c4dfedef492fdd989e6b6e9fa11e6f24b4f8e227000000006a473044022072da32fc4a55c9ad6fe0fa5c87f7c00dbe6050fb5bda95fc2649f5131dfcbebe02201e08aa107daa9badd7397b15c74862c00269ff439fdbf2778423d185f24bc957012103981c001f2db6aed59a5bd0efbb4044755b33a7d44344bd365dadc85d2f5846afffffffff92a6cbae747ad66a2cd9347370d79a4e23d214324e8c14ef0afa68fec7eacf77000000006b483045022100b193f0ea67211e60c4072ce9a204ec73a5a71625cb3aca9fc2712ac6678e0941022042c9e31ef7bd5c8568a4ada443d66c88fe173b3ffdd72ef0f777e76a357fae9a0121029d80858e48264494142ddccbe6150fbc07937902cc6645159c9a2faa65a3bf2bffffffffb97ce34b9d837cb9683f8d07c81d30ec4a529f1f848697d6417b0b10443dfb80000000006b483045022100ddcb1604f05c9f88330fedde345d77d748b9aa71a8417393069aab10425645af02200c089c3a8418237fcfc0d31e97a081fe080ddfe11440a840e96adc94584149540121023a853d0162ca8c5bc0d4d2198bf7ccd1002f30cabda68eea5baf8854944e087effffffff0160cfba01000000001976a9143b4b0e4b16518ef312e6a1b25e5ffddcd71550b488ac00000000

    }
    /**
     * 获取地址私钥
     * @param address
     * @return
     */
    public static String dumpprivkey(String address){
        Base64.Encoder encoder = Base64.getEncoder();
        String cred = encoder.encodeToString((Config.BTC_USERNMAME + ":" + Config.BTC_PASSWORD).getBytes());
        String params = "{\"jsonrpc\": \"1.0\", \"id\":\"curltest\", \"method\": \"dumpprivkey\", \"params\": [\""+address+"\"] }";
        return Utils.postByJson("http://192.168.112.66:6332",cred,params);
    }
}
