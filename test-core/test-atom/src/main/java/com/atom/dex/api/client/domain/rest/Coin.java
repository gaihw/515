package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coin {

    private String denom;
    private String amount;

}
