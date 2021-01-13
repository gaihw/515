package com.atom.dex.api.client.domain.rest.reponse;

import com.atom.dex.api.client.domain.rest.Coin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class SendTo {
    private String fromAddress;
    private String toAddress;
    private List<Coin> amount;
}
