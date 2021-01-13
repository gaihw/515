
package com.eos.domain.response.chain.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class InlineTrace {

    @JsonProperty("receipt")
    private Receipt__ receipt;
    @JsonProperty("act")
    private Act_ act;
    @JsonProperty("elapsed")
    private Integer elapsed;
    @JsonProperty("cpu_usage")
    private Integer cpuUsage;
    @JsonProperty("console")
    private String console;
    @JsonProperty("total_cpu_usage")
    private Integer totalCpuUsage;
    @JsonProperty("trx_id")
    private String trxId;
    @JsonProperty("inline_traces")
    private List<Object> inlineTraces = null;

    @JsonProperty("receipt")
    public Receipt__ getReceipt() {
        return receipt;
    }

    @JsonProperty("receipt")
    public void setReceipt(Receipt__ receipt) {
        this.receipt = receipt;
    }

    @JsonProperty("act")
    public Act_ getAct() {
        return act;
    }

    @JsonProperty("act")
    public void setAct(Act_ act) {
        this.act = act;
    }

    @JsonProperty("elapsed")
    public Integer getElapsed() {
        return elapsed;
    }

    @JsonProperty("elapsed")
    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    @JsonProperty("cpu_usage")
    public Integer getCpuUsage() {
        return cpuUsage;
    }

    @JsonProperty("cpu_usage")
    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    @JsonProperty("console")
    public String getConsole() {
        return console;
    }

    @JsonProperty("console")
    public void setConsole(String console) {
        this.console = console;
    }

    @JsonProperty("total_cpu_usage")
    public Integer getTotalCpuUsage() {
        return totalCpuUsage;
    }

    @JsonProperty("total_cpu_usage")
    public void setTotalCpuUsage(Integer totalCpuUsage) {
        this.totalCpuUsage = totalCpuUsage;
    }

    @JsonProperty("trx_id")
    public String getTrxId() {
        return trxId;
    }

    @JsonProperty("trx_id")
    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    @JsonProperty("inline_traces")
    public List<Object> getInlineTraces() {
        return inlineTraces;
    }

    @JsonProperty("inline_traces")
    public void setInlineTraces(List<Object> inlineTraces) {
        this.inlineTraces = inlineTraces;
    }

}
