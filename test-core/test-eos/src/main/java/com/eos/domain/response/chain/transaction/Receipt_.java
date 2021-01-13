
package com.eos.domain.response.chain.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Receipt_ {

    @JsonProperty("receiver")
    private String receiver;
    @JsonProperty("act_digest")
    private String actDigest;
    @JsonProperty("recv_sequence")
    private Integer recvSequence;
    @JsonProperty("auth_sequence")
    private List<List<String>> authSequence = null;
    @JsonProperty("code_sequence")
    private Integer codeSequence;
    @JsonProperty("abi_sequence")
    private Integer abiSequence;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
    public Integer getRecvSequence() {
        return recvSequence;
    }

    @JsonProperty("recv_sequence")
    public void setRecvSequence(Integer recvSequence) {
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
    public Integer getCodeSequence() {
        return codeSequence;
    }

    @JsonProperty("code_sequence")
    public void setCodeSequence(Integer codeSequence) {
        this.codeSequence = codeSequence;
    }

    @JsonProperty("abi_sequence")
    public Integer getAbiSequence() {
        return abiSequence;
    }

    @JsonProperty("abi_sequence")
    public void setAbiSequence(Integer abiSequence) {
        this.abiSequence = abiSequence;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
