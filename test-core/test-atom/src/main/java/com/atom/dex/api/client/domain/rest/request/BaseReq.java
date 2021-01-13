package com.atom.dex.api.client.domain.rest.request;

import com.atom.dex.api.client.domain.rest.Coin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class BaseReq {

    private String from;
    private String memo;
    private String chain_id;
    private String account_number;
    private String sequence;
    private String gas;
    private String gas_adjustment;
    private List<Coin> fees;
    private boolean simulate;
}
