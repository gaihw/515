
package com.eos.domain.response.chain.transaction.actions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "receipt",
    "act",
    "elapsed",
    "cpu_usage",
    "console",
    "total_cpu_usage",
    "trx_id",
    "inline_traces"
})
public class ActionTrace {

    @JsonProperty("receipt")
    public Receipt receipt;
    @JsonProperty("act")
    public Act act;
    @JsonProperty("elapsed")
    public Integer elapsed;
    @JsonProperty("cpu_usage")
    public Integer cpuUsage;
    @JsonProperty("console")
    public String console;
    @JsonProperty("total_cpu_usage")
    public Integer totalCpuUsage;
    @JsonProperty("trx_id")
    public String trxId;
    @JsonProperty("inline_traces")
    public List<Object> inlineTraces = null;

}
