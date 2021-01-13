package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidatorInfo {
    private String address;
    @JsonProperty("pub_key")
    private List<Integer> pubKey;
    @JsonProperty("voting_power")
    private Long votingPower;

}
