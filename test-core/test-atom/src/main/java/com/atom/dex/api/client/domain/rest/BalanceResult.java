package com.atom.dex.api.client.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResult {
    private long height;
    private List<Coin> result;
}
