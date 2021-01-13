package com.test.dao;

import com.test.db.JdbcTemplateMgr;
import com.test.db.SQLUtil;

public class ConfigDao {

    // `currency_id`, `type`, `name`, `alarm_balance`, `receive_num`, `status_flag`, `rpc_url`, `fee_factor`, `sign_type`

    final static String TAB_NAME = "tb_currency_info";

    public static String getCurrencyInfoRpcUrl(String coinName) {
        String rpcUrlParam = JdbcTemplateMgr.query(SQLUtil.select("rpc_url", TAB_NAME) + " where name = '" + coinName + "'", "rpc_url").get(0).toString();
        return rpcUrlParam;
    }




}
