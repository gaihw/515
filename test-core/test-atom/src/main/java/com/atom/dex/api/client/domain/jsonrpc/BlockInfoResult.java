package com.atom.dex.api.client.domain.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockInfoResult {

    private Long total_count;
    private List<Transaction> txs;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Transaction {
        private String hash;
        private Long height;
        private Long index;
        private TxResult tx_result;
        private String tx;


    }


}
