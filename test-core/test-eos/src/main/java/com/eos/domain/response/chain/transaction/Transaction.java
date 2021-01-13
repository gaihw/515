
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
    "id",
    "trx",
    "block_time",
    "block_num",
    "last_irreversible_block",
    "traces"
})
public class Transaction {

    @JsonProperty("id")
    private String id;
    @JsonProperty("trx")
    private Trx trx;
    @JsonProperty("block_time")
    private String blockTime;
    @JsonProperty("block_num")
    private Integer blockNum;
    @JsonProperty("last_irreversible_block")
    private Integer lastIrreversibleBlock;
    @JsonProperty("traces")
    private List<Trace> traces = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("trx")
    public Trx getTrx() {
        return trx;
    }

    @JsonProperty("trx")
    public void setTrx(Trx trx) {
        this.trx = trx;
    }

    @JsonProperty("block_time")
    public String getBlockTime() {
        return blockTime;
    }

    @JsonProperty("block_time")
    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    @JsonProperty("block_num")
    public Integer getBlockNum() {
        return blockNum;
    }

    @JsonProperty("block_num")
    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }

    @JsonProperty("last_irreversible_block")
    public Integer getLastIrreversibleBlock() {
        return lastIrreversibleBlock;
    }

    @JsonProperty("last_irreversible_block")
    public void setLastIrreversibleBlock(Integer lastIrreversibleBlock) {
        this.lastIrreversibleBlock = lastIrreversibleBlock;
    }

    @JsonProperty("traces")
    public List<Trace> getTraces() {
        return traces;
    }

    @JsonProperty("traces")
    public void setTraces(List<Trace> traces) {
        this.traces = traces;
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
