package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeInfo {
    private String id;
    @JsonProperty("listen_addr")
    private String listenAddr;
    private String network;
    private String version;
    private String channels;
    private String moniker;
    private Map<String, Object> other;


}
