
package com.eos.domain.response.chain.transaction.actions;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "account",
    "name",
    "authorization",
    "data",
    "hex_data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Act {

    @JsonProperty("account")
    public String account;
    @JsonProperty("name")
    public String name;
    @JsonProperty("authorization")
    public List<Authorization> authorization = null;

    @JsonIgnore
    public Data data;

    @JsonProperty("hex_data")
    public String hexData;


    @JsonAnySetter
    public void setDataFromJson(String name, Object value)
    {
        // if value is single String, call appropriate ctor
        if (value instanceof String) {
            data = null;
        }
        // if value is map, it must contain 'val1',  'val2' entries
        if (value instanceof Map) {
           data = JSONObject.parseObject(new JSONObject((Map)value).toString(),Data.class);
        }
        // error?
    }

}
