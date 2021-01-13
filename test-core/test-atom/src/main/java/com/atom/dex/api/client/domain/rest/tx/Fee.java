package com.atom.dex.api.client.domain.rest.tx;

import com.atom.dex.api.client.domain.rest.Coin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fee {
    private String gas;
    private List<Coin> amount;
}
