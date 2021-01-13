
package com.eos.domain.response.chain.push;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
public class Receipt_ {

    @JsonProperty("receiver")
    private String receiver;
    @JsonProperty("act_digest")
    private String actDigest;

    @JsonProperty("recv_sequence")
    private Long recvSequence;
    @JsonProperty("auth_sequence")
    private List<List<String>> authSequence = null;
    @JsonProperty("code_sequence")
    private Long codeSequence;
    @JsonProperty("abi_sequence")
    private Long abiSequence;

    @JsonProperty("receiver")
    public String getReceiver() {
        return receiver;
    }

    @JsonProperty("receiver")
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @JsonProperty("act_digest")
    public String getActDigest() {
        return actDigest;
    }

    @JsonProperty("act_digest")
    public void setActDigest(String actDigest) {
        this.actDigest = actDigest;
    }


    @JsonProperty("recv_sequence")
    public Long getRecvSequence() {
        return recvSequence;
    }

    @JsonProperty("recv_sequence")
    public void setRecvSequence(Long recvSequence) {
        this.recvSequence = recvSequence;
    }

    @JsonProperty("auth_sequence")
    public List<List<String>> getAuthSequence() {
        return authSequence;
    }

    @JsonProperty("auth_sequence")
    public void setAuthSequence(List<List<String>> authSequence) {
        this.authSequence = authSequence;
    }

    @JsonProperty("code_sequence")
    public Long getCodeSequence() {
        return codeSequence;
    }

    @JsonProperty("code_sequence")
    public void setCodeSequence(Long codeSequence) {
        this.codeSequence = codeSequence;
    }

    @JsonProperty("abi_sequence")
    public Long getAbiSequence() {
        return abiSequence;
    }

    @JsonProperty("abi_sequence")
    public void setAbiSequence(Long abiSequence) {
        this.abiSequence = abiSequence;
    }

}
