
package com.eos.domain.response.chain.transaction.actions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "receiver",
    "act_digest",

    "recv_sequence",
    "auth_sequence",
    "code_sequence",
    "abi_sequence"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Receipt {

    @JsonProperty("receiver")
    public String receiver;
    @JsonProperty("act_digest")
    public String actDigest;

    @JsonProperty("recv_sequence")
    public Integer recvSequence;
    @JsonProperty("auth_sequence")
    public List<List<String>> authSequence = null;
    @JsonProperty("code_sequence")
    public Integer codeSequence;
    @JsonProperty("abi_sequence")
    public Integer abiSequence;

}
