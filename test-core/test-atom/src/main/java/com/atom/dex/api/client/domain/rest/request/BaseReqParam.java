package com.atom.dex.api.client.domain.rest.request;

import com.atom.dex.api.client.domain.rest.Coin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseReqParam {

    @JsonProperty("base_req")
    private BaseReq base_req;
    @JsonProperty("amount")
    private List<Coin> amount;
}
