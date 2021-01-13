package com.atom.dex.api.client.domain.rest.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTxResponse {

    private String type;
    private UnsignTx value;
}
