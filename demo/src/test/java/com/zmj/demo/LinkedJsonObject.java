package com.zmj.demo;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2023/5/24 16:04
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedJsonObject extends JSONObject {

    private LinkedHashMap<Object, Object> nameValuePairs;

    public LinkedJsonObject() {
        nameValuePairs = new LinkedHashMap<>();
    }

    @Override
    public JSONObject put(String name, int value) throws JSONException {
        // TODO Auto-generated method stub
        return put(name, value);
    }

    @Override
    public JSONObject put(String name, long value) throws JSONException {
        // TODO Auto-generated method stub
        return put(name, value);
    }

    @Override
    public JSONObject put(String name, double value) throws JSONException {
        // TODO Auto-generated method stub
        return put(name, value);
    }

    @Override
    public JSONObject put(String name, boolean value) throws JSONException {
        // TODO Auto-generated method stub
        return put(name, value);
    }

    @Override
    public JSONObject put(String name, Object value) throws JSONException {

        checkName(name);

        if (value == null) {
            nameValuePairs.remove(name);
            return this;
        }
        if (value instanceof Double) {
            if (((Double) value).isInfinite() || ((Double) value).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        } else if (value instanceof Float) {
            if (((Float) value).isInfinite() || ((Float) value).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
        nameValuePairs.put(name, value);

        return this;
    }


    String checkName(String name) throws JSONException {
        if (name == null) {
            throw new JSONException("Names must be non-null");
        }
        return name;
    }

    public String toString() {
        try {
            Iterator<Object> keys = nameValuePairs.keySet().iterator();
            StringBuffer sb = new StringBuffer("{");

            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(nameValuePairs.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof JSONStringer) {
            Object o;
            try {
                o = ((JSONStringer) value).toString();
            } catch (Exception e) {
                throw new JSONException(e.getMessage());
            }
            if (o instanceof String) {
                return (String) o;
            }
            throw new JSONException("Bad value from toJSONString: " + o);
        }
        if (value instanceof Number) {
            return numberToString((Number) value);
        }
        if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
            return value.toString();
        }
        if (value instanceof Map) {
            return new JSONObject((Map) value).toString();
        }
        if (value instanceof Collection) {
            return new JSONArray((Collection) value).toString();
        }
        return quote(value.toString());
    }
}

