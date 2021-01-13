
package com.eos.domain.response.chain.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timestamp",
    "producer",
    "confirmed",
    "previous",
    "transaction_mroot",
    "action_mroot",
    "schedule_version",
    "new_producers",
    "header_extensions",
    "producer_signature",
    "transactions",
    "block_extensions",
    "id",
    "block_num",
    "ref_block_prefix"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("producer")
    private String producer;
    @JsonProperty("confirmed")
    private Long confirmed;
    @JsonProperty("previous")
    private String previous;
    @JsonProperty("transaction_mroot")
    private String transactionMroot;
    @JsonProperty("action_mroot")
    private String actionMroot;
    @JsonProperty("schedule_version")
    private Long scheduleVersion;
    @JsonProperty("new_producers")
    private Object newProducers;
    @JsonProperty("header_extensions")
    private List<Object> headerExtensions = null;
    @JsonProperty("producer_signature")
    private String producerSignature;
    @JsonProperty("transactions")
    private List<Transaction> transactions = null;
    @JsonProperty("block_extensions")
    private List<Object> blockExtensions = null;
    @JsonProperty("id")
    private String id;
    @JsonProperty("block_num")
    private Long blockNum;
    @JsonProperty("ref_block_prefix")
    private Long refBlockPrefix;

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("producer")
    public String getProducer() {
        return producer;
    }

    @JsonProperty("producer")
    public void setProducer(String producer) {
        this.producer = producer;
    }

    @JsonProperty("confirmed")
    public Long getConfirmed() {
        return confirmed;
    }

    @JsonProperty("confirmed")
    public void setConfirmed(Long confirmed) {
        this.confirmed = confirmed;
    }

    @JsonProperty("previous")
    public String getPrevious() {
        return previous;
    }

    @JsonProperty("previous")
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @JsonProperty("transaction_mroot")
    public String getTransactionMroot() {
        return transactionMroot;
    }

    @JsonProperty("transaction_mroot")
    public void setTransactionMroot(String transactionMroot) {
        this.transactionMroot = transactionMroot;
    }

    @JsonProperty("action_mroot")
    public String getActionMroot() {
        return actionMroot;
    }

    @JsonProperty("action_mroot")
    public void setActionMroot(String actionMroot) {
        this.actionMroot = actionMroot;
    }

    @JsonProperty("schedule_version")
    public Long getScheduleVersion() {
        return scheduleVersion;
    }

    @JsonProperty("schedule_version")
    public void setScheduleVersion(Long scheduleVersion) {
        this.scheduleVersion = scheduleVersion;
    }

    @JsonProperty("new_producers")
    public Object getNewProducers() {
        return newProducers;
    }

    @JsonProperty("new_producers")
    public void setNewProducers(Object newProducers) {
        this.newProducers = newProducers;
    }

    @JsonProperty("header_extensions")
    public List<Object> getHeaderExtensions() {
        return headerExtensions;
    }

    @JsonProperty("header_extensions")
    public void setHeaderExtensions(List<Object> headerExtensions) {
        this.headerExtensions = headerExtensions;
    }

    @JsonProperty("producer_signature")
    public String getProducerSignature() {
        return producerSignature;
    }

    @JsonProperty("producer_signature")
    public void setProducerSignature(String producerSignature) {
        this.producerSignature = producerSignature;
    }

    @JsonProperty("transactions")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @JsonProperty("transactions")
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @JsonProperty("block_extensions")
    public List<Object> getBlockExtensions() {
        return blockExtensions;
    }

    @JsonProperty("block_extensions")
    public void setBlockExtensions(List<Object> blockExtensions) {
        this.blockExtensions = blockExtensions;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("block_num")
    public Long getBlockNum() {
        return blockNum;
    }

    @JsonProperty("block_num")
    public void setBlockNum(Long blockNum) {
        this.blockNum = blockNum;
    }

    @JsonProperty("ref_block_prefix")
    public Long getRefBlockPrefix() {
        return refBlockPrefix;
    }

    @JsonProperty("ref_block_prefix")
    public void setRefBlockPrefix(Long refBlockPrefix) {
        this.refBlockPrefix = refBlockPrefix;
    }

}
