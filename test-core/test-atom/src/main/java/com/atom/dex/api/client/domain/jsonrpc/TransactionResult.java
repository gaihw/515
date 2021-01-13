package com.atom.dex.api.client.domain.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResult {
    private String hash;
    private Long height;
    private byte[] tx;
    @JsonProperty("tx_result")
    private TxResult txResult;

}
