package com.test.dao;

import com.test.db.JdbcTemplateMgr;
import com.test.db.SQLUtil;

import java.util.List;

public class AddressDao {

    public static List<String> getColdAddressList(String coinName) {
        String sql = SQLUtil.select("address", "tb_wallet_address_cold_" + coinName);
        return JdbcTemplateMgr.query(sql, "address");
    }
}
