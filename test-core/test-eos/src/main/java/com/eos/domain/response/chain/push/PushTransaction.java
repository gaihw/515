
package com.eos.domain.response.chain.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "transaction_id",
    "processed"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushTransaction {

    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("processed")
    private Processed processed;

    @JsonProperty("transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transaction_id")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("processed")
    public Processed getProcessed() {
        return processed;
    }

    @JsonProperty("processed")
    public void setProcessed(Processed processed) {
        this.processed = processed;
    }

}
