package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncInfo {
    @JsonProperty("latest_block_hash")
    private String latestBlockHash;
    @JsonProperty("latest_app_hash")
    private String latestAppHash;
    @JsonProperty("latest_block_height")
    private Long latestBlockHeight;
    @JsonProperty("latest_block_time")
    private String latestBlockTime;
    @JsonProperty("catching_up")
    private Boolean catchingUp;


}

