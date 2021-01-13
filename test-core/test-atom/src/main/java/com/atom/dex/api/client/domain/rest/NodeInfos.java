package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NodeInfos {

    @JsonProperty("node_info")
    private NodeInfo nodeInfo;
    @JsonProperty("sync_info")
    private SyncInfo syncInfo;
    @JsonProperty("validator_info")
    private ValidatorInfo validatorInfo;


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class ValidatorInfo {
        private String address;
        @JsonProperty("pub_key")
        private PubKey pubKey;
        @JsonProperty("voting_power")
        private Long votingPower;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class PubKey {
        private String type;
        private byte[] value;
    }

}
