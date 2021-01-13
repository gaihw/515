
package com.eos.domain.response.chain.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "signatures",
    "compression",
    "packed_context_free_data",
    "context_free_data",
    "packed_trx",
    "transaction"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trx {

    @JsonProperty("id")
    private String id;
    @JsonProperty("signatures")
    private List<String> signatures = null;
    @JsonProperty("compression")
    private String compression;
    @JsonProperty("packed_context_free_data")
    private String packedContextFreeData;
    @JsonProperty("context_free_data")
    private List<Object> contextFreeData = null;
    @JsonProperty("packed_trx")
    private String packedTrx;
    @JsonProperty("transaction")
    private Transaction_ transaction;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("signatures")
    public List<String> getSignatures() {
        return signatures;
    }

    @JsonProperty("signatures")
    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    @JsonProperty("compression")
    public String getCompression() {
        return compression;
    }

    @JsonProperty("compression")
    public void setCompression(String compression) {
        this.compression = compression;
    }

    @JsonProperty("packed_context_free_data")
    public String getPackedContextFreeData() {
        return packedContextFreeData;
    }

    @JsonProperty("packed_context_free_data")
    public void setPackedContextFreeData(String packedContextFreeData) {
        this.packedContextFreeData = packedContextFreeData;
    }

    @JsonProperty("context_free_data")
    public List<Object> getContextFreeData() {
        return contextFreeData;
    }

    @JsonProperty("context_free_data")
    public void setContextFreeData(List<Object> contextFreeData) {
        this.contextFreeData = contextFreeData;
    }

    @JsonProperty("packed_trx")
    public String getPackedTrx() {
        return packedTrx;
    }

    @JsonProperty("packed_trx")
    public void setPackedTrx(String packedTrx) {
        this.packedTrx = packedTrx;
    }

    @JsonProperty("transaction")
    public Transaction_ getTransaction() {
        return transaction;
    }

    @JsonProperty("transaction")
    public void setTransaction(Transaction_ transaction) {
        this.transaction = transaction;
    }

}
