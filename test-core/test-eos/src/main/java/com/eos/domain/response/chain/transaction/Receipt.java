
package com.eos.domain.response.chain.transaction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
@JsonInclude ( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder ( {
    "status",
    "cpu_usage_us",
    "net_usage_words",
    "trx"
} )

@JsonIgnoreProperties ( ignoreUnknown = true )
public class Receipt {

    @JsonProperty ( "status" )
    private String status;
    @JsonProperty ( "cpu_usage_us" )
    private Integer cpuUsageUs;
    @JsonProperty ( "net_usage_words" )
    private Integer netUsageWords;

    @JsonIgnore
    private List<Trx_> trx;

    @JsonAnySetter
    public void setTrxFromJson(String name, Object value) {
        trx = new ArrayList<>();
        ArrayList list = (ArrayList)value;
        trx.add(new JSONObject((LinkedHashMap)list.get(1)).toJavaObject(Trx_.class));
    }
}
