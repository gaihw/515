package com.atom.dex.api.client.domain.rest.reponse;


import com.atom.dex.api.client.domain.rest.Coin;
import com.atom.dex.api.client.domain.rest.tx.Fee;
import com.atom.dex.api.client.domain.rest.tx.Signature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnsignTx {

    private List<Msg> msg;
    private Fee fee;
    private String memo;
    private List<Signature> signatures;


}

