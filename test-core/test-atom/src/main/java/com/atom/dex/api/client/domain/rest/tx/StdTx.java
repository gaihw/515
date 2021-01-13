package com.atom.dex.api.client.domain.rest.tx;


import com.atom.dex.api.client.domain.rest.Coin;
import com.atom.dex.api.client.domain.rest.PubKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StdTx {

    private String msg;
    private Fee fee;
    private String  memo;
    private Signature signature;
}

