
package com.eos.domain.response.chain.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@JsonPropertyOrder({
    "id",
    "receipt",
    "elapsed",
    "net_usage",
    "scheduled",
    "action_traces",
    "except"
})
public class Processed {

    @JsonProperty("id")
    private String id;
    @JsonProperty("receipt")
    private Receipt receipt;
    @JsonProperty("elapsed")
    private Integer elapsed;
    @JsonProperty("net_usage")
    private Integer netUsage;
    @JsonProperty("scheduled")
    private Boolean scheduled;
    @JsonProperty("action_traces")
    private List<ActionTrace> actionTraces = null;
    @JsonProperty("except")
    private Object except;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("receipt")
    public Receipt getReceipt() {
        return receipt;
    }

    @JsonProperty("receipt")
    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @JsonProperty("elapsed")
    public Integer getElapsed() {
        return elapsed;
    }

    @JsonProperty("elapsed")
    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    @JsonProperty("net_usage")
    public Integer getNetUsage() {
        return netUsage;
    }

    @JsonProperty("net_usage")
    public void setNetUsage(Integer netUsage) {
        this.netUsage = netUsage;
    }

    @JsonProperty("scheduled")
    public Boolean getScheduled() {
        return scheduled;
    }

    @JsonProperty("scheduled")
    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    @JsonProperty("action_traces")
    public List<ActionTrace> getActionTraces() {
        return actionTraces;
    }

    @JsonProperty("action_traces")
    public void setActionTraces(List<ActionTrace> actionTraces) {
        this.actionTraces = actionTraces;
    }

    @JsonProperty("except")
    public Object getExcept() {
        return except;
    }

    @JsonProperty("except")
    public void setExcept(Object except) {
        this.except = except;
    }

}
