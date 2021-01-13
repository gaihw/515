
package com.eos.domain.response.chain.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "expiration",
    "ref_block_num",
    "ref_block_prefix",
    "max_net_usage_words",
    "max_cpu_usage_ms",
    "delay_sec",
    "context_free_actions",
    "actions",
    "transaction_extensions",
    "signatures",
    "context_free_data"
})
public class Trx__ {

    @JsonProperty("expiration")
    private String expiration;
    @JsonProperty("ref_block_num")
    private Integer refBlockNum;
    @JsonProperty("ref_block_prefix")
    private Long refBlockPrefix;
    @JsonProperty("max_net_usage_words")
    private Long maxNetUsageWords;
    @JsonProperty("max_cpu_usage_ms")
    private Long maxCpuUsageMs;
    @JsonProperty("delay_sec")
    private Integer delaySec;
    @JsonProperty("context_free_actions")
    private List<Object> contextFreeActions = null;
    @JsonProperty("actions")
    private List<Action> actions = null;
    @JsonProperty("transaction_extensions")
    private List<Object> transactionExtensions = null;
    @JsonProperty("signatures")
    private List<String> signatures = null;
    @JsonProperty("context_free_data")
    private List<Object> contextFreeData = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("expiration")
    public String getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("ref_block_num")
    public Integer getRefBlockNum() {
        return refBlockNum;
    }

    @JsonProperty("ref_block_num")
    public void setRefBlockNum(Integer refBlockNum) {
        this.refBlockNum = refBlockNum;
    }

    @JsonProperty("ref_block_prefix")
    public Long getRefBlockPrefix() {
        return refBlockPrefix;
    }

    @JsonProperty("ref_block_prefix")
    public void setRefBlockPrefix(Long refBlockPrefix) {
        this.refBlockPrefix = refBlockPrefix;
    }

    @JsonProperty("max_net_usage_words")
    public Long getMaxNetUsageWords() {
        return maxNetUsageWords;
    }

    @JsonProperty("max_net_usage_words")
    public void setMaxNetUsageWords(Long maxNetUsageWords) {
        this.maxNetUsageWords = maxNetUsageWords;
    }

    @JsonProperty("max_cpu_usage_ms")
    public Long getMaxCpuUsageMs() {
        return maxCpuUsageMs;
    }

    @JsonProperty("max_cpu_usage_ms")
    public void setMaxCpuUsageMs(Long maxCpuUsageMs) {
        this.maxCpuUsageMs = maxCpuUsageMs;
    }

    @JsonProperty("delay_sec")
    public Integer getDelaySec() {
        return delaySec;
    }

    @JsonProperty("delay_sec")
    public void setDelaySec(Integer delaySec) {
        this.delaySec = delaySec;
    }

    @JsonProperty("context_free_actions")
    public List<Object> getContextFreeActions() {
        return contextFreeActions;
    }

    @JsonProperty("context_free_actions")
    public void setContextFreeActions(List<Object> contextFreeActions) {
        this.contextFreeActions = contextFreeActions;
    }

    @JsonProperty("actions")
    public List<Action> getActions() {
        return actions;
    }

    @JsonProperty("actions")
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @JsonProperty("transaction_extensions")
    public List<Object> getTransactionExtensions() {
        return transactionExtensions;
    }

    @JsonProperty("transaction_extensions")
    public void setTransactionExtensions(List<Object> transactionExtensions) {
        this.transactionExtensions = transactionExtensions;
    }

    @JsonProperty("signatures")
    public List<String> getSignatures() {
        return signatures;
    }

    @JsonProperty("signatures")
    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    @JsonProperty("context_free_data")
    public List<Object> getContextFreeData() {
        return contextFreeData;
    }

    @JsonProperty("context_free_data")
    public void setContextFreeData(List<Object> contextFreeData) {
        this.contextFreeData = contextFreeData;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
