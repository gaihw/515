package com.atom.dex.api.client.domain.rest;


import com.atom.dex.api.client.domain.rest.tx.StdTx;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxQuery {

    private String hash;
    private long height;
    private StdTx tx;
    private Result result;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Result {
        private String log;
        private String gasWanted;
        private String gasUsed;
        private List<KVPair> tags;

    }
}


