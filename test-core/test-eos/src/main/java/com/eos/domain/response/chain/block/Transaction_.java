
package com.eos.domain.response.chain.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    "transaction_extensions"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction_ {

    @JsonProperty("expiration")
    private String expiration;
    @JsonProperty("ref_block_num")
    private Long refBlockNum;
    @JsonProperty("ref_block_prefix")
    private Long refBlockPrefix;
    @JsonProperty("max_net_usage_words")
    private Long maxNetUsageWords;
    @JsonProperty("max_cpu_usage_ms")
    private Long maxCpuUsageMs;
    @JsonProperty("delay_sec")
    private Long delaySec;
    @JsonProperty("context_free_actions")
    private List<Object> contextFreeActions = null;
    @JsonProperty("actions")
    private List<Action> actions = null;
    @JsonProperty("transaction_extensions")
    private List<Object> transactionExtensions = null;

    @JsonProperty("expiration")
    public String getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("ref_block_num")
    public Long getRefBlockNum() {
        return refBlockNum;
    }

    @JsonProperty("ref_block_num")
    public void setRefBlockNum(Long refBlockNum) {
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
    public Long getDelaySec() {
        return delaySec;
    }

    @JsonProperty("delay_sec")
    public void setDelaySec(Long delaySec) {
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

}
