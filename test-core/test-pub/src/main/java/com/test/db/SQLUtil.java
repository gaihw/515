package com.test.db;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

public class SQLUtil {

    public static String select(String baseColum, String tabName) {
        return "select " + baseColum.trim() + " from " + tabName;
    }

    public static String insertBatch(String baseColum, String tabName) {
        return "insert into " + tabName + "(" + baseColum + ") values ";
    }

    public static String insert(String baseColum, String tabName) {
        String qote = "";
        for (int i = 0; i < baseColum.trim().split(",").length; i++) {
            qote += "?,";
        }
        return "insert into " + tabName + "(" + baseColum + ") values(" + qote.substring(0, qote.length() - 1) + ")";
    }

    public static String update(String baseColum, String tabName) {
        String sql = "update " + tabName + " set ";
        String[] cols = baseColum.trim().split(",");
        for (String col : cols) {
            sql += col + " = ?,";
        }
        return sql.substring(0, sql.length() - 1);
    }

    public static String count(String tabName) {
        return "select count(*) as count from " + tabName;
    }

    public static String colIn(List list) {
        return " ('" + StringUtils.join(list, "','") + "')";
    }

    public static String colIn(Set set) {
        return " ('" + StringUtils.join(set, "','") + "')";
    }


}
