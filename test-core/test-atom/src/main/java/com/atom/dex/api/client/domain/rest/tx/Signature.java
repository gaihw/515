package com.atom.dex.api.client.domain.rest.tx;

import com.atom.dex.api.client.domain.rest.PubKey;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Signature {
    private String signature;
    private PubKey pubKey;
    private String accountNumber;
    private String sequence;
}
